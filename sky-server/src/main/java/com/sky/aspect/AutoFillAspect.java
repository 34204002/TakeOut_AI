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
            entity.getClass().getMethod(AutoFillConstant.SET_CREATE_TIME,LocalDateTime.class).invoke(entity,now);
            entity.getClass().getMethod(AutoFillConstant.SET_UPDATE_TIME,LocalDateTime.class).invoke(entity,now);
            entity.getClass().getMethod(AutoFillConstant.SET_CREATE_USER,Long.class).invoke(entity,id);
            entity.getClass().getMethod(AutoFillConstant.SET_UPDATE_USER,Long.class).invoke(entity,id);
        } else if (autoFill.value() == OperationType.UPDATE) {
            //为两个字段赋值
            entity.getClass().getMethod(AutoFillConstant.SET_UPDATE_TIME,LocalDateTime.class).invoke(entity,now);
            entity.getClass().getMethod(AutoFillConstant.SET_UPDATE_USER,Long.class).invoke(entity,id);
        }
    }
}
