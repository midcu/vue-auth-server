package com.midcu.authsystem.web.ro;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class UserLogin {

    @NotBlank(message = "登录名不能为空！")
    private String username;

    @NotBlank(message = "密码不能为空！")
    private String password;

}
