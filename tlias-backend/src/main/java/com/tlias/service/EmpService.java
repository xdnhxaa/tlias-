package com.tlias.service;

import com.tlias.entity.Emp;
import com.tlias.entity.EmpLoginInfo;
import com.tlias.entity.EmpQueryParam;
import com.tlias.entity.PageResult;

import java.util.List;

public interface EmpService {
    PageResult page(EmpQueryParam param);

    List<Emp> listAll();

    Emp getById(Integer id);

    void save(Emp emp);

    void update(Emp emp);

    void deleteByIds(List<Integer> ids);

    EmpLoginInfo login(Emp emp);

    Integer countAll();
}
