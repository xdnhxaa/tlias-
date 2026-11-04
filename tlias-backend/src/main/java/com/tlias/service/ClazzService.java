package com.tlias.service;

import com.tlias.entity.Clazz;
import com.tlias.entity.ClazzQueryParam;
import com.tlias.entity.PageResult;

import java.util.List;

public interface ClazzService {
    PageResult page(ClazzQueryParam param);

    List<Clazz> listAll();

    Clazz getById(Integer id);

    void save(Clazz clazz);

    void update(Clazz clazz);

    void delete(Integer id);

    Integer countAll();
}
