package com.jhy.app.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jhy.app.system.domain.Dept;

public interface DeptMapper extends BaseMapper<Dept> {

	/**
	 * 递归删除部门
	 *
	 * @param deptId deptId
	 */
	void deleteDepts(String deptId);
}