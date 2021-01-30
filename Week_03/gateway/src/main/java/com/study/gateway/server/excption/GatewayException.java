package com.study.gateway.server.excption;

public class GatewayException extends Exception{
    private int code;
    private String message;

    public GatewayException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;

    }

    public int getCode() {
        return code;
    }
}
