package com.tlias.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.tlias.anno.Log;
import com.tlias.entity.Clazz;
import com.tlias.entity.ClazzQueryParam;
import com.tlias.entity.PageResult;
import com.tlias.exception.BusinessException;
import com.tlias.mapper.ClazzMapper;
import com.tlias.mapper.StudentMapper;
import com.tlias.service.ClazzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClazzServiceImpl implements ClazzService {

    @Autowired
    private ClazzMapper clazzMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public PageResult page(ClazzQueryParam param) {
        PageHelper.startPage(param.getPage(), param.getPageSize());
        List<Clazz> list = clazzMapper.list(param);
        Page<Clazz> p = (Page<Clazz>) list;
        return new PageResult(p.getTotal(), p.getResult());
    }

    @Override
    public List<Clazz> listAll() {
        return clazzMapper.listAll();
    }

    @Override
    public Clazz getById(Integer id) {
        return clazzMapper.getById(id);
    }

    @Log
    @Override
    public void save(Clazz clazz) {
        validate(clazz);
        clazz.setCreateTime(LocalDateTime.now());
        clazz.setUpdateTime(LocalDateTime.now());
        clazzMapper.insert(clazz);
    }

    @Log
    @Override
    public void update(Clazz clazz) {
        validate(clazz);
        clazz.setUpdateTime(LocalDateTime.now());
        clazzMapper.update(clazz);
    }

    @Log
    @Override
    public void delete(Integer id) {
        Integer count = studentMapper.countByClazzId(id);
        if (count != null && count > 0) {
            throw new BusinessException("该班级下还有 " + count + " 名学员，不能删除");
        }
        clazzMapper.deleteById(id);
    }

    @Override
    public Integer countAll() {
        return clazzMapper.countAll();
    }

    private void validate(Clazz clazz) {
        if (clazz.getBeginDate() != null && clazz.getEndDate() != null
                && clazz.getBeginDate().isAfter(clazz.getEndDate())) {
            throw new BusinessException("开课时间不能晚于结课时间");
        }
    }
}
