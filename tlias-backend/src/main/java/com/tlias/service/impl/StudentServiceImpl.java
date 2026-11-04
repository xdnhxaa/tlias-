package com.tlias.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.tlias.anno.Log;
import com.tlias.entity.PageResult;
import com.tlias.entity.Student;
import com.tlias.entity.StudentQueryParam;
import com.tlias.exception.BusinessException;
import com.tlias.mapper.StudentMapper;
import com.tlias.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public PageResult page(StudentQueryParam param) {
        PageHelper.startPage(param.getPage(), param.getPageSize());
        List<Student> list = studentMapper.list(param);
        Page<Student> p = (Page<Student>) list;
        return new PageResult(p.getTotal(), p.getResult());
    }

    @Override
    public Student getById(Integer id) {
        return studentMapper.getById(id);
    }

    @Log
    @Override
    public void save(Student student) {
        if (student.getNo() == null || student.getNo().isBlank()) {
            throw new BusinessException("学号不能为空");
        }
        student.setCreateTime(LocalDateTime.now());
        student.setUpdateTime(LocalDateTime.now());
        studentMapper.insert(student);
    }

    @Log
    @Override
    public void update(Student student) {
        student.setUpdateTime(LocalDateTime.now());
        studentMapper.update(student);
    }

    @Log
    @Override
    public void deleteByIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new BusinessException("请选择要删除的学员");
        }
        studentMapper.deleteByIds(ids);
    }

    @Log
    @Override
    public void violation(Integer id, Integer score) {
        if (score == null || score <= 0) {
            throw new BusinessException("扣分必须大于 0");
        }
        studentMapper.updateViolation(id, score);
    }

    @Override
    public Integer countAll() {
        return studentMapper.countAll();
    }
}
