package com.tlias.mapper;

import com.tlias.entity.Emp;
import com.tlias.entity.EmpQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface EmpMapper {

    /** 条件查询（配合 PageHelper 分页） */
    List<Emp> list(EmpQueryParam param);

    /** 查询全部员工（班级下拉用） */
    List<Emp> listAll();

    Emp getById(Integer id);

    void insert(Emp emp);

    void update(Emp emp);

    void deleteByIds(@Param("ids") List<Integer> ids);

    /** 按用户名密码查询（登录用） */
    Emp getByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

    /** 各职位人数统计 */
    List<Map<String, Object>> countByJob();

    /** 各性别员工人数统计 */
    List<Map<String, Object>> countByGender();

    Integer countAll();
}
