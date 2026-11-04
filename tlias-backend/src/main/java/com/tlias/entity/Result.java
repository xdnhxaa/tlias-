package com.tlias.entity;

import lombok.Data;

/**
 * 统一响应结果
 */
@Data
public class Result {

    /** 1 成功，0 失败 */
    private Integer code;
    private String msg;
    private Object data;

    public static Result success() {
        Result r = new Result();
        r.code = 1;
        r.msg = "success";
        return r;
    }

    public static Result success(Object data) {
        Result r = new Result();
        r.code = 1;
        r.msg = "success";
        r.data = data;
        return r;
    }

    public static Result error(String msg) {
        Result r = new Result();
        r.code = 0;
        r.msg = msg;
        return r;
    }
}
