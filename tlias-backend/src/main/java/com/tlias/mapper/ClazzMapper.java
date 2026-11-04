package com.tlias.mapper;

import com.tlias.entity.Clazz;
import com.tlias.entity.ClazzQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ClazzMapper {

    List<Clazz> list(ClazzQueryParam param);

    List<Clazz> listAll();

    Clazz getById(Integer id);

    void insert(Clazz clazz);

    void update(Clazz clazz);

    void deleteById(Integer id);

    /** 各班级人数统计（报表用） */
    List<Map<String, Object>> countStudentByClazz();

    Integer countAll();
}
