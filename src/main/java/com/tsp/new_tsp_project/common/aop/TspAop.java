package com.tsp.new_tsp_project.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.lang.reflect.Method;

@Component
@Aspect
@Slf4j
public class TspAop {

	@Pointcut("execution(* com.tsp.new_tsp_project.api.admin..*(..))")
	public void pointCut() {}

	@Around("pointCut()")
	public void tspLogAop(ProceedingJoinPoint joinPoint) {
		StopWatch stopWatch = new StopWatch();
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

		Method method = methodSignature.getMethod();

		Object[] objects = joinPoint.getArgs();

		for(Object param: objects) {
			log.info("들어온 파라미터 값 : " + param);
		}
		log.info("실행된 Method Name : " + method.getName());

		try {
			stopWatch.start();
			Object returnValue = joinPoint.proceed();
			log.info("Http Method Return Value = " + returnValue);
			stopWatch.stop();
			log.info("Around AOP 실행시간 = " + stopWatch.getTotalTimeSeconds());
		} catch (Throwable throwable) {
			log.info("Around AOP Error : Method name = " + method.getName());
		}
	}

}
