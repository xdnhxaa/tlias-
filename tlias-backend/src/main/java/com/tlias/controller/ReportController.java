package com.tlias.controller;

import com.tlias.entity.Result;
import com.tlias.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    /** 首页看板：各模块数量统计 */
    @GetMapping("/overview")
    public Result overview() {
        return Result.success(reportService.overview());
    }

    /** 统计各职位员工人数 */
    @GetMapping("/empJobData")
    public Result empJobData() {
        return Result.success(reportService.empJobData());
    }

    /** 统计各性别员工人数 */
    @GetMapping("/empGenderData")
    public Result empGenderData() {
        return Result.success(reportService.empGenderData());
    }

    /** 统计学员学历分布 */
    @GetMapping("/studentDegreeData")
    public Result studentDegreeData() {
        return Result.success(reportService.studentDegreeData());
    }

    /** 统计各班级人数 */
    @GetMapping("/studentCountData")
    public Result studentCountData() {
        return Result.success(reportService.studentCountData());
    }
}
