package com.tlias.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/** 员工 */
@Data
public class Emp {
    private Integer id;
    private String username;
    private String password;
    private String name;
    /** 1 男 2 女 */
    private Integer gender;
    private String phone;
    /** 1 班主任 2 讲师 3 学生主管 4 教研主管 5 咨询师 */
    private Integer job;
    private Integer salary;
    private String image;
    private LocalDate entryDate;
    private Integer deptId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    /** 关联查询字段：部门名称 */
    private String deptName;
    /** 关联查询字段：工作经历 */
    private List<EmpExpr> exprList;
}
