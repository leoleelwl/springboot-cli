package com.jhy.app.shiro;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jhy.app.system.dao.UserMapper;
import com.jhy.app.system.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author jihy
 */
@Slf4j
public class ShiroRealm extends AuthorizingRealm {

	@Autowired
	private UserMapper userMapper;

	/**
	 * 授权
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
		SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
		// TODO 获取用户角色集
		//Set<String> roleSet = userManager.getUserRoles(username);
		//simpleAuthorizationInfo.setRoles(roleSet);
		simpleAuthorizationInfo.addRole("");
		// TODO 获取用户权限集
		//Set<String> permissionSet = userManager.getUserPermissions(username);
		//simpleAuthorizationInfo.setStringPermissions(permissionSet);
		simpleAuthorizationInfo.addStringPermission("");
		return null;
	}

	/**
	 * 登录认证
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

		SimpleAuthenticationInfo info = null;
		//将token转换成UsernamePasswordToken
		UsernamePasswordToken upToken = (UsernamePasswordToken) token;
		//从转换后的token中获取用户名
		String username = upToken.getUsername();
		log.info("开始认证用户: {} ",username);
		//查询数据库，得到用户
		User user = userMapper.selectOne(new QueryWrapper<User>().lambda().eq(User::getUsername, username));

		if (user == null) {
			return null;
		}
		//得到加密密码的盐值
		//ByteSource salt = ByteSource.Util.bytes(user.getSalt());

//        logger.info("加密密码的盐："+salt);
//        //得到盐值加密后的密码：只用于方便数据库测试，后期不会用到。
//        Object md = new SimpleHash("MD5",upToken.getPassword(),salt,1024);
//        logger.info("盐值加密后的密码："+md);

		info = new SimpleAuthenticationInfo(
				user, //用户名
				user.getPassword(), //密码
				/*salt,*/ //加密的盐值
				getName()  //realm name
		);
		return info;
	}

}
