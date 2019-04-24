package com.jhy.app.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jhy.app.system.domain.LoginLog;

public interface LoginLogService extends IService<LoginLog> {

    void saveLoginLog(LoginLog loginLog);
}
