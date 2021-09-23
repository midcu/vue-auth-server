package com.midcu.authsystem.web.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.Value;

@Data
@Value
public class PermissionVo {

    public Long id;

    public String name;

    public String title;

    public String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date createTime;

    public Long pid;

    public Integer state;
    
}
