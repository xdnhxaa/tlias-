package com.tlias.controller;

import com.tlias.entity.Dept;
import com.tlias.entity.Result;
import com.tlias.service.DeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/depts")
public class DeptController {

    @Autowired
    private DeptService deptService;

    /** 查询全部部门 */
    @GetMapping
    public Result list() {
        return Result.success(deptService.list());
    }

    /** 根据 ID 查询部门（回显） */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        return Result.success(deptService.getById(id));
    }

    /** 新增部门 */
    @PostMapping
    public Result save(@RequestBody Dept dept) {
        deptService.save(dept);
        return Result.success();
    }

    /** 修改部门 */
    @PutMapping
    public Result update(@RequestBody Dept dept) {
        deptService.update(dept);
        return Result.success();
    }

    /** 删除部门 */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        deptService.delete(id);
        return Result.success();
    }
}
