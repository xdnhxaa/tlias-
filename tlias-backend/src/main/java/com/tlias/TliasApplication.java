package com.tlias;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * tlias 员工管理系统启动类
 */
@SpringBootApplication
@MapperScan("com.tlias.mapper")
public class TliasApplication {

    public static void main(String[] args) {
        SpringApplication.run(TliasApplication.class, args);
        System.out.println("""

                ==========================================================
                  tlias 员工管理系统 启动成功
                  后端接口: http://localhost:8080
                  前端页面: http://localhost:8080/index.html
                  默认账号: zhangsan / 123456
                ==========================================================
                """);
    }
}
