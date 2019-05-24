package com.jhy.app.common.aspect;

import java.lang.reflect.Method;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.jhy.app.common.annotations.Log;
import com.jhy.app.common.properties.AppProperties;
import com.jhy.app.common.utils.HttpContextUtil;
import com.jhy.app.common.utils.IPUtil;
import com.jhy.app.shiro.JWTUtil;
import com.jhy.app.system.dao.LogMapper;
import com.jhy.app.system.domain.SysLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LogAspect {


	@Autowired
	private AppProperties appProperties;

	@Autowired
	private LogMapper sysLogMapper;

	@Pointcut("@annotation(com.jhy.app.common.annotations.Log)")
	public void pointcut() {
	}

	@Around("pointcut()")
	public void around(ProceedingJoinPoint point) {
		long beginTime = System.currentTimeMillis();
		try {
			// 执行方法
			point.proceed();
		} catch (Throwable e) {
			log.error("记录日志出错...");
		}
		// 执行时长(毫秒)
		long time = System.currentTimeMillis() - beginTime;
		// 保存日志
		saveLog(point, time);
	}

	private void saveLog(ProceedingJoinPoint joinPoint, long time) {
		if(appProperties.isOpenAopLog()){
			MethodSignature signature = (MethodSignature) joinPoint.getSignature();
			Method method = signature.getMethod();
			SysLog sysLog = new SysLog();
			Log logAnnotation = method.getAnnotation(Log.class);
			if (logAnnotation != null) {
				// 注解上的描述
				sysLog.setOperation(logAnnotation.value());
			}
			// 请求的方法名
			String className = joinPoint.getTarget().getClass().getName();
			String methodName = signature.getName();
			sysLog.setMethod(className + "." + methodName + "()");
			// 请求的方法参数值
			Object[] args = joinPoint.getArgs();
			// 请求的方法参数名称
			LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
			String[] paramNames = u.getParameterNames(method);
			if (args != null && paramNames != null) {
				String params = "";
				for (int i = 0; i < args.length; i++) {
					params += "  " + paramNames[i] + ": " + args[i];
				}
				sysLog.setParams(params);
			}
			// 获取request
			HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
			// 设置IP地址
			sysLog.setIp(IPUtil.getIpAddr(request));
			String  token = (String) SecurityUtils.getSubject().getPrincipal();
			String  username = JWTUtil.getUsername(token);
			sysLog.setUsername(username);
			sysLog.setTime(time);
			Date date = new Date();
			sysLog.setCreateTime(date);
			// 保存系统日志
			sysLogMapper.insert(sysLog);
		}
	}
}
