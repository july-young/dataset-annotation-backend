package org.dubhe.biz.log.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.dubhe.biz.log.enums.LogEnum;
import org.dubhe.biz.log.utils.LogUtil;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.UUID;

/**
 * @description 日志切面

 */
@Component
@Aspect
@Slf4j
public class LogAspect {

	public static final String TRACE_ID = "traceId";

	@Pointcut("execution(* org.dubhe..task..*.*(..)))")
	public void serviceAspect() {
	}

	@Pointcut("execution(* org.dubhe..rest..*.*(..))) ")
	public void restAspect() {
	}

	@Pointcut(" serviceAspect() ")
	public void aroundAspect() {
	}

	@Around("aroundAspect()")
	public Object around(JoinPoint joinPoint) throws Throwable {
		if (StringUtils.isEmpty(MDC.get(TRACE_ID))) {
			MDC.put(TRACE_ID, UUID.randomUUID().toString());
		}
		return ((ProceedingJoinPoint) joinPoint).proceed();
	}

	@Around("restAspect()")
	public Object aroundRest(JoinPoint joinPoint) throws Throwable {
		MDC.clear();
		MDC.put(TRACE_ID, UUID.randomUUID().toString());
		return combineLogInfo(joinPoint);
	}

	private Object combineLogInfo(JoinPoint joinPoint) throws Throwable {
		Object[] param = joinPoint.getArgs();
		LogUtil.info(LogEnum.LOG_ASPECT, "uri:{},input:{},==>begin", joinPoint.getSignature(), param);
		long start = System.currentTimeMillis();
		Object result = ((ProceedingJoinPoint) joinPoint).proceed();
		long end = System.currentTimeMillis();
		LogUtil.info(LogEnum.LOG_ASPECT, "uri:{},output:{},proc_time:{}ms,<==end", joinPoint.getSignature().toString(),
				result, end - start);
		return result;
	}

}
