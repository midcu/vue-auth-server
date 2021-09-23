package com.midcu.authsystem.web.qo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuQuery extends PageQuery{

    public String pid;

    private Boolean paged = false;

    private String sortBy = "sort";
}
