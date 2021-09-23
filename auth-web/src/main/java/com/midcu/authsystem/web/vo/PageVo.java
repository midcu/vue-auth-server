package com.midcu.authsystem.web.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PageVo {

    private int pageNumber = 1;

    private int pageSize = 10;

    private Long total;
}
