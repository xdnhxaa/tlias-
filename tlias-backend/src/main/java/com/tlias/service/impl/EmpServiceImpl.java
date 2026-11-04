package com.tlias.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.tlias.anno.Log;
import com.tlias.entity.*;
import com.tlias.exception.BusinessException;
import com.tlias.mapper.EmpExprMapper;
import com.tlias.mapper.EmpMapper;
import com.tlias.service.EmpService;
import com.tlias.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class EmpServiceImpl implements EmpService {

    @Autowired
    private EmpMapper empMapper;

    @Autowired
    private EmpExprMapper empExprMapper;

    @Override
    public PageResult page(EmpQueryParam param) {
        PageHelper.startPage(param.getPage(), param.getPageSize());
        List<Emp> list = empMapper.list(param);
        Page<Emp> p = (Page<Emp>) list;
        return new PageResult(p.getTotal(), p.getResult());
    }

    @Override
    public List<Emp> listAll() {
        return empMapper.listAll();
    }

    @Override
    public Emp getById(Integer id) {
        return empMapper.getById(id);
    }

    @Log
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(Emp emp) {
        if (emp.getUsername() == null || emp.getUsername().isBlank()) {
            throw new BusinessException("用户名不能为空");
        }
        if (emp.getPassword() == null || emp.getPassword().isBlank()) {
            emp.setPassword("123456");
        }
        emp.setCreateTime(LocalDateTime.now());
        emp.setUpdateTime(LocalDateTime.now());
        empMapper.insert(emp);

        List<EmpExpr> exprList = emp.getExprList();
        if (exprList != null && !exprList.isEmpty()) {
            exprList.forEach(e -> e.setEmpId(emp.getId()));
            empExprMapper.insertBatch(exprList);
        }
    }

    @Log
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(Emp emp) {
        emp.setUpdateTime(LocalDateTime.now());
        empMapper.update(emp);

        // 工作经历：先删后插，保证与前端提交的一致
        empExprMapper.deleteByEmpId(emp.getId());
        List<EmpExpr> exprList = emp.getExprList();
        if (exprList != null && !exprList.isEmpty()) {
            exprList.forEach(e -> e.setEmpId(emp.getId()));
            empExprMapper.insertBatch(exprList);
        }
    }

    @Log
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteByIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new BusinessException("请选择要删除的员工");
        }
        empMapper.deleteByIds(ids);
        empExprMapper.deleteByEmpIds(ids);
    }

    @Override
    public EmpLoginInfo login(Emp emp) {
        Emp empDB = empMapper.getByUsernameAndPassword(emp.getUsername(), emp.getPassword());
        if (empDB == null) {
            log.warn("登录失败，用户名或密码错误：{}", emp.getUsername());
            return null;
        }
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", empDB.getId());
        claims.put("username", empDB.getUsername());
        String token = JwtUtils.generateJwt(claims);
        log.info("登录成功：{}（{}）", empDB.getName(), empDB.getUsername());
        return new EmpLoginInfo(empDB.getId(), empDB.getUsername(), empDB.getName(), token);
    }

    @Override
    public Integer countAll() {
        return empMapper.countAll();
    }
}
