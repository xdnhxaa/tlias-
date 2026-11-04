package com.tlias.controller;

import com.tlias.entity.Emp;
import com.tlias.entity.EmpLoginInfo;
import com.tlias.entity.Result;
import com.tlias.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class LoginController {

    @Autowired
    private EmpService empService;

    /** 员工登录 */
    @PostMapping("/login")
    public Result login(@RequestBody Emp emp) {
        log.info("员工登录：{}", emp.getUsername());
        EmpLoginInfo loginInfo = empService.login(emp);
        if (loginInfo == null) {
            return Result.error("用户名或密码错误");
        }
        return Result.success(loginInfo);
    }
}
