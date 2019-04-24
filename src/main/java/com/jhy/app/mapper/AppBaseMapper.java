package com.jhy.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author jihongyuan
 * @since 2019-04-22
 */

public interface AppBaseMapper<T> extends BaseMapper<T> {

    /**
     * 自定义通用方法
     */
    Integer deleteAll();
}
