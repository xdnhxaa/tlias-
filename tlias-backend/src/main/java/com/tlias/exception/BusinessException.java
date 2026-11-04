package com.tlias.exception;

/** 业务异常：会被全局异常处理器捕获并返回友好提示 */
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
