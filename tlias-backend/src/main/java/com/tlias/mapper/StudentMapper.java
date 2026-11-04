package com.tlias.mapper;

import com.tlias.entity.Student;
import com.tlias.entity.StudentQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface StudentMapper {

    List<Student> list(StudentQueryParam param);

    Student getById(Integer id);

    void insert(Student student);

    void update(Student student);

    void deleteByIds(@Param("ids") List<Integer> ids);

    /** 违纪处理：次数 +1、扣分累加 */
    void updateViolation(@Param("id") Integer id, @Param("score") Integer score);

    /** 学历分布统计 */
    List<Map<String, Object>> countByDegree();

    /** 班级下的学员数量（删除班级前校验） */
    Integer countByClazzId(Integer clazzId);

    Integer countAll();
}
