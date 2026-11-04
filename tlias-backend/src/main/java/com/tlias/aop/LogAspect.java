package com.tlias.aop;

import com.tlias.entity.EmpLog;
import com.tlias.mapper.EmpLogMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 操作日志切面：凡是标了 @Log 的 Service 方法，执行成功后写一条日志
 */
@Slf4j
@Aspect
@Component
public class LogAspect {

    @Autowired
    private EmpLogMapper empLogMapper;

    @Around("@annotation(com.tlias.anno.Log)")
    public Object recordLog(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long cost = System.currentTimeMillis() - start;

        try {
            String method = joinPoint.getSignature().getName();
            String clazz = joinPoint.getTarget().getClass().getSimpleName();
            String info = describe(clazz, method, joinPoint.getArgs()) + "（耗时 " + cost + " ms）";
            if (info.length() > 1900) {
                info = info.substring(0, 1900);
            }
            EmpLog empLog = new EmpLog();
            empLog.setOperateTime(LocalDateTime.now());
            empLog.setInfo(info);
            empLogMapper.insert(empLog);
            log.debug("操作日志已记录：{}", info);
        } catch (Exception e) {
            // 记日志失败不能影响正常业务
            log.warn("写操作日志失败：{}", e.getMessage());
        }
        return result;
    }

    private String describe(String clazz, String method, Object[] args) {
        String action = switch (method) {
            case "save" -> "新增";
            case "update" -> "修改";
            case "delete", "deleteByIds" -> "删除";
            case "violation" -> "违纪处理";
            default -> "操作";
        };
        String module = switch (clazz) {
            case "EmpServiceImpl" -> "员工";
            case "DeptServiceImpl" -> "部门";
            case "ClazzServiceImpl" -> "班级";
            case "StudentServiceImpl" -> "学员";
            default -> "数据";
        };
        String target = "";
        if (args != null && args.length > 0 && args[0] != null) {
            Object first = args[0];
            if (first instanceof List<?> list) {
                target = list.toString();
            } else {
                try {
                    Method getName = first.getClass().getMethod("getName");
                    Object name = getName.invoke(first);
                    if (name != null) {
                        target = String.valueOf(name);
                    }
                } catch (Exception ignored) {
                    target = String.valueOf(first);
                }
            }
        }
        return action + module + "：" + target;
    }
}
