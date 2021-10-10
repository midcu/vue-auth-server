package com.midcu.authsystem.web.ro;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class RoleRo implements BaseRo{
    @NotBlank(message = "请填写角色名称！")
    private String name;

    private String description;

    @NotNull(message = "请设置状态！")
    private Integer state;
    
}
