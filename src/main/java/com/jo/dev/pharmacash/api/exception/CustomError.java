package com.jo.dev.pharmacash.api.exception;

import lombok.Data;

@Data
public class CustomError {
    private int code;
    private String message;
    private String detail;
    private String path;
}
