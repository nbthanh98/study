package com.thanhnb.jwtauth.quartz.job;

import org.quartz.InterruptableJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.UnableToInterruptJobException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class SimpleJob extends QuartzJobBean implements InterruptableJob {
    @Override
    public void interrupt() throws UnableToInterruptJobException {

    }

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        System.out.println("[SimpleJob][executeInternal] date=" + LocalDateTime.now() + " hello world.");
    }
}
