package com.midcu.authsystem.web;

import java.util.List;

import com.midcu.authsystem.web.vo.ResponseVo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionController {

    @Value("${spring.profiles.active:prod}") String profilesActive;

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ResponseVo> handleException(Throwable e){

        if (profilesActive.equals("dev") || profilesActive.equals("test")) {
            log.error("请求发生错误：", e);
            return new ResponseEntity<ResponseVo>(new ResponseVo(e.getMessage()), HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<ResponseVo>(new ResponseVo("请求发生错误！"), HttpStatus.BAD_REQUEST);
        }


    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ResponseVo> handleException(HttpRequestMethodNotSupportedException e){

        return new ResponseEntity<ResponseVo>(new ResponseVo("请求方法不支持！"), HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ResponseVo> handleException(BindException e){

        List<ObjectError> errorList = e.getBindingResult().getAllErrors();
        StringBuilder msg = new StringBuilder();
        if (errorList != null) {
            for(ObjectError error : errorList) {
                msg.append(error.getDefaultMessage());
                msg.append(";");
            }
        }

        return new ResponseEntity<ResponseVo>(new ResponseVo(msg.toString()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ResponseVo> handleAccessDeniedException(AccessDeniedException e){

        return new ResponseEntity<ResponseVo>(new ResponseVo("没有访问权限！"), HttpStatus.UNAUTHORIZED);
    }

}
