package com.jhy.app.shiro;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jhy.app.common.constant.AppConstant;
import com.jhy.app.common.exceptions.RedisConnectException;
import com.jhy.app.common.service.RedisService;
import com.jhy.app.common.utils.AppUtil;
import com.jhy.app.common.utils.HttpContextUtil;
import com.jhy.app.common.utils.IPUtil;
import com.jhy.app.system.dao.UserMapper;
import com.jhy.app.system.domain.User;
import com.jhy.app.system.manager.UserManager;
import com.puppycrawl.tools.checkstyle.utils.TokenUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

/**
 * @author jihy
 */
@Slf4j
public class ShiroRealm extends AuthorizingRealm {

	@Autowired
	private RedisService redisService;

	@Autowired
	private UserManager userManager;

	@Override
	public boolean supports(AuthenticationToken token) {
		return token instanceof JWTToken;
	}

	/**
	 * 授权
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection token) {
		String username = JWTUtil.getUsername(token.toString());
		SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
		// 获取用户角色集
		Set<String> roleSet = userManager.getUserRoles(username);
		simpleAuthorizationInfo.setRoles(roleSet);
		// 获取用户权限集
		Set<String> permissionSet = userManager.getUserPermissions(username);
		simpleAuthorizationInfo.setStringPermissions(permissionSet);
		return simpleAuthorizationInfo;
	}

	/**
	 * 登录认证
	 */
	@Override
	@SneakyThrows
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		String jwtToken = (String) token.getCredentials();

		HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
		String ip = IPUtil.getIpAddr(request);

		String encryptToken = AppUtil.encryptToken(jwtToken);

		String encryptTokenInRedis = redisService.get(AppConstant.TOKEN_CACHE_PREFIX + encryptToken + "." + ip);

		if(StringUtils.isBlank(encryptTokenInRedis)){
			throw new AuthenticationException("token已过期");
		}
		String username = JWTUtil.getUsername(jwtToken);

		if(StringUtils.isBlank(username)){
			throw new AuthenticationException("token校验不通过");
		}
		User user = userManager.getUser(username);
		if(user == null){
			throw new AuthenticationException("用户名或密码错误");
		}
		if(!JWTUtil.verify(jwtToken, username, user.getPassword())){
			throw new AuthenticationException("token校验不通过");
		}
		return new SimpleAuthenticationInfo(jwtToken,jwtToken,"app_shiro_realm");
	}

}
