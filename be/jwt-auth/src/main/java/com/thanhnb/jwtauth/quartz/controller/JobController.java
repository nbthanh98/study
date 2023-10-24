package com.thanhnb.jwtauth.quartz.controller;

import com.thanhnb.jwtauth.quartz.JobService;
import com.thanhnb.jwtauth.quartz.ServerResponse;
import com.thanhnb.jwtauth.quartz.job.SimpleJob;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/scheduler")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @RequestMapping("/schedule")
    public ServerResponse schedule(@RequestParam("jobName") String jobName,
                                   @RequestParam("groupKey") String groupKey,
                                   @RequestParam("jobScheduleTime") @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm") Date jobScheduleTime,
                                   @RequestParam("cronExpression") String cronExpression) {

        if(!StringUtils.hasText(jobName)) {
            return ServerResponse.make(HttpStatus.BAD_REQUEST.value(), "JobName cannot null or empty.",  false);
        }

        boolean jobExist = jobService.isJobWithNamePresent(jobName);
        if (jobExist) {
            return ServerResponse.make(HttpStatus.BAD_REQUEST.value(), "JobName exist.",  false);
        }

        if (StringUtils.hasText(cronExpression)) {
            boolean isSuccess = jobService.scheduleCronJob(jobName, groupKey, SimpleJob.class, jobScheduleTime, cronExpression);
            return isSuccess ? ServerResponse.make(HttpStatus.CREATED.value(), "scheduleCronJob success.",false)
                    : ServerResponse.make(HttpStatus.BAD_REQUEST.value(), "JobName exist.",false);
        }

        boolean isSuccess = jobService.scheduleOneTimeJob(jobName, groupKey, SimpleJob.class, jobScheduleTime);
        return isSuccess ? ServerResponse.make(HttpStatus.CREATED.value(), "scheduleCronJob success.",false)
                : ServerResponse.make(HttpStatus.BAD_REQUEST.value(), "JobName exist.",false);
    }

    @RequestMapping(value = "/get-all-job", method = RequestMethod.GET)
    public ServerResponse getAllJobs(@RequestParam("groupKey") String groupKey) {
        List<Map<String, Object>> data = jobService.getAllJobs(groupKey);
        if (Objects.isNull(data) || data.isEmpty()) {
            return ServerResponse.make(HttpStatus.OK.value(), "ok", new ArrayList<>());
        }
        return ServerResponse.make(HttpStatus.OK.value(), "ok", data);
    }
    @RequestMapping(value = "/is-job-running", method = RequestMethod.GET)
    public ServerResponse isJobRunning(@RequestParam("jobName") String jobName,
                                       @RequestParam("groupKey") String groupKey) {
        boolean status = jobService.isJobRunning(jobName, groupKey);
        return ServerResponse.make(HttpStatus.OK.value(), "ok", status);
    }

    @RequestMapping(value = "/job-state", method = RequestMethod.GET)
    public ServerResponse getJobState(@RequestParam("jobName") String jobName,
                                      @RequestParam("groupKey") String groupKey) {
        System.out.println("JobController.getJobState()");

        String jobState = jobService.getJobState(jobName, groupKey);
        return ServerResponse.make(HttpStatus.OK.value(), "ok", jobState);
    }
    @RequestMapping(value = "/pause", method = RequestMethod.GET)
    public ServerResponse pause(@RequestParam("jobName") String jobName,
                                @RequestParam("groupKey") String groupKey) {
        boolean success = jobService.pauseJob(jobName, groupKey);
        return ServerResponse.make(HttpStatus.OK.value(), "ok", success);
    }

    @RequestMapping(value = "/resume", method = RequestMethod.GET)
    public ServerResponse resume(@RequestParam("jobName") String jobName,
                                 @RequestParam("groupKey") String groupKey) {
        boolean status = jobService.resumeJob(jobName, groupKey);
        return ServerResponse.make(HttpStatus.OK.value(), "ok", status);
    }
}
