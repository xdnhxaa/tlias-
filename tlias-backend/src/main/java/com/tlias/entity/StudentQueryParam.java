package com.tlias.entity;

import lombok.Data;

/** 学员分页查询参数 */
@Data
public class StudentQueryParam {
    private String name;
    private Integer degree;
    private Integer clazzId;
    private Integer page = 1;
    private Integer pageSize = 10;
}
