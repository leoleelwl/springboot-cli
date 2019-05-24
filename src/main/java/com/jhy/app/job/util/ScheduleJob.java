package com.jhy.app.job.util;

import com.jhy.app.common.utils.SpringContextUtil;
import com.jhy.app.job.domain.Job;
import com.jhy.app.job.domain.JobLog;
import com.jhy.app.job.service.JobLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.apache.commons.beanutils.BeanUtils.copyProperties;

/**
 * 定时任务
 */
@Slf4j
public class ScheduleJob extends QuartzJobBean {

    private ExecutorService service = Executors.newSingleThreadExecutor();

    @Override
    protected void executeInternal(JobExecutionContext context) {
        // 获取spring bean
        JobLogService scheduleJobLogService = SpringContextUtil.getBean(JobLogService.class);
        Job scheduleJob = null;
        JobLog jobLog =null;
        long startTime = 0;
        try {
            Object o = context.getMergedJobDataMap().get(Job.JOB_PARAM_KEY);
            scheduleJob= new Job();
            copyProperties(scheduleJob, o);
            jobLog = new JobLog();
            jobLog.setJobId(scheduleJob.getJobId());
            jobLog.setBeanName(scheduleJob.getBeanName());
            jobLog.setMethodName(scheduleJob.getMethodName());
            jobLog.setParams(scheduleJob.getParams());
            jobLog.setCreateTime(new Date());

            startTime = System.currentTimeMillis();
            // 执行任务
            log.info("任务准备执行，任务ID：{}", scheduleJob.getJobId());
            ScheduleRunnable task = new ScheduleRunnable(scheduleJob.getBeanName(), scheduleJob.getMethodName(),
                    scheduleJob.getParams());
            Future<?> future = service.submit(task);
            future.get();
            long times = System.currentTimeMillis() - startTime;
            jobLog.setTimes(times);
            // 任务状态 0：成功 1：失败
            jobLog.setStatus(JobLog.JOB_SUCCESS);

            log.info("任务执行完毕，任务ID：{} 总共耗时：{} 毫秒", scheduleJob.getJobId(), times);
        } catch (Exception e) {
            log.error("任务执行失败，任务ID：" + scheduleJob.getJobId(), e);
            long times = System.currentTimeMillis() - startTime;
            jobLog.setTimes(times);
            // 任务状态 0：成功 1：失败
            jobLog.setStatus(JobLog.JOB_FAIL);
            jobLog.setError(StringUtils.substring(e.toString(), 0, 2000));
        } finally {
            scheduleJobLogService.saveJobLog(jobLog);
        }
    }
}
