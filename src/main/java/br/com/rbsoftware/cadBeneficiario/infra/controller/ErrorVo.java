package br.com.rbsoftware.cadBeneficiario.infra.controller;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

public class ErrorVo {
    public String message;
    public String url;
    @JsonSetter(nulls = Nulls.SKIP)
    private String stackTrace;

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

}
