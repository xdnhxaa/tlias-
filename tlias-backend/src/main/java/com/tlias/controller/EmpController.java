package com.tlias.controller;

import com.tlias.entity.Emp;
import com.tlias.entity.EmpQueryParam;
import com.tlias.entity.Result;
import com.tlias.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/emps")
public class EmpController {

    @Autowired
    private EmpService empService;

    /** 分页条件查询员工 */
    @GetMapping("/page")
    public Result page(EmpQueryParam param) {
        return Result.success(empService.page(param));
    }

    /** 查询全部员工（下拉框用） */
    @GetMapping("/list")
    public Result list() {
        return Result.success(empService.listAll());
    }

    /** 根据 ID 查询员工（含工作经历） */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        return Result.success(empService.getById(id));
    }

    /** 新增员工 */
    @PostMapping
    public Result save(@RequestBody Emp emp) {
        empService.save(emp);
        return Result.success();
    }

    /** 修改员工 */
    @PutMapping
    public Result update(@RequestBody Emp emp) {
        empService.update(emp);
        return Result.success();
    }

    /** 批量删除员工 */
    @DeleteMapping("/{ids}")
    public Result delete(@PathVariable List<Integer> ids) {
        empService.deleteByIds(ids);
        return Result.success();
    }
}
