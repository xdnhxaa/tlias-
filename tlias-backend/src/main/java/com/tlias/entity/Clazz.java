package com.tlias.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/** 班级 */
@Data
public class Clazz {
    private Integer id;
    private String name;
    private String room;
    private LocalDate beginDate;
    private LocalDate endDate;
    private Integer masterId;
    /** 1 Java 2 前端 3 大数据 4 Python 5 测试 6 运维 */
    private Integer subject;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    /** 关联查询字段：班主任姓名 */
    private String masterName;
    /** 计算字段：未开班 / 在读中 / 已结课 */
    private String status;
}
