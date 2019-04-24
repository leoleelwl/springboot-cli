package com.jhy.app.common.exceptions;

/**
 * FEBS 系统内部异常
 */
public class AppException extends Exception {

    private static final long serialVersionUID = -994962710559017255L;

    public AppException(String message) {
        super(message);
    }
}
