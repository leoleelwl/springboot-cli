package com.jhy.app.common.aspect;

import com.jhy.app.common.beans.ResponseBean;
import com.jhy.app.common.exceptions.AppException;
import com.jhy.app.common.exceptions.CheckException;
import com.jhy.app.common.exceptions.UnloginException;
import com.jhy.app.common.utils.MailUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 处理和包装异常
 * @author jihy
 */
@Aspect
@Component
@Order(-99)
@Slf4j
public class GlobalExceptionAspect {


    @Autowired
    MailUtil mailUtil;

    @Pointcut("execution(public com.jhy.app.common.beans.ResponseBean *(..))")
    public void controllerMethod() {
    }

    @Around("controllerMethod()")
    public Object handlerControllerMethod(ProceedingJoinPoint pjp) {
        long startTime = System.currentTimeMillis();
        ResponseBean<?> result;
        try {
            result = (ResponseBean<?>) pjp.proceed();
            log.info(pjp.getSignature() + "use time:" + (System.currentTimeMillis() - startTime));
        } catch (Throwable e) {
            result = handlerException(pjp, e);
        }
        return result;
    }


    /**
     * 全局异常处理类
     * @param pjp
     * @param e
     * @return
     */
    private ResponseBean<?> handlerException(ProceedingJoinPoint pjp, Throwable e) {
        ResponseBean<?> result = new ResponseBean();

        // 已知异常
        if (e instanceof CheckException) {
            result.setMsg(e.getLocalizedMessage());
            result.setCode(ResponseBean.FAIL);
        }
        // 自己抛出的
        else if (e instanceof UnloginException) {
            result.setMsg("Unlogin");
            result.setCode(ResponseBean.NO_LOGIN);
        }
        //shiro异常： 登陆失败，如密码错误
        else if (e instanceof IncorrectCredentialsException) {
            result.setMsg("Login failed. Try xwjie/123456");
            result.setCode(ResponseBean.FAIL);
        }
        // shiro异常：没有权限
        else if (e instanceof UnauthorizedException) {
            result.setMsg("NO PERMISSION: " + e.getMessage());
            result.setCode(ResponseBean.NO_PERMISSION);
        }
        //  shiro抛出
        else if (e instanceof AuthorizationException) {
            result.setMsg("Unlogin");
            result.setCode(ResponseBean.NO_LOGIN);
        }
        else if (e instanceof AppException) {
            result.setMsg(e.getMessage());
            result.setCode(ResponseBean.FAIL);
        }
        else {
            log.error(pjp.getSignature() + " error ", e);
            //TODO 未知的异常，应该格外注意，可以发送邮件通知等

            result.setMsg(e.toString());
            result.setCode(ResponseBean.FAIL);
            //mailUtil.send(, );
        }

        return result;
    }
}
