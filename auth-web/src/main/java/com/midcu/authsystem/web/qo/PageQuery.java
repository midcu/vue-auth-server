package com.midcu.authsystem.web.qo;

import javax.validation.constraints.Min;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PageQuery {
    
    @Min(1)
    private int page = 1;

    @Min(1)
    private int pageSize = 10;

    private Boolean paged = true;

    private String sortBy = "createTime";

    public static PageQuery NonPaged () {
        return new PageQuery(0 , 0, false, null);
    }

}
