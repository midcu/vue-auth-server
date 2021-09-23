package com.midcu.authsystem.dto;

import lombok.Value;

@Value
public class UserDto {

    private Long id;

    private String username;
    private String password;
    private Integer state;
    
}
