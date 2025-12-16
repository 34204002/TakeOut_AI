package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;

import org.apache.ibatis.annotations.Arg;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
public class AutoFillAspect {
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut(){

    }
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint) throws Exception {
       log.info("拦截执行切面方法AutoFill");
        MethodSignature methodSignature=(MethodSignature) joinPoint.getSignature();//方法签名对象
        AutoFill autoFill=methodSignature.getMethod().getAnnotation(AutoFill.class);//方法注解对象

        Object[] arg =joinPoint.getArgs();
        Object entity=arg[0];//方法收到的参数对象

        LocalDateTime now=LocalDateTime.now();
        Long id= BaseContext.getCurrentId();

        if(autoFill.value()== OperationType.INSERT){
            //为四个字段赋值
            // 添加非空判断，避免 NoSuchMethodException
            setMethodValue(entity, AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class, now);
            setMethodValue(entity, AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class, now);
            setMethodValue(entity, AutoFillConstant.SET_CREATE_USER, Long.class, id);
            setMethodValue(entity, AutoFillConstant.SET_UPDATE_USER, Long.class, id);
        } else if (autoFill.value() == OperationType.UPDATE) {
            //为两个字段赋值
            setMethodValue(entity, AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class, now);
            setMethodValue(entity, AutoFillConstant.SET_UPDATE_USER, Long.class, id);
        }
    }

    /**
     * 通过反射为对象属性赋值
     * @param entity 对象
     * @param methodName 方法名
     * @param paramType 参数类型
     * @param paramValue 参数值
     */
    private void setMethodValue(Object entity, String methodName, Class<?> paramType, Object paramValue) {
        try {
            Method method = entity.getClass().getMethod(methodName, paramType);
            method.invoke(entity, paramValue);
        } catch (NoSuchMethodException e) {
            // 方法不存在，跳过
            log.warn("Method {} not found in class {}", methodName, entity.getClass().getSimpleName());
        } catch (Exception e) {
            // 其他异常
            log.error("Error while invoking method {}: {}", methodName, e.getMessage());
        }
    }
}
