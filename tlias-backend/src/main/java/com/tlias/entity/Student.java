package com.tlias.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/** 学员 */
@Data
public class Student {
    private Integer id;
    private String name;
    private String no;
    /** 1 男 2 女 */
    private Integer gender;
    private String phone;
    private String idCard;
    /** 1 是 0 否 */
    private Integer isCollege;
    private String address;
    /** 1 初中 2 高中 3 大专 4 本科 5 硕士 6 博士 */
    private Integer degree;
    private LocalDate graduationDate;
    private Integer clazzId;
    private Integer violationCount;
    private Integer violationScore;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    /** 关联查询字段：班级名称 */
    private String clazzName;
}
