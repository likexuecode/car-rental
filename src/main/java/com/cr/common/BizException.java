package com.cr.common;

/**
 * 业务异常：统一由全局异常处理器转换为 Result.fail(msg)
 */
public class BizException extends RuntimeException {

    public BizException(String message) {
        super(message);
    }
}
