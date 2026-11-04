package com.tlias.mapper;

import com.tlias.entity.EmpExpr;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EmpExprMapper {

    void insertBatch(@Param("exprList") List<EmpExpr> exprList);

    @Delete("delete from emp_expr where emp_id = #{empId}")
    void deleteByEmpId(Integer empId);

    void deleteByEmpIds(@Param("empIds") List<Integer> empIds);

    @org.apache.ibatis.annotations.Select("select * from emp_expr where emp_id = #{empId}")
    List<EmpExpr> getByEmpId(Integer empId);
}
