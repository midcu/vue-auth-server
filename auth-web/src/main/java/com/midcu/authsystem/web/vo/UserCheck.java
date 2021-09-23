package com.midcu.authsystem.web.vo;

import lombok.Value;

@Value
public class UserCheck {

    private Long id;

    private String username;

    private String password;
}
