package com.andon.springbootutil.constant;

/**
 * @author Andon
 * 2025/5/27
 */
public enum UpgradeStatus {
    /**
     * 待升级
     */
    WAIT,
    /**
     * 升级中
     */
    UPGRADING,
    /**
     * 升级成功
     */
    SUCCESS,
    /**
     * 升级失败
     */
    FAILURE,
    /**
     * 已失效
     */
    INVALID,
}
