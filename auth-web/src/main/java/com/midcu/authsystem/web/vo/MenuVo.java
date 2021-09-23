package com.midcu.authsystem.web.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.Value;

@Data
@Value
public class MenuVo {

    public Long id;

    public Long pid;

    public String path;

    public Boolean display;

    public String title;

    public String component;

    public String name;

    public String description;

    public String icon;

    public String layout;

    public Integer type;

    public Integer sort;

    public Boolean iframe;

    public String iframeSrc;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date createTime;

    public Integer state;
}
