package com.midcu.authsystem.exception;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class ResponseException {
    private String message;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    public ResponseException(String message) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}
