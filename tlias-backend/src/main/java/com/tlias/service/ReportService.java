package com.tlias.service;

import java.util.List;
import java.util.Map;

public interface ReportService {
    Map<String, Object> empJobData();

    List<Map<String, Object>> empGenderData();

    List<Map<String, Object>> studentDegreeData();

    Map<String, Object> studentCountData();

    Map<String, Object> overview();
}
