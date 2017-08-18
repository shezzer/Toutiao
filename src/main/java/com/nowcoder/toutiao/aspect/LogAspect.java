package com.nowcoder.toutiao.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by Sherl on 2017/7/14.
 */
@Aspect
@Component
public class LogAspect {
    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);
    @Before("execution(* com.nowcoder.toutiao.controller.IndexController.*(..))")
    public void brfore(JoinPoint joinPoint){
        StringBuffer sb = new StringBuffer();
        for(Object arg : joinPoint.getArgs()){
            sb.append("args:"+arg+"|");
        }
        logger.info("before method:"+sb.toString());
    }
    @After("execution(* com.nowcoder.toutiao.controller.IndexController.*(..))")
    public void after(JoinPoint joinPoint){
        logger.info("after method:");
    }
}
