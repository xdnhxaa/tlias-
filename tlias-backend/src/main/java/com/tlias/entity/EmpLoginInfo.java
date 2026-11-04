package com.tlias.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 登录成功后返回给前端的信息 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpLoginInfo {
    private Integer id;
    private String username;
    private String name;
    private String token;
}
