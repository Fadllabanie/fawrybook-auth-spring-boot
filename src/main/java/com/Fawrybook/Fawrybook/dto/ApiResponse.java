package com.Fawrybook.Fawrybook.dto;

public class ApiResponse<T> {
    private boolean status;
    private int httpCode;
    private String message;
    private T data;

    public ApiResponse(boolean status, int httpCode, String message, T data) {
        this.status = status;
        this.httpCode = httpCode;
        this.message = message;
        this.data = data;
    }

    // Getters and Setters
    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(int httpCode) {
        this.httpCode = httpCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
