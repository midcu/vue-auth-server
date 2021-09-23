package com.midcu.authsystem.web.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.Value;

@Data
@Value
public class RoleVo{
    
    public Long id;

    public String name;

    public String permit;

    public String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date createTime;
}
