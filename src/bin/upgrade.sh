#!/bin/sh

upgrade_script_dir=$(cd "$(dirname "$0")" && pwd)
cd "${upgrade_script_dir}"

# 前端包
web_package="${upgrade_script_dir}/web"
# 前端包部署目录
web_deploy_path="/data/apps/web/web"

# 前置机包
executor_package="${upgrade_script_dir}/ailand-data-collector-executor-release.tar.gz"
# 前置机端口
executor_port=6112
# 前置机部署目录
executor_deploy_path="/data/apps/ailand-web/package/ailand-data-collector-executor"

# 后端包
back_package="${upgrade_script_dir}/springboot-util-release.tar.gz"
# 后端端口
back_port=8866
# 后端包部署目录
back_deploy_path="/data/apps/springboot-util"
# 后端配置文件变更
back_change_config_path="${upgrade_script_dir}/application.properties"

# 日志文件路径
log_file="${upgrade_script_dir}/upgrade.log"
# 版本信息文件路径
version_info_file="${upgrade_script_dir}/version_info.json"
# 前一个版本
before_version=$(grep '"beforeVersion":' "${version_info_file}" | awk -F'"beforeVersion":"' '{print $2}' | awk -F'"' '{print $1}')

# 历史部署包备份目录
history_deploy_backup_path="/data/apps/HISTORY_DEPLOY_BACKUP"

# 提取 version_info.json 中的 log 字段并写入 upgrade.log（覆盖模式）
log_content=$(grep '"log":' "${version_info_file}" | awk -F'"log":"' '{print $2}' | awk -F'"' '{print $1}')
echo -e "${log_content}" > "${log_file}"

# 追加日志到 upgrade.log 和 version_info.json 的 log 字段
append_log() {
    local new_log="$1"  # 新日志内容（需包含时间戳和级别，如 "2025-05-29 19:27:23 INFO 新日志"）
    local current_time=$(date '+%Y-%m-%d %H:%M:%S')

    # 1. 追加到 upgrade.log（直接写入）
    echo "${current_time} ${new_log}" >> "${log_file}"

    # 2. 追加到 version_info.json 的 log 字段
    # 读取原 log 内容 -> 拼接新日志 -> 替换原文件中的 log 字段
    local original_log=$(grep '"log":' "${version_info_file}" | awk -F'"log":"' '{print $2}' | awk -F'"' '{print $1}')
    [ -z "${original_log}" ] && original_log=""  # 处理log字段不存在的情况

    # 拼接新日志：原始内容 + 换行符\n + 新日志
    local updated_log="${original_log}\\n${current_time} ${new_log}"

    # 转义替换字符串中的特殊字符（避免 sed 解析错误）
    local escaped_log=$(echo "$updated_log" | sed 's/\\/\\\\/g; s/\//\\\//g; s/\&/\\\&/g')

    # 使用sed替换log字段
    if grep -q '"log":' "${version_info_file}" 2>/dev/null; then
        sed -i.bak "s|\"log\":\"[^\"]*\"|\"log\":\"${escaped_log}\"|" "${version_info_file}"
    else
        # 新增log字段
        sed -i.bak -e ':begin' -e '$!N; s/\n//; tbegin' -e 's/\([^}]\)\s*\}/\1, "log": "'"${escaped_log}"'"}/' "${version_info_file}"
    fi

    # 清理临时文件
    [ -f "${version_info_file}.bak" ] && rm -rf "${version_info_file}.bak"
}

# 更新升级状态
set_upgrade_status() {
    local status="$1"
    local current_time=$(date '+%Y-%m-%d %H:%M:%S')

    # 转义特殊字符避免sed错误
    local escaped_status=$(echo "$status" | sed 's/\\/\\\\/g; s/\//\\\//g; s/\&/\\\&/g')

    if grep -q '"status":' "${version_info_file}" 2>/dev/null; then
        # 修改status字段
        sed -i.bak "s|\"status\":\"[^\"]*\"|\"status\":\"${escaped_status}\"|" "${version_info_file}"
    else
        # 新增状态字段
        sed -i.bak -e ':begin' -e '$!N; s/\n//; tbegin' -e "s/\([^}]\)\s*\}/\1, \"status\": \"${escaped_status}\"}/" "${version_info_file}"
    fi

    # 清理sed临时文件
    [ -f "${version_info_file}.bak" ] && rm -rf "${version_info_file}.bak"

    # 记录状态变更日志
    append_log "TRACE 升级状态变更：${status}"
}

# 错误处理函数（捕获未显式处理的异常）
handle_error() {
    local exit_code=$?       # 错误状态码
    local line_number=$1     # 错误行号（由trap传递）
    local current_time=$(date '+%Y-%m-%d %H:%M:%S')

    # 仅处理非0退出码（避免重复记录已显式处理的错误）
    if [ "${exit_code}" -ne 0 ]; then
        local error_msg="执行升级脚本异常：在第 ${line_number} 行失败，退出码 ${exit_code}"
        append_log "ERROR ${error_msg}"
        set_upgrade_status "FAILURE"   # 设置状态为FAILURE
        exit ${exit_code}    # 终止脚本（可选）
    fi
}

# 配置错误捕获：启用ERR信号监听，并传递行号给handle_error
set -E                      # 允许子函数继承trap
trap 'handle_error $LINENO' ERR  # 绑定ERR信号到错误处理函数
set -e

# 设置 version_info.json 中的 afterPath 字段
set_after_path() {
    local after_path="$1"  # 要设置的路径值
    # 转义特殊字符避免 sed 解析错误（兼容 \、/、& 等符号）
    local escaped_after_path=$(echo "${after_path}" | sed 's/\\/\\\\/g; s/\//\\\//g; s/\&/\\\&/g')

    # 检查是否已存在 afterPath 字段
    if grep -q '"afterPath":' "${version_info_file}" 2>/dev/null; then
        # 存在则替换值
        sed -i.bak "s|\"afterPath\":\"[^\"]*\"|\"afterPath\":\"${escaped_after_path}\"|" "${version_info_file}"
    else
        # 不存在则新增字段
        sed -i.bak -e ':begin' -e '$!N; s/\n//; tbegin' -e 's/\([^}]\)\s*\}/\1, "afterPath": "'"${escaped_after_path}"'"}/' "${version_info_file}"
    fi

    # 清理 sed 生成的临时备份文件
    [ -f "${version_info_file}.bak" ] && rm -rf "${version_info_file}.bak"
}



append_log "INFO ===================================================================================================="
# 检测端口${back_port}是否被监听，并提取对应进程PID（优先使用ss命令，兼容netstat）
back_pid=$(ss -tlnp 2>/dev/null | grep -E ":${back_port}\b" | awk -F'pid=' '{print $2}' | awk -F',' '{print $1}' | head -n1)
append_log "INFO [${back_port}] 端口进程 ---> PID: [${back_pid}]"
[ -z "$back_pid" ] && back_pid=$(netstat -tlnp 2>/dev/null | grep -E ":${back_port}\b" | awk '{print $7}' | cut -d'/' -f1 | head -n1)
# 若找到有效PID且进程存在，则获取其工作目录
if [ -n "$back_pid" ] && [ -e "/proc/$back_pid" ]; then
    cwd_path=$(readlink /proc/"$back_pid"/cwd 2>/dev/null)
    [ -n "$cwd_path" ] && back_deploy_path="$cwd_path"
    append_log "INFO [${back_port}] 端口工作目录 ---> [${back_deploy_path}]"
fi

# 创建历史备份目录
backup_dir="${history_deploy_backup_path}/${before_version}"
append_log "INFO 正在创建历史备份目录：[${backup_dir}]"
if ! mkdir -p "${backup_dir}"; then
    append_log "ERROR 历史备份目录创建失败：[${backup_dir}]（可能权限不足）"
    set_upgrade_status "FAILURE"
    exit 1
fi
append_log "INFO 历史备份目录创建完成"



has_web=0
has_exec=0
has_back=0
append_log "INFO ===================================================================================================="
# 前端升级
if [ -d "${web_package}" ]; then
    # 1. 检测前端升级包，设置升级包权限
    append_log "INFO 检测到前端升级包 ---> [${web_package}]"
    has_web=1
    append_log "INFO ->>> i.正在备份当前前端部署包：[${web_deploy_path}] -> [${backup_dir}/]"
    if [ ! -d "${web_deploy_path}" ]; then
        append_log "WARN 当前前端部署包不存在，无需备份：[${web_deploy_path}]"
    else
        if ! cp -r "${web_deploy_path}" "${backup_dir}/"; then
            append_log "WARN 当前前端部署包备份失败（可能权限不足或磁盘空间不足），源路径：[${web_deploy_path}]"
        else
            append_log "INFO 当前前端部署包备份完成"
        fi
    fi
    # 2. 删除旧部署包
    append_log "INFO ->>> ii.正在删除旧前端部署包：${web_deploy_path}"
    if ! rm -rf "${web_deploy_path}"; then
        append_log "WARN 旧前端部署包删除失败（可能文件被占用），路径：[${web_deploy_path}]"
    else
        append_log "INFO 旧前端部署包删除完成"
    fi
    # 3. 移动前端包到部署目录
    append_log "INFO ->>> iii.正在移动前端包到部署目录：[${web_package}] -> [${web_deploy_path}]"
    if ! mv "${web_package}" "${web_deploy_path}"; then
        append_log "ERROR 前端包移动失败（可能权限不足或目标路径已存在），目标路径：[${web_deploy_path}]"
        set_upgrade_status "FAILURE"
        exit 1
    fi
    # 4. 设置部署目录权限
    append_log "INFO ->>> iv.正在设置部署目录权限 -> [chmod 777 ${web_deploy_path}]"
    if ! chmod 777 "${web_deploy_path}"; then
        append_log "ERROR 部署目录权限设置失败 --文件-> [${web_deploy_path}]"
        set_upgrade_status "FAILURE"
        exit 1
    fi
    append_log "INFO 部署目录权限设置成功"
    append_log "INFO 前端升级完成 ---> [${web_deploy_path}]"
else
    append_log "WARN 未检测到前端升级包（${web_package}），跳过前端升级"
fi

append_log "INFO ===================================================================================================="
# 前置机升级
if [ -f "${executor_package}" ]; then
    # 1. 检测前置机升级包，设置升级包权限
    append_log "INFO 检测到前置机升级包 --文件-> [${executor_package}]"
    has_exec=1
    # 检测端口${executor_port}是否被监听，并提取对应进程PID（优先使用ss命令，兼容netstat）
    executor_pid=$(ss -tlnp 2>/dev/null | grep -E ":${executor_port}\b" | awk -F'pid=' '{print $2}' | awk -F',' '{print $1}' | head -n1)
    append_log "INFO [${executor_port}] 端口进程 ---> PID: [${executor_pid}]"
    [ -z "${executor_pid}" ] && executor_pid=$(netstat -tlnp 2>/dev/null | grep -E ":${executor_port}\b" | awk '{print $7}' | cut -d'/' -f1 | head -n1)
    # 若找到有效PID且进程存在，则获取其工作目录
    if [ -n "${executor_pid}" ] && [ -e "/proc/${executor_pid}" ]; then
        cwd_path=$(readlink /proc/"${executor_pid}"/cwd 2>/dev/null)
        [ -n "${cwd_path}" ] && executor_deploy_path="${cwd_path}"
        append_log "INFO [${executor_port}] 端口工作目录 ---> [${executor_deploy_path}]"
    fi
    append_log "INFO ->>> i.正在设置升级包权限 -> [chmod 777 ${executor_package}]"
    if ! chmod 777 "${executor_package}"; then
        append_log "ERROR 升级包权限设置失败 --文件-> [${executor_package}]"
        set_upgrade_status "FAILURE"
        exit 1
    fi
    append_log "INFO 升级包权限设置成功"
    # 2. 备份当前前置机部署目录到历史备份目录
    append_log "INFO ->>> ii.正在备份当前前置机部署包：[${executor_deploy_path}] -> [${backup_dir}/]"
    if [ ! -d "${executor_deploy_path}" ]; then
        append_log "WARN 当前前置机部署包不存在，无需备份：[${executor_deploy_path}]"
    else
        if ! cp -r "${executor_deploy_path}" "${backup_dir}/"; then
            append_log "ERROR 当前前置机部署包备份失败（可能权限不足或磁盘空间不足），源路径：[${executor_deploy_path}]"
            set_upgrade_status "FAILURE"
            exit 1
        else
            append_log "INFO 当前前置机部署包备份完成"
        fi
    fi
    # 3. 解压前置机包到临时目录
    temp_unzip_dir_executor="${upgrade_script_dir}/temp_unzip_executor"
    append_log "INFO ->>> iii.正在创建前置机临时解压目录：[${temp_unzip_dir_executor}]"
    if ! mkdir -p "${temp_unzip_dir_executor}"; then
        append_log "ERROR 前置机临时解压目录创建失败（可能权限不足）"
        set_upgrade_status "FAILURE"
        exit 1
    fi
    append_log "INFO 正在解压前置机包：[${executor_package}] -> [${temp_unzip_dir_executor}]"
    if ! tar -zxvf "${executor_package}" -C "${temp_unzip_dir_executor}"; then
        append_log "ERROR 前置机包解压失败（可能文件损坏或权限不足）"
        set_upgrade_status "FAILURE"
        exit 1
    fi
    append_log "INFO 前置机包解压完成"
    # 4. 覆盖旧配置文件（application-*）
    unzipped_dir_executor=$(ls -d "${temp_unzip_dir_executor}"/* 2>/dev/null | head -n1)  # 获取解压后的主目录
    if [ -z "${unzipped_dir_executor}" ] || [ ! -d "${unzipped_dir_executor}" ]; then
        append_log "ERROR 解压后未找到前置机目录，路径：[${temp_unzip_dir_executor}]"
        set_upgrade_status "FAILURE"
        exit 1
    fi
    source_config_dir="${executor_deploy_path}/config"
    target_config_dir="${unzipped_dir_executor}/config"
    append_log "INFO ->>> iv.正在覆盖配置文件：[${source_config_dir}/application-*] -> [${target_config_dir}]"
    if [ -d "${source_config_dir}" ]; then
        # 检查是否存在待迁移的旧配置文件
        if [ -n "$(echo "${source_config_dir}"/application-* 2>/dev/null | grep -v '\*')" ]; then
            # 清理目标目录中的旧配置文件（避免残留）
            if [ -d "${target_config_dir}" ]; then
                rm -rf "${target_config_dir}"/application-* 2>/dev/null
                append_log "INFO 已清理目标目录配置文件：[${target_config_dir}/application-*]"
            else
                append_log "WARN 目标配置目录不存在：[${target_config_dir}]，跳过清理"
            fi
            # 执行配置文件复制
            if ! cp -r "${source_config_dir}"/application-* "${target_config_dir}/" 2>/dev/null; then
                append_log "WARN 配置文件覆盖失败（可能权限不足或文件类型不匹配），跳过"
            else
                append_log "INFO 配置文件覆盖完成"
            fi
        else
            append_log "INFO 无待迁移的旧配置文件（${source_config_dir}/application-*），跳过覆盖"
        fi
    else
        append_log "WARN 旧前置机配置目录不存在：[${source_config_dir}]，无需覆盖"
    fi
    # 5. 覆盖启停脚本的逻辑
    source_start_sh="${executor_deploy_path}/start.sh"
    target_start_sh="${unzipped_dir_executor}/start.sh"
    append_log "INFO ->>> v.准备覆盖 start.sh 脚本: [${source_start_sh}] -> [${target_start_sh}]"
    # 检查源 start.sh 是否存在
    if [ -f "${source_start_sh}" ]; then
        # 清理目标目录中旧的 start.sh（如果存在）
        if [ -f "${target_start_sh}" ]; then
            rm -f "${target_start_sh}"
            append_log "INFO 已清理目标旧 start.sh: [${target_start_sh}]"
        else
            append_log "WARN 目标路径暂无非 start.sh 文件，跳过清理"
        fi
        # 执行 start.sh 复制
        if ! cp -p "${source_start_sh}" "${target_start_sh}" 2>/dev/null; then
            # -p 保留文件属性（如权限等），可根据需求决定是否保留
            append_log "WARN start.sh 覆盖失败（可能权限不足等），跳过"
        else
            append_log "INFO start.sh 覆盖完成"
        fi
    else
        append_log "INFO 源 start.sh (${source_start_sh}) 不存在，跳过覆盖"
    fi
    # 覆盖 stop.sh 脚本的逻辑（前置机服务）
    source_stop_sh="${executor_deploy_path}/stop.sh"
    target_stop_sh="${unzipped_dir_executor}/stop.sh"
    append_log "INFO ->>> v.准备覆盖 stop.sh 脚本: [${source_stop_sh}] -> [${target_stop_sh}]"
    # 检查源 stop.sh 是否存在
    if [ -f "${source_stop_sh}" ]; then
        # 清理目标目录中旧的 stop.sh（如果存在）
        if [ -f "${target_stop_sh}" ]; then
            rm -f "${target_stop_sh}"
            append_log "INFO 已清理目标旧 stop.sh: [${target_stop_sh}]"
        else
            append_log "WARN 目标路径暂无非 stop.sh 文件，跳过清理"
        fi
        # 执行 stop.sh 复制
        if ! cp -p "${source_stop_sh}" "${target_stop_sh}" 2>/dev/null; then
            # -p 保留文件属性（如权限等），可根据需求决定是否保留
            append_log "WARN stop.sh 覆盖失败（可能权限不足等），跳过"
        else
            append_log "INFO stop.sh 覆盖完成"
        fi
    else
        append_log "INFO 源 stop.sh (${source_stop_sh}) 不存在，跳过覆盖"
    fi
    # 6. 执行 stop.sh 脚本终止前置机进程
    append_log "INFO ->>> vi.正在通过 stop.sh 脚本停止前置机 [${executor_port}] 端口关联进程"
    # 使用 shell 执行 stop.sh，隔离目录变更，同时捕获标准输出、错误输出到日志
    if ! (cd "${executor_deploy_path}" && sh "${source_stop_sh}" >> "${log_file}" 2>&1); then
        append_log "ERROR 执行 [${source_stop_sh}] 脚本终止进程失败（详细输出见上方日志）"
        set_upgrade_status "FAILURE"
        exit 1
    else
        append_log "INFO 已执行 [${source_stop_sh}] 脚本，尝试终止进程（输出已捕获到日志）"
        max_wait=60
        append_log "INFO 正在检查进程终止状态（最多等待 ${max_wait} 秒）..."
        start_time=$(date +%s)
        interval=2  # 初始检查间隔（秒）
        max_interval=10  # 最大间隔时间，避免后期等待过长
        while true; do
            # 计算已等待时间
            current_time=$(date +%s)
            elapsed=$((current_time - start_time))
            # 检查是否超时
            if [ $elapsed -ge $max_wait ]; then
                append_log "ERROR 等待超时（${max_wait}秒），进程 [${executor_pid}] 仍未终止"
                set_upgrade_status "FAILURE"
                exit 1
            fi
            # 检查进程是否存在
            if [ -n "${executor_pid}" ] && [ -e "/proc/${executor_pid}" ]; then
                append_log "INFO 进程 [${executor_pid}] 仍在运行，已等待 [${elapsed}/${max_wait}] 秒，等待 ${interval} 秒后重试..."
                sleep $interval
                # 动态调整下一次检查间隔（指数递增，但不超过 max_interval）
                next_interval=$((interval * 2))
                interval=$((next_interval > max_interval ? max_interval : next_interval))
            else
                append_log "INFO 进程 [${executor_pid}] 已成功终止，耗时 [${elapsed}] 秒"
                break
            fi
        done
    fi
    # 7. 删除旧前置机部署目录
    append_log "INFO ->>> vii.正在删除旧前置机部署目录：[${executor_deploy_path}]"
    if ! rm -rf "${executor_deploy_path}"; then
        append_log "ERROR 旧前置机部署目录删除失败（可能文件被占用）"
        set_upgrade_status "FAILURE"
        exit 1
    else
        append_log "INFO 旧前置机部署目录删除完成"
    fi
    # 8. 移动解压后的包到部署目录
    append_log "INFO ->>> viii.正在移动新前置机包到部署目录：[${unzipped_dir_executor}] -> [${executor_deploy_path}]"
    if ! mv "${unzipped_dir_executor}" "${executor_deploy_path}"; then
        append_log "ERROR 前置机包移动失败（可能权限不足或路径已存在）"
        set_upgrade_status "FAILURE"
        exit 1
    fi
    append_log "INFO 完成前置机包移动到部署目录"
    # 9. 执行启动脚本
    start_script="${executor_deploy_path}/start.sh"
    append_log "INFO ->>> ix.正在执行启动脚本：[${start_script}]"
    if [ ! -d "${executor_deploy_path}" ]; then
        append_log "ERROR 前置机部署目录不存在：[${executor_deploy_path}]（可能移动新包失败）"
        set_upgrade_status "FAILURE"
        exit 1
    fi
    if [ ! -x "${executor_deploy_path}" ]; then
        append_log "ERROR 无权限进入前置机部署目录：[${executor_deploy_path}]（检查目录权限）"
        set_upgrade_status "FAILURE"
        exit 1
    fi
    if [ ! -f "${start_script}" ]; then
        append_log "ERROR 启动脚本不存在：[${start_script}]"
        set_upgrade_status "FAILURE"
        exit 1
    fi
    if ! chmod +x "${start_script}"; then
        append_log "ERROR 启动脚本权限设置失败"
        set_upgrade_status "FAILURE"
        exit 1
    fi
    if ss -tln | grep -q ":${executor_port}\b"; then
        append_log "ERROR 端口 [${executor_port}] 被占用，无法启动服务"
        set_upgrade_status "FAILURE"
        exit 1
    fi
    # 使用子 shell 执行启动脚本，隔离目录变更，并捕获输出到日志
    if ! (cd "${executor_deploy_path}" && sh "${start_script}" >> "${log_file}" 2>&1); then
        append_log "ERROR 启动脚本执行失败（详细输出见上方日志）"
        set_upgrade_status "FAILURE"
        exit 1
    fi
    append_log "INFO 启动脚本执行完成"
    # 10. 检查端口是否启动
    max_wait=60  # 最长等待时间（秒），可根据实际调整
    append_log "INFO ->>> x.等待检查端口 [${executor_port}] 状态（最长等待 ${max_wait} 秒）"
    # 初始化等待时间和间隔
    start_time=$(date +%s)
    check_interval=5
    while true; do
        # 检查当前等待时间是否超时
        current_time=$(date +%s)
        elapsed_time=$((current_time - start_time))
        if [ $elapsed_time -ge $max_wait ]; then
            append_log "ERROR 前置机端口 [${executor_port}] 启动超时（最长等待 ${max_wait} 秒），未成功启动，请联系运维检查服务的启动日志"
            set_upgrade_status "FAILURE"
            exit 1
        fi
        # 获取端口对应的 PID（优先用 ss，兼容 netstat）
        new_pid=$(ss -tnlp 2>/dev/null | grep -E ":${executor_port}\b" | awk -F'pid=' '{print $2}' | awk -F',' '{print $1}' | head -n1)
        if [ -z "$new_pid" ]; then
            new_pid=$(netstat -tnlp 2>/dev/null | grep -E ":${executor_port}\b" | awk '{print $7}' | cut -d'/' -f1 | head -n1)
        fi
        # 检查 PID 有效性：存在且进程目录可访问
        if [ -n "$new_pid" ] && [ -e "/proc/$new_pid" ]; then
            append_log "INFO 前置机端口 [${executor_port}] 已启动，PID: [${new_pid}]，耗时[${elapsed_time}] 秒"
            break
        fi
        # 未检测到端口启动，等待后继续检查
        append_log "TRACE 前置机端口 [${executor_port}] 尚未启动，已等待 [${elapsed_time}/${max_wait}] 秒，继续等待[${check_interval}] 秒"
        sleep ${check_interval}
    done
    append_log "INFO 前置机升级完成 ---> [${executor_deploy_path}]"
else
    append_log "WARN 未检测到前置机升级包（${executor_package}），跳过前置机升级"
fi

append_log "INFO ===================================================================================================="
# 后端升级
if [ -f "${back_package}" ]; then
    # 1. 检测后端升级包，设置升级包权限
    append_log "INFO 检测到后端升级包 --文件-> [${back_package}]"
    has_back=1
    append_log "INFO ->>> i.正在设置升级包权限 -> [chmod 777 ${back_package}]"
    if ! chmod 777 "${back_package}"; then
        append_log "ERROR 升级包权限设置失败 --文件-> [${back_package}]"
        set_upgrade_status "FAILURE"
        exit 1
    fi
    append_log "INFO 升级包权限设置成功"
    # 2. 备份当前后端部署目录到历史备份目录
    append_log "INFO ->>> ii.正在备份当前后端部署包：[${back_deploy_path}] -> [${backup_dir}/]"
    if [ ! -d "${back_deploy_path}" ]; then
        append_log "WARN 当前后端部署包不存在，无需备份：[${back_deploy_path}]"
    else
        if ! cp -r "${back_deploy_path}" "${backup_dir}/"; then
            append_log "ERROR 当前后端部署包备份失败（可能权限不足或磁盘空间不足），源路径：[${back_deploy_path}]"
            set_upgrade_status "FAILURE"
            exit 1
        else
            append_log "INFO 当前后端部署包备份完成"
        fi
    fi
    # 3. 解压后端包到临时目录
    temp_unzip_dir_back="${upgrade_script_dir}/temp_unzip_back"
    append_log "INFO ->>> iii.正在创建后端临时解压目录：[${temp_unzip_dir_back}]"
    if ! mkdir -p "${temp_unzip_dir_back}"; then
        append_log "ERROR 后端临时解压目录创建失败（可能权限不足）"
        set_upgrade_status "FAILURE"
        exit 1
    fi
    append_log "INFO 正在解压后端包：[${back_package}] -> [${temp_unzip_dir_back}]"
    if ! tar -zxvf "${back_package}" -C "${temp_unzip_dir_back}"; then
        append_log "ERROR 后端包解压失败（可能文件损坏或权限不足）"
        set_upgrade_status "FAILURE"
        exit 1
    fi
    append_log "INFO 后端包解压完成"
    # 4. 覆盖旧配置文件（application-*）
    unzipped_dir_back=$(ls -d "${temp_unzip_dir_back}"/* 2>/dev/null | head -n1)  # 获取解压后的主目录
    if [ -z "${unzipped_dir_back}" ] || [ ! -d "${unzipped_dir_back}" ]; then
        append_log "ERROR 解压后未找到后端目录，路径：[${temp_unzip_dir_back}]"
        set_upgrade_status "FAILURE"
        exit 1
    fi
    source_config_dir="${back_deploy_path}/config"
    target_config_dir="${unzipped_dir_back}/config"
    append_log "INFO ->>> iv.正在覆盖配置文件：[${source_config_dir}/application-*] -> [${target_config_dir}]"
    if [ -d "${source_config_dir}" ]; then
        # 检查是否存在待迁移的旧配置文件
        if [ -n "$(echo "${source_config_dir}"/application-* 2>/dev/null | grep -v '\*')" ]; then
            # 清理目标目录中的旧配置文件（避免残留）
            if [ -d "${target_config_dir}" ]; then
                rm -rf "${target_config_dir}"/application-* 2>/dev/null
                append_log "INFO 已清理目标目录配置文件：[${target_config_dir}/application-*]"
            else
                append_log "WARN 目标配置目录不存在：[${target_config_dir}]，跳过清理"
            fi
            # 执行配置文件复制
            if ! cp -r "${source_config_dir}"/application-* "${target_config_dir}/" 2>/dev/null; then
                append_log "WARN 配置文件覆盖失败（可能权限不足或文件类型不匹配），跳过"
            else
                append_log "INFO 配置文件覆盖完成"
            fi
        else
            append_log "INFO 无待迁移的旧配置文件（${source_config_dir}/application-*），跳过覆盖"
        fi
    else
        append_log "WARN 旧后端配置目录不存在：[${source_config_dir}]，无需覆盖"
    fi
    # 5. 变更配置文件
    append_log "INFO ->>> v.正在变更配置文件"
    if [ -f "${back_change_config_path}" ]; then
        append_log "INFO 检测到变更配置文件：[${back_change_config_path}]，开始变更配置..."
        # 清理配置文件中的Windows换行符（\r）
        if sed -i 's/\r$//' "${back_change_config_path}"; then
            append_log "INFO 成功清理变更配置文件[${back_change_config_path}]的换行符（\r）"
        else
            append_log "WARN 清理变更配置文件[${back_change_config_path}]的换行符失败，可能影响后续配置解析"
        fi
        # 检查目标配置目录是否存在
        if [ ! -d "${target_config_dir}" ]; then
            append_log "ERROR 目标配置目录不存在：[${target_config_dir}]，无法应用自定义配置"
            set_upgrade_status "FAILURE"
            exit 1
        fi
        # 移除 UTF-8 BOM
        sed -i '1s/^\xEF\xBB\xBF//' "${back_change_config_path}"
        echo >> "${back_change_config_path}"  # 添加空行（即换行符）
        # 逐行读取变更配置文件
        while IFS= read -r line; do
            # 跳过空行和注释行（以#开头）
            if [[ -z "${line}" || "${line}" =~ ^[[:space:]]*# ]]; then
                continue
            fi
            # 提取键和值（分割等号，去除前后空格）
            key=$(echo "${line}" | awk -F '=' '{print $1}' | sed 's/^[[:space:]]*//; s/[[:space:]]*$//')
            value=$(echo "${line}" | cut -d '=' -f2- | sed 's/^[[:space:]]*//; s/[[:space:]]*$//')
            # 校验键是否有效（非空且非纯空格）
            if [ -z "${key}" ] || [[ "${key}" == *"="* ]]; then
                append_log "WARN 变更配置文件中存在无效行：[${line}]，跳过"
                continue
            fi
            append_log "DEBUG 解析配置行：[${line}] -> key=[${key}], value=[${value}]"
            # 转义key中的正则特殊字符（.^$*+?()[{\|）
            escaped_key=$(echo "${key}" | sed 's/[][\.^$*+?(){\\|]/\\&/g')
            # 转义值中的特殊字符（避免sed替换错误）
            escaped_value=$(echo "${value}" | sed 's/\\/\\\\/g; s/\//\\\//g; s/\&/\\\&/g')
            # 遍历目标目录下所有application-*.properties文件
            for config_file in "${target_config_dir}"/application-*.properties; do
                if [ ! -f "${config_file}" ]; then
                    append_log "WARN 目标配置文件不存在：[${config_file}]，跳过"
                    continue
                fi
                # 使用 grep -q 静默检查
                if grep -q -E "^[[:blank:]]*${escaped_key}[[:blank:]]*=" "${config_file}" 2>>"${log_file}"; then
                    # 键存在：执行替换
                    sed -i.bak "/^[[:blank:]]*${escaped_key}[[:blank:]]*=/s|=.*|=${escaped_value}|" "${config_file}"
                    # 检查是否实际修改（diff 对比）
                    if diff "${config_file}" "${config_file}.bak" >/dev/null 2>&1; then
                        append_log "WARN 在文件 [${config_file}] 中配置项 ${key} 已存在且值相同，无需更新"
                    else
                        append_log "INFO 在文件 [${config_file}] 中更新配置项：${key}=${value}"
                    fi
                    rm -rf "${config_file}.bak"
                else
                    # 键不存在：追加新配置
                    echo "${key}=${value}" >> "${config_file}"
                    append_log "INFO 在文件 [${config_file}] 中新增配置项：${key}=${value}"
                fi
            done
        done < "${back_change_config_path}"
        append_log "INFO 完成配置文件变更"

    else
        append_log "WARN 未检测到变更配置文件 [${back_change_config_path}]，跳过变更"
    fi
    # 6. 覆盖启停脚本的逻辑
    source_start_sh="${back_deploy_path}/start.sh"
    target_start_sh="${unzipped_dir_back}/start.sh"
    append_log "INFO ->>> vi.准备覆盖 start.sh 脚本: [${source_start_sh}] -> [${target_start_sh}]"
    # 检查源 start.sh 是否存在
    if [ -f "${source_start_sh}" ]; then
        # 清理目标目录中旧的 start.sh（如果存在）
        if [ -f "${target_start_sh}" ]; then
            rm -f "${target_start_sh}"
            append_log "INFO 已清理目标旧 start.sh: [${target_start_sh}]"
        else
            append_log "WARN 目标路径暂无非 start.sh 文件，跳过清理"
        fi
        # 执行 start.sh 复制
        if ! cp -p "${source_start_sh}" "${target_start_sh}" 2>/dev/null; then
            # -p 保留文件属性（如权限等），可根据需求决定是否保留
            append_log "WARN start.sh 覆盖失败（可能权限不足等），跳过"
        else
            append_log "INFO start.sh 覆盖完成"
        fi
    else
        append_log "INFO 源 start.sh (${source_start_sh}) 不存在，跳过覆盖"
    fi
    # 覆盖 stop.sh 脚本的逻辑
    source_stop_sh="${back_deploy_path}/stop.sh"
    target_stop_sh="${unzipped_dir_back}/stop.sh"
    append_log "INFO ->>> vi.准备覆盖 stop.sh 脚本: [${source_stop_sh}] -> [${target_stop_sh}]"
    # 检查源 stop.sh 是否存在
    if [ -f "${source_stop_sh}" ]; then
        # 清理目标目录中旧的 stop.sh（如果存在）
        if [ -f "${target_stop_sh}" ]; then
            rm -f "${target_stop_sh}"
            append_log "INFO 已清理目标旧 stop.sh: [${target_stop_sh}]"
        else
            append_log "WARN 目标路径暂无非 stop.sh 文件，跳过清理"
        fi
        # 执行 stop.sh 复制
        if ! cp -p "${source_stop_sh}" "${target_stop_sh}" 2>/dev/null; then
            # -p 保留文件属性（如权限等），可根据需求决定是否保留
            append_log "WARN stop.sh 覆盖失败（可能权限不足等），跳过"
        else
            append_log "INFO stop.sh 覆盖完成"
        fi
    else
        append_log "INFO 源 stop.sh (${source_stop_sh}) 不存在，跳过覆盖"
    fi
    # 7. 执行 stop.sh 脚本终止后端进程
    append_log "INFO ->>> vii.正在通过 [${source_stop_sh}] 脚本停止后端 [${back_port}] 端口关联进程"
    # 使用 shell 执行 stop.sh，隔离目录变更，同时捕获标准输出、错误输出到日志
    if ! (cd "${back_deploy_path}" && sh "${source_stop_sh}" >> "${log_file}" 2>&1); then
        append_log "ERROR 执行 [${source_stop_sh}] 脚本终止进程失败（详细输出见上方日志）"
        set_upgrade_status "FAILURE"
        exit 1
    else
        append_log "INFO 已执行 [${source_stop_sh}] 脚本，尝试终止进程（输出已捕获到日志）"
        max_wait=60
        append_log "INFO 正在检查进程终止状态（最多等待 ${max_wait} 秒）..."
        start_time=$(date +%s)
        interval=2  # 初始检查间隔（秒）
        max_interval=10  # 最大间隔时间，避免后期等待过长
        while true; do
            # 计算已等待时间
            current_time=$(date +%s)
            elapsed=$((current_time - start_time))
            # 检查是否超时
            if [ $elapsed -ge $max_wait ]; then
                append_log "ERROR 等待超时（${max_wait}秒），进程 [${back_pid}] 仍未终止"
                set_upgrade_status "FAILURE"
                exit 1
            fi
            # 检查进程是否存在
            if [ -n "${back_pid}" ] && [ -e "/proc/${back_pid}" ]; then
                append_log "INFO 进程 [${back_pid}] 仍在运行，已等待 [${elapsed}/${max_wait}] 秒，等待 ${interval} 秒后重试..."
                sleep $interval
                # 动态调整下一次检查间隔（指数递增，但不超过 max_interval）
                next_interval=$((interval * 2))
                interval=$((next_interval > max_interval ? max_interval : next_interval))
            else
                append_log "INFO 进程 [${back_pid}] 已成功终止，耗时 [${elapsed}] 秒"
                break
            fi
        done
    fi
    # 8. 删除旧后端部署目录
    append_log "INFO ->>> viii.正在删除旧后端部署目录：[${back_deploy_path}]"
    if ! rm -rf "${back_deploy_path}"; then
        append_log "ERROR 旧后端部署目录删除失败（可能文件被占用）"
        set_upgrade_status "FAILURE"
        exit 1
    else
        append_log "INFO 旧后端部署目录删除完成"
    fi
    # 9. 移动解压后的包到部署目录
    append_log "INFO ->>> ix.正在移动新后端包到部署目录：[${unzipped_dir_back}] -> [${back_deploy_path}]"
    if ! mv "${unzipped_dir_back}" "${back_deploy_path}"; then
        append_log "ERROR 后端包移动失败（可能权限不足或路径已存在）"
        set_upgrade_status "FAILURE"
        exit 1
    fi
    append_log "INFO 完成后端包移动到部署目录"
    # 新增：将后端部署路径写入 version_info.json 的 afterPath 字段
    set_after_path "${back_deploy_path}"
    # 10. 执行启动脚本
    start_script="${back_deploy_path}/start.sh"
    append_log "INFO ->>> x.正在执行启动脚本：[${start_script}]"
    if [ ! -d "${back_deploy_path}" ]; then
        append_log "ERROR 后端部署目录不存在：[${back_deploy_path}]（可能移动新包失败）"
        set_upgrade_status "FAILURE"
        exit 1
    fi
    if [ ! -x "${back_deploy_path}" ]; then
        append_log "ERROR 无权限进入后端部署目录：[${back_deploy_path}]（检查目录权限）"
        set_upgrade_status "FAILURE"
        exit 1
    fi
    if [ ! -f "${start_script}" ]; then
        append_log "ERROR 启动脚本不存在：[${start_script}]"
        set_upgrade_status "FAILURE"
        exit 1
    fi
    if ! chmod +x "${start_script}"; then
        append_log "ERROR 启动脚本权限设置失败"
        set_upgrade_status "FAILURE"
        exit 1
    fi
    if ss -tln | grep -q ":${back_port}\b"; then
        append_log "ERROR 端口 [${back_port}] 被占用，无法启动服务"
        set_upgrade_status "FAILURE"
        exit 1
    fi
    # 使用子 shell 执行启动脚本，隔离目录变更，并捕获输出到日志
    if ! (cd "${back_deploy_path}" && sh "${start_script}" >> "${log_file}" 2>&1); then
        append_log "ERROR 启动脚本执行失败（详细输出见上方日志）"
        set_upgrade_status "FAILURE"
        exit 1
    fi
    append_log "INFO 启动脚本执行完成"
    # 复制版本信息到后端工作目录
    cp -f "${version_info_file}" "${back_deploy_path}/"
    # 11. 检查端口是否启动
    max_wait=60
    append_log "INFO ->>> xi.等待检查端口 [${back_port}] 状态，（最长等待 ${max_wait} 秒）"
    # 初始化等待时间和间隔
    start_time=$(date +%s)
    check_interval=5
    while true; do
        # 检查当前等待时间是否超过最长等待时间
        current_time=$(date +%s)
        elapsed_time=$((current_time - start_time))
        if [ $elapsed_time -ge $max_wait ]; then
            append_log "ERROR 后端端口 [${back_port}] 启动超时（最长等待 ${max_wait} 秒），未成功启动，请联系运维检查服务的启动日志"
            set_upgrade_status "FAILURE"
            cp -f "${version_info_file}" "${back_deploy_path}/" 2>/dev/null  # 复制版本信息（即使失败）
            exit 1
        fi

        # 检查端口对应的 PID
        new_pid=$(ss -tnlp 2>/dev/null | grep -E ":${back_port}\b" | awk -F'pid=' '{print $2}' | awk -F',' '{print $1}' | head -n1)
        if [ -z "$new_pid" ]; then
            new_pid=$(netstat -tnlp 2>/dev/null | grep -E ":${back_port}\b" | awk '{print $7}' | cut -d'/' -f1 | head -n1)
        fi

        # 如果获取到 PID 且进程存在，则端口已启动
        if [ -n "$new_pid" ] && [ -e "/proc/$new_pid" ]; then
            append_log "INFO 后端端口 [${back_port}] 已启动，PID: [${new_pid}]，耗时[${elapsed_time}] 秒"
            break
        fi

        # 未检测到端口启动，等待一段时间后继续检查
        append_log "TRACE 后端端口 [${back_port}] 尚未启动，已等待 [${elapsed_time}/${max_wait}] 秒，继续等待[${check_interval}] 秒"
        sleep ${check_interval}
    done
    append_log "INFO 后端升级完成 ---> [${back_deploy_path}]"
else
    append_log "WARN 未检测到后端升级包（${back_package}），跳过后端升级"
fi

append_log "INFO ===================================================================================================="
# 检查是否同时不存在前端和后端升级包
if [ ${has_web} -eq 0 ] && [ ${has_exec} -eq 0 ] && [ ${has_back} -eq 0 ]; then
    append_log "WARN 未检测任何升级包"
    set_upgrade_status "FAILURE"
    exit 1
fi

# 所有步骤成功完成
set_upgrade_status "SUCCESS"
# 新增：将后端部署路径写入 version_info.json 的 afterPath 字段
set_after_path "${back_deploy_path}"
# 复制版本信息到后端工作目录
cp -f "${version_info_file}" "${back_deploy_path}/"
exit 0