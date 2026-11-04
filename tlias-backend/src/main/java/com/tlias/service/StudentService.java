package com.tlias.service;

import com.tlias.entity.PageResult;
import com.tlias.entity.Student;
import com.tlias.entity.StudentQueryParam;

import java.util.List;

public interface StudentService {
    PageResult page(StudentQueryParam param);

    Student getById(Integer id);

    void save(Student student);

    void update(Student student);

    void deleteByIds(List<Integer> ids);

    void violation(Integer id, Integer score);

    Integer countAll();
}
