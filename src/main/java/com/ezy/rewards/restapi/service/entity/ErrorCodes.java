package com.ezy.rewards.restapi.service.entity;

public class ErrorCodes {
    private String Code;
    private String Context;

    public ErrorCodes() {
    }

    public ErrorCodes(String code, String context) {
        Code = code;
        Context = context;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getContext() {
        return Context;
    }

    public void setContext(String context) {
        Context = context;
    }

}
