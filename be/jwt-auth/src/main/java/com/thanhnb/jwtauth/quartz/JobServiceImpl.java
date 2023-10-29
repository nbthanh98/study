package com.thanhnb.jwtauth.quartz;

import com.thanhnb.jwtauth.quartz.util.JobUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import java.util.*;

import static org.quartz.Trigger.*;

@Slf4j
@Service
@AllArgsConstructor
public class JobServiceImpl implements JobService {

        @Lazy
        private final SchedulerFactoryBean schedulerFactoryBean;
        private final ApplicationContext context;

        @Override
        public boolean scheduleOneTimeJob(String jobName, String groupKey, Class<? extends QuartzJobBean> jobClass, Date date) {
                JobDetail jobDetail = JobUtil.createJob(jobClass, false, context, jobName, groupKey);
                Trigger cronTriggerBean = JobUtil.createSingleTrigger(jobName, date, SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
                log.info("[JobService][scheduleOneTimeJob] create trigger jobName={}-groupKey={} at date={}", jobName, groupKey, date);
                try {
                        Scheduler scheduler = schedulerFactoryBean.getScheduler();
                        Date dt = scheduler.scheduleJob(jobDetail, cronTriggerBean);
                        log.info("[JobService][scheduleOneTimeJob] scheduleOneTimeJob jobName={}-groupKey={} at date={} success", jobName, groupKey, dt);
                        return true;
                } catch (SchedulerException ex) {
                        log.error("[JobService][scheduleOneTimeJob] scheduleOneTimeJob with jobName={}-groupKey={} exception={}", jobName, groupKey, ExceptionUtils.getMessage(ex));
                }
                return false;
        }

        @Override
        public boolean scheduleCronJob(String jobName, String groupKey, Class<? extends QuartzJobBean> jobClass, Date date, String cronExpression) {
                JobDetail jobDetail = JobUtil.createJob(jobClass, false, context, jobName, groupKey);
                Trigger cronTriggerBean = JobUtil.createCronTrigger(jobName, date, cronExpression, SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
                log.info("[JobService][scheduleCronJob] create trigger jobName={}-groupKey={} at date={}", jobName, groupKey, date);
                try {
                        Scheduler scheduler = schedulerFactoryBean.getScheduler();
                        Date dt = scheduler.scheduleJob(jobDetail, cronTriggerBean);
                        log.info("[JobService][scheduleCronJob] scheduleCronJob jobName={}-groupKey={} at date={} success", jobName, groupKey, dt);
                        return true;
                } catch (SchedulerException ex) {
                        log.error("[JobService][scheduleOneTimeJob] scheduleCronJob with jobName={}-groupKey={} exception={}", jobName, groupKey, ExceptionUtils.getMessage(ex));
                }
                return false;
        }

        @Override
        public boolean updateOneTimeJob(String jobName, Date date) {
                try {
                        Trigger newTrigger = JobUtil.createSingleTrigger(jobName, date, SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
                        log.info("[JobService][updateOneTimeJob] create trigger jobName={} at date={}", jobName, date);

                        Date dt = schedulerFactoryBean.getScheduler().rescheduleJob(TriggerKey.triggerKey(jobName), newTrigger);
                        log.info("[JobService][updateOneTimeJob] updateOneTimeJob jobName={} at date={} success", jobName, dt);
                        return true;
                } catch (Exception ex) {
                        log.error("[JobService][updateOneTimeJob] updateOneTimeJob with jobName={} exception={}", jobName, ExceptionUtils.getMessage(ex));
                        return false;
                }
        }

        @Override
        public boolean updateCronJob(String jobName, Date date, String cronExpression) {
                System.out.println("Request received for updating cron job.");

                String jobKey = jobName;

                System.out.println("Parameters received for updating cron job : jobKey :" + jobKey + ", date: " + date);
                try {
                        //Trigger newTrigger = JobUtil.createSingleTrigger(jobKey, date, SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_REMAINING_COUNT);
                        Trigger newTrigger = JobUtil.createCronTrigger(jobKey, date, cronExpression, SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);

                        Date dt = schedulerFactoryBean.getScheduler().rescheduleJob(TriggerKey.triggerKey(jobKey), newTrigger);
                        System.out.println("Trigger associated with jobKey :" + jobKey + " rescheduled successfully for date :" + dt);
                        return true;
                } catch (Exception e) {
                        System.out.println("SchedulerException while updating cron job with key :" + jobKey + " message :" + e.getMessage());
                        e.printStackTrace();
                        return false;
                }
        }

        @Override
        public boolean unScheduleJob(String jobName) {
                System.out.println("Request received for Unscheduleding job.");

                String jobKey = jobName;

                TriggerKey tkey = new TriggerKey(jobKey);
                System.out.println("Parameters received for unscheduling job : tkey :" + jobKey);
                try {
                        boolean status = schedulerFactoryBean.getScheduler().unscheduleJob(tkey);
                        System.out.println("Trigger associated with jobKey :" + jobKey + " unscheduled with status :" + status);
                        return status;
                } catch (SchedulerException e) {
                        System.out.println("SchedulerException while unscheduling job with key :" + jobKey + " message :" + e.getMessage());
                        e.printStackTrace();
                        return false;
                }
        }

        @Override
        public boolean deleteJob(String jobName) {
                System.out.println("Request received for deleting job.");

                String jobKey = jobName;
                String groupKey = "SampleGroup";

                JobKey jkey = new JobKey(jobKey, groupKey);
                System.out.println("Parameters received for deleting job : jobKey :" + jobKey);

                try {
                        boolean status = schedulerFactoryBean.getScheduler().deleteJob(jkey);
                        System.out.println("Job with jobKey :" + jobKey + " deleted with status :" + status);
                        return status;
                } catch (SchedulerException e) {
                        System.out.println("SchedulerException while deleting job with key :" + jobKey + " message :" + e.getMessage());
                        e.printStackTrace();
                        return false;
                }
        }

        @Override
        public boolean pauseJob(String jobName, String groupKey) {
                System.out.println("Request received for pausing job.");
                JobKey jkey = new JobKey(jobName, groupKey);
                System.out.println("Parameters received for pausing job : jobKey :" + jobName + ", groupKey :" + groupKey);

                try {
                        schedulerFactoryBean.getScheduler().pauseJob(jkey);
                        System.out.println("Job with jobKey :" + jobName + " paused succesfully.");
                        return true;
                } catch (SchedulerException e) {
                        System.out.println("SchedulerException while pausing job with key :" + jobName + " message :" + e.getMessage());
                        e.printStackTrace();
                        return false;
                }
        }

        @Override
        public boolean resumeJob(String jobName, String groupKey) {
                System.out.println("Request received for resuming job.");
                JobKey jKey = new JobKey(jobName, groupKey);
                System.out.println("Parameters received for resuming job : jobKey :" + jobName);
                try {
                        schedulerFactoryBean.getScheduler().resumeJob(jKey);
                        System.out.println("Job with jobKey :" + jobName + " resumed succesfully.");
                        return true;
                } catch (SchedulerException e) {
                        System.out.println("SchedulerException while resuming job with key :" + jobName + " message :" + e.getMessage());
                        e.printStackTrace();
                        return false;
                }
        }

        /**
         * Start a job now
         */
        @Override
        public boolean startJobNow(String jobName) {
                System.out.println("Request received for starting job now.");

                String jobKey = jobName;
                String groupKey = "SampleGroup";

                JobKey jKey = new JobKey(jobKey, groupKey);
                System.out.println("Parameters received for starting job now : jobKey :" + jobKey);
                try {
                        schedulerFactoryBean.getScheduler().triggerJob(jKey);
                        System.out.println("Job with jobKey :" + jobKey + " started now succesfully.");
                        return true;
                } catch (SchedulerException e) {
                        System.out.println("SchedulerException while starting job now with key :" + jobKey + " message :" + e.getMessage());
                        e.printStackTrace();
                        return false;
                }
        }

        /**
         * Check if job is already running
         */
        @Override
        public boolean isJobRunning(String jobName, String groupKey) {
                System.out.println("Request received to check if job is running");
                System.out.println("Parameters received for checking job is running now : jobKey :" + jobName);
                try {

                        List<JobExecutionContext> currentJobs = schedulerFactoryBean.getScheduler().getCurrentlyExecutingJobs();
                        if (currentJobs != null) {
                                for (JobExecutionContext jobCtx : currentJobs) {
                                        String jobNameDB = jobCtx.getJobDetail().getKey().getName();
                                        String groupNameDB = jobCtx.getJobDetail().getKey().getGroup();
                                        if (jobName.equalsIgnoreCase(jobNameDB) && groupKey.equalsIgnoreCase(groupNameDB)) {
                                                return true;
                                        }
                                }
                        }
                } catch (SchedulerException e) {
                        System.out.println("SchedulerException while checking job with key :" + jobName + " is running. error message :" + e.getMessage());
                        e.printStackTrace();
                        return false;
                }
                return false;
        }

        /**
         * Get all jobs
         */
        @Override
        public List<Map<String, Object>> getAllJobs(String groupKey) {
                List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                try {
                        Scheduler scheduler = schedulerFactoryBean.getScheduler();

                        for (String groupName : scheduler.getJobGroupNames()) {
                                for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {

                                        String jobName = jobKey.getName();
                                        String jobGroup = jobKey.getGroup();

                                        //get job's trigger
                                        List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
                                        Date scheduleTime = triggers.get(0).getStartTime();
                                        Date nextFireTime = triggers.get(0).getNextFireTime();
                                        Date lastFiredTime = triggers.get(0).getPreviousFireTime();

                                        Map<String, Object> map = new HashMap<String, Object>();
                                        map.put("jobName", jobName);
                                        map.put("groupName", jobGroup);
                                        map.put("scheduleTime", scheduleTime);
                                        map.put("lastFiredTime", lastFiredTime);
                                        map.put("nextFireTime", nextFireTime);

                                        if (isJobRunning(jobName, groupKey)) {
                                                map.put("jobStatus", "RUNNING");
                                        } else {
                                                String jobState = getJobState(jobName, groupKey);
                                                map.put("jobStatus", jobState);
                                        }

					/*					Date currentDate = new Date();
					if (scheduleTime.compareTo(currentDate) > 0) {
						map.put("jobStatus", "scheduled");

					} else if (scheduleTime.compareTo(currentDate) < 0) {
						map.put("jobStatus", "Running");

					} else if (scheduleTime.compareTo(currentDate) == 0) {
						map.put("jobStatus", "Running");
					}*/

                                        list.add(map);
                                        System.out.println("Job details:");
                                        System.out.println("Job Name:" + jobName + ", Group Name:" + groupName + ", Schedule Time:" + scheduleTime);
                                }

                        }
                } catch (SchedulerException e) {
                        System.out.println("SchedulerException while fetching all jobs. error message :" + e.getMessage());
                        e.printStackTrace();
                }
                return list;
        }

        /**
         * Check job exist with given name
         */
        @Override
        public boolean isJobWithNamePresent(String jobName) {
                try {
                        String groupKey = "SampleGroup";
                        JobKey jobKey = new JobKey(jobName, groupKey);
                        Scheduler scheduler = schedulerFactoryBean.getScheduler();
                        if (scheduler.checkExists(jobKey)) {
                                return true;
                        }
                } catch (SchedulerException e) {
                        System.out.println("SchedulerException while checking job with name and group exist:" + e.getMessage());
                        e.printStackTrace();
                }
                return false;
        }

        /**
         * Get the current state of job
         */
        public String getJobState(String jobName, String groupKey) {
                System.out.println("JobServiceImpl.getJobState()");

                try {
                        JobKey jobKey = new JobKey(jobName, groupKey);

                        Scheduler scheduler = schedulerFactoryBean.getScheduler();
                        JobDetail jobDetail = scheduler.getJobDetail(jobKey);

                        List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobDetail.getKey());
                        if (triggers != null && !triggers.isEmpty()) {
                                for (Trigger trigger : triggers) {
                                        TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());

                                        if (TriggerState.PAUSED.equals(triggerState)) {
                                                return "PAUSED";
                                        } else if (TriggerState.BLOCKED.equals(triggerState)) {
                                                return "BLOCKED";
                                        } else if (TriggerState.COMPLETE.equals(triggerState)) {
                                                return "COMPLETE";
                                        } else if (TriggerState.ERROR.equals(triggerState)) {
                                                return "ERROR";
                                        } else if (TriggerState.NONE.equals(triggerState)) {
                                                return "NONE";
                                        } else if (TriggerState.NORMAL.equals(triggerState)) {
                                                return "SCHEDULED";
                                        }
                                }
                        }
                } catch (SchedulerException e) {
                        System.out.println("SchedulerException while checking job with name and group exist:" + e.getMessage());
                        e.printStackTrace();
                }
                return null;
        }

        /**
         * Stop a job
         */
        @Override
        public boolean stopJob(String jobName) {
                System.out.println("JobServiceImpl.stopJob()");
                try {
                        String jobKey = jobName;
                        String groupKey = "SampleGroup";

                        Scheduler scheduler = schedulerFactoryBean.getScheduler();
                        JobKey jkey = new JobKey(jobKey, groupKey);

                        return scheduler.interrupt(jkey);

                } catch (SchedulerException e) {
                        System.out.println("SchedulerException while stopping job. error message :" + e.getMessage());
                        e.printStackTrace();
                }
                return false;
        }
}
