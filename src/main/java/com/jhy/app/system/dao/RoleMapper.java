package com.jhy.app.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jhy.app.system.domain.Role;

import java.util.List;

public interface RoleMapper extends BaseMapper<Role> {
	
	List<Role> findUserRole(String userName);
	
}