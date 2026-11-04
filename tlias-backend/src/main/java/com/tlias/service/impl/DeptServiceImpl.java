package com.tlias.service.impl;

import com.tlias.anno.Log;
import com.tlias.entity.Dept;
import com.tlias.exception.BusinessException;
import com.tlias.mapper.DeptMapper;
import com.tlias.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeptServiceImpl implements DeptService {

    @Autowired
    private DeptMapper deptMapper;

    @Override
    public List<Dept> list() {
        return deptMapper.list();
    }

    @Override
    public Dept getById(Integer id) {
        return deptMapper.getById(id);
    }

    @Log
    @Override
    public void save(Dept dept) {
        if (dept.getName() == null || dept.getName().isBlank()) {
            throw new BusinessException("部门名称不能为空");
        }
        dept.setCreateTime(LocalDateTime.now());
        dept.setUpdateTime(LocalDateTime.now());
        deptMapper.insert(dept);
    }

    @Log
    @Override
    public void update(Dept dept) {
        dept.setUpdateTime(LocalDateTime.now());
        deptMapper.update(dept);
    }

    @Log
    @Override
    public void delete(Integer id) {
        Integer count = deptMapper.countEmpByDeptId(id);
        if (count != null && count > 0) {
            throw new BusinessException("该部门下还有 " + count + " 名员工，不能删除");
        }
        deptMapper.deleteById(id);
    }

    @Override
    public Integer countAll() {
        return deptMapper.countAll();
    }
}
