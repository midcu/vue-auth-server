package com.midcu.authsystem.web.ro;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class PermissionRo implements BaseRo {

    @NotBlank(message = "请填写权限名称！")
    private String name;

    @NotBlank(message = "请填写权限标题！")
    private String title;

    private String description;

    @NotNull(message = "请填写父ID！")
    private Long pid;
    
    @NotNull(message = "请设置状态！")
    private Integer state;
}
