package com.tlias.mapper;

import com.tlias.entity.EmpLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface EmpLogMapper {

    @Insert("insert into emp_log(operate_time, info) values(#{operateTime}, #{info})")
    void insert(EmpLog empLog);

    @Select("select id, operate_time, info from emp_log order by operate_time desc limit 20")
    List<EmpLog> listRecent();
}
