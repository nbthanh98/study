package com.thanhnb.jwtauth.quartz;

import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface JobService {
    boolean scheduleOneTimeJob(String jobName, String groupKey, Class<? extends QuartzJobBean> jobClass, Date date);
    boolean scheduleCronJob(String jobName, String groupKey, Class<? extends QuartzJobBean> jobClass, Date date, String cronExpression);

    boolean updateOneTimeJob(String jobName, Date date);
    boolean updateCronJob(String jobName, Date date, String cronExpression);

    boolean unScheduleJob(String jobName);
    boolean deleteJob(String jobName);
    boolean pauseJob(String jobName, String groupKey);
    boolean resumeJob(String jobName, String groupKey);
    boolean startJobNow(String jobName);
    boolean isJobRunning(String jobName, String groupKey);
    List<Map<String, Object>> getAllJobs(String groupKey);
    boolean isJobWithNamePresent(String jobName);
    String getJobState(String jobName, String groupKey);
    boolean stopJob(String jobName);
}
