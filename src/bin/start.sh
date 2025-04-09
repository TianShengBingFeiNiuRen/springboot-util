#!/bin/sh
source /etc/profile

APPLICATION="springboot-util"
APPLICATION_JAR="${APPLICATION}.jar"

workspace=$(cd $(dirname $0); pwd)

PID=$(ps -ef | grep "${workspace}/lib/${APPLICATION_JAR}" | grep -v grep | awk '{print $2}')
if [ -n "$PID" ]; then
    echo "程序进程已存在，将不会执行启动操作"
    exit 0
fi

LOG_DIR="${workspace}/log"
mkdir -p ${LOG_DIR}

JAVA_OPT="-server -XX:NewRatio=2 -XX:SurvivorRatio=8 -Xms8g -Xmx16g"
JAVA_OPT="${JAVA_OPT} -XX:-OmitStackTraceInFastThrow"

JVM_REMOTE_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=7766"

echo "$(date +'%Y-%m-%d %H:%M:%S') 等待启动 ${APPLICATION}..."
nohup java ${JAVA_OPT} ${JVM_REMOTE_OPTS} -Dlog_dir=${LOG_DIR} -jar ${workspace}/lib/${APPLICATION_JAR} > /dev/null 2>&1 &
sleep 5s
echo "$(date +'%Y-%m-%d %H:%M:%S') ${APPLICATION} started..."

# sed -i 's/\r$//' filename.sh