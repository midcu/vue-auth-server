package com.midcu.authsystem.web.qo;

import javax.validation.constraints.Min;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageQuery {
    
    @Min(1)
    private int page = 1;

    @Min(1)
    private int pageSize = 10;

    private Boolean paged = true;

    private String sortBy = "createTime";

}
