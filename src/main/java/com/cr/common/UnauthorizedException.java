package com.cr.common;

/**
 * 未登录或权限不足异常
 */
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }
}
