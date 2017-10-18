package com.maple.fastweb.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LogAspect {

	@After("execution(* com.maple.fastweb.service.impl..*.*(..)))")
	public void doAfter(JoinPoint jp) {
		System.out.println("LogAspect -- log Ending method: "
				+ jp.getTarget().getClass().getName() + "."
				+ jp.getSignature().getName());
	}


	@Around("execution(* com.maple.fastweb.service..*.*(..)))")
	public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
		long time = System.currentTimeMillis();
		Object retVal = pjp.proceed();
		time = System.currentTimeMillis() - time;
		System.out.println("LogAspect -- process time: " + time + " ms");
		return retVal;
	}

	@Before("execution(* com.maple.fastweb.service..*.*(..)))")
	public void doBefore(JoinPoint jp) {
		System.out.println("LogAspect -- log Begining method: "
				+ jp.getTarget().getClass().getName() + "."
				+ jp.getSignature().getName());
	}

	@AfterThrowing(throwing="ex"
	        , pointcut=" execution(* com.maple.fastweb.service..*.*(..))")
	public void doThrowing(JoinPoint jp, Throwable ex) {
		System.out.println("LogAspect -- method " + jp.getTarget().getClass().getName()
				+ "." + jp.getSignature().getName() + " throw exception");
		System.out.println(ex.getMessage());
	}


}
