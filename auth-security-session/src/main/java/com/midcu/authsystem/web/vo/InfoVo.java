package com.midcu.authsystem.web.vo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InfoVo {

    private List<MenuVo> menus;

    private UserVo user;

    private List<String> permissions;
}
