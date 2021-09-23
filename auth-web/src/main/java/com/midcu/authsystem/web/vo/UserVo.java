package com.midcu.authsystem.web.vo;

import java.util.ArrayList;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UserVo {

    public final Long id;

    public final String username;
    
    public final String nickname;

    public final String description;

    public final String phone;

    public final String email;

    public final Integer state;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public final Date createTime;

    private ArrayList<Long> roleIds;
}