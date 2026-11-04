package com.tlias.mapper;

import com.tlias.entity.Dept;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DeptMapper {

    @Select("select id, name, create_time, update_time from dept order by update_time desc")
    List<Dept> list();

    @Select("select id, name, create_time, update_time from dept where id = #{id}")
    Dept getById(Integer id);

    @Insert("insert into dept(name, create_time, update_time) values(#{name}, #{createTime}, #{updateTime})")
    void insert(Dept dept);

    @Update("update dept set name = #{name}, update_time = #{updateTime} where id = #{id}")
    void update(Dept dept);

    @Delete("delete from dept where id = #{id}")
    void deleteById(Integer id);

    /** 部门下的员工数量（删除前校验用） */
    @Select("select count(*) from emp where dept_id = #{deptId}")
    Integer countEmpByDeptId(Integer deptId);

    @Select("select count(*) from dept")
    Integer countAll();
}
