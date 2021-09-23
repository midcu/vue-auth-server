package com.midcu.authsystem.web.rp;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.midcu.authsystem.web.vo.PageVo;

import lombok.Data;

@Data
public class ListResponse<T> {

    private PageVo page;

    private List<T> data;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    public ListResponse(List<T> data) {
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    public ListResponse(List<T> data, PageVo pageVo) {
        this.data = data;
        this.page = pageVo;
        this.timestamp = LocalDateTime.now();
    }
}
