package com.kilcote.controller._base.aspect;

import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.kilcote.common.exceptions.AdminTemplateException;
import com.kilcote.controller._base.annotation.BaseAspectSupport;
import com.kilcote.controller._base.annotation.ControllerEndpoint;
import com.kilcote.service.system.LogService;
import com.kilcote.utils.HttpUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Shun fu
 */
@Aspect
@Slf4j
@Component("ControllerEndpointAspect")
@RequiredArgsConstructor
public class ControllerEndpointAspect extends BaseAspectSupport {

    private final LogService logService;

    @Pointcut("execution(* com.kilcote.controller.system.*.*(..)) && @annotation(com.kilcote.controller._base.annotation.ControllerEndpoint)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws AdminTemplateException {
        Object result;
        Method targetMethod = resolveMethod(point);
        ControllerEndpoint annotation = targetMethod.getAnnotation(ControllerEndpoint.class);
        String operation=annotation.operation();
        long start = System.currentTimeMillis();
        try {
            result = point.proceed();
            if (StringUtils.isNotBlank(operation)) {
                String email = HttpUtil.getCurrentUsername();
                String ip = HttpUtil.getHttpServletRequestIpAddress();
                logService.addLog(point, targetMethod, ip, operation, email, start);
            }
            return result;
        } catch (Throwable throwable) {
            log.error(throwable.getMessage(), throwable);
            String exceptionMessage=annotation.exceptionMessage();
            String message = throwable.getMessage();
        	String error = exceptionMessage + "，" + message; // : exceptionMessage;
            throw new AdminTemplateException(error);
        }
    }
 
}



