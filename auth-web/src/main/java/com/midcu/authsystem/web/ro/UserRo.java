package com.midcu.authsystem.web.ro;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class UserRo implements BaseRo{

    @NotBlank(message = "请填写登录用户名！")
    private String username;
    
    private String nickname;

    @NotBlank(message = "请填写登陆密码！")
    private String password;

    private String description;

    private String phone;

    private String email;

    private Integer state = 0;
    
}
