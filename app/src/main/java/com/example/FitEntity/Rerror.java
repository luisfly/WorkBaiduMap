package com.example.FitEntity;

/**
 * 错误返回
 */
public class Rerror extends HttpMessageObject {
    private String error;

    public Rerror() {

    }

    public Rerror(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
