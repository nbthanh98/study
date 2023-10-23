package com.thanhnb.jwtauth.quartz.listener;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.springframework.stereotype.Component;

@Component
public class JobsListener implements JobListener {

    @Override
    public String getName() {
        return "globalJob";
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        System.out.println("J[JobsListener][jobToBeExecuted] obsListener.jobToBeExecuted()");
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
        System.out.println("[JobsListener][jobExecutionVetoed] JobsListener.jobExecutionVetoed()");
    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        System.out.println("[JobsListener][jobWasExecuted] JobsListener.jobWasExecuted()");
    }

}
