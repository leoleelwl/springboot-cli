package com.jhy.app.job.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jhy.app.job.domain.Job;
import java.util.List;

public interface JobMapper extends BaseMapper<Job> {

	List<Job> queryList();
}