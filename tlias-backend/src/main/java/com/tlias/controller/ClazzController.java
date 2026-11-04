package com.tlias.controller;

import com.tlias.entity.Clazz;
import com.tlias.entity.ClazzQueryParam;
import com.tlias.entity.Result;
import com.tlias.service.ClazzService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/clazzs")
public class ClazzController {

    @Autowired
    private ClazzService clazzService;

    /** 分页条件查询班级 */
    @GetMapping("/page")
    public Result page(ClazzQueryParam param) {
        return Result.success(clazzService.page(param));
    }

    /** 查询全部班级（下拉框用） */
    @GetMapping("/list")
    public Result list() {
        return Result.success(clazzService.listAll());
    }

    /** 根据 ID 查询班级 */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        return Result.success(clazzService.getById(id));
    }

    /** 新增班级 */
    @PostMapping
    public Result save(@RequestBody Clazz clazz) {
        clazzService.save(clazz);
        return Result.success();
    }

    /** 修改班级 */
    @PutMapping
    public Result update(@RequestBody Clazz clazz) {
        clazzService.update(clazz);
        return Result.success();
    }

    /** 删除班级 */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        clazzService.delete(id);
        return Result.success();
    }
}
