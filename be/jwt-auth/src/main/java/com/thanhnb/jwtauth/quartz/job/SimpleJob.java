package com.thanhnb.jwtauth.quartz.job;

import org.quartz.InterruptableJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.UnableToInterruptJobException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author thanhnb
 */
public class SimpleJob extends QuartzJobBean implements InterruptableJob {
    @Override
    public void interrupt() throws UnableToInterruptJobException {

    }

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        String jobKey = context.getJobDetail().getKey().getName();
        System.out.println("[SimpleJob][executeInternal] start job=" + jobKey + " date=" + LocalDateTime.now() + " hello world.");
    }
}
