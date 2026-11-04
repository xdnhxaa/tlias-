package com.tlias.controller;

import com.tlias.entity.Result;
import com.tlias.entity.Student;
import com.tlias.entity.StudentQueryParam;
import com.tlias.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    /** 分页条件查询学员 */
    @GetMapping("/page")
    public Result page(StudentQueryParam param) {
        return Result.success(studentService.page(param));
    }

    /** 根据 ID 查询学员 */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        return Result.success(studentService.getById(id));
    }

    /** 新增学员 */
    @PostMapping
    public Result save(@RequestBody Student student) {
        studentService.save(student);
        return Result.success();
    }

    /** 修改学员 */
    @PutMapping
    public Result update(@RequestBody Student student) {
        studentService.update(student);
        return Result.success();
    }

    /** 批量删除学员 */
    @DeleteMapping("/{ids}")
    public Result delete(@PathVariable List<Integer> ids) {
        studentService.deleteByIds(ids);
        return Result.success();
    }

    /** 违纪处理 */
    @PutMapping("/violation/{id}/{score}")
    public Result violation(@PathVariable Integer id, @PathVariable Integer score) {
        studentService.violation(id, score);
        return Result.success();
    }
}
