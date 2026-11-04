package com.tlias.service.impl;

import com.tlias.mapper.ClazzMapper;
import com.tlias.mapper.DeptMapper;
import com.tlias.mapper.EmpMapper;
import com.tlias.mapper.StudentMapper;
import com.tlias.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private EmpMapper empMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private ClazzMapper clazzMapper;

    @Autowired
    private DeptMapper deptMapper;

    @Override
    public Map<String, Object> empJobData() {
        List<Map<String, Object>> rows = empMapper.countByJob();
        List<String> jobList = new ArrayList<>();
        List<Object> dataList = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            jobList.add(String.valueOf(row.get("pos")));
            dataList.add(row.get("sum"));
        }
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("jobList", jobList);
        result.put("dataList", dataList);
        return result;
    }

    @Override
    public List<Map<String, Object>> empGenderData() {
        return empMapper.countByGender();
    }

    @Override
    public List<Map<String, Object>> studentDegreeData() {
        return studentMapper.countByDegree();
    }

    @Override
    public Map<String, Object> studentCountData() {
        List<Map<String, Object>> rows = clazzMapper.countStudentByClazz();
        List<String> clazzList = new ArrayList<>();
        List<Object> dataList = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            clazzList.add(String.valueOf(row.get("name")));
            dataList.add(row.get("value"));
        }
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("clazzList", clazzList);
        result.put("dataList", dataList);
        return result;
    }

    @Override
    public Map<String, Object> overview() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("empCount", empMapper.countAll());
        result.put("deptCount", deptMapper.countAll());
        result.put("clazzCount", clazzMapper.countAll());
        result.put("studentCount", studentMapper.countAll());
        return result;
    }
}
