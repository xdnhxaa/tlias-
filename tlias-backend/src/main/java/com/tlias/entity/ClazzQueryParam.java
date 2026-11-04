package com.tlias.entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/** 班级分页查询参数 */
@Data
public class ClazzQueryParam {
    private String name;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate begin;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate end;

    private Integer page = 1;
    private Integer pageSize = 10;
}
