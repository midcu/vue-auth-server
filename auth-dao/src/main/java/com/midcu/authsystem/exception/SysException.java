package com.midcu.authsystem.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class SysException extends RuntimeException{

    private Integer status = HttpStatus.BAD_GATEWAY.value();

    public SysException(String msg){
        super(msg);
    }

    public SysException(HttpStatus status, String msg){
        super(msg);
        this.status = status.value();
    }
}
