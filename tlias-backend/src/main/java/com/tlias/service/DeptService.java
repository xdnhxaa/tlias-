package com.tlias.service;

import com.tlias.entity.Dept;

import java.util.List;

public interface DeptService {
    List<Dept> list();

    Dept getById(Integer id);

    void save(Dept dept);

    void update(Dept dept);

    void delete(Integer id);

    Integer countAll();
}
