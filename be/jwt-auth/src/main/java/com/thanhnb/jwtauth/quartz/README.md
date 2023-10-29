
Quartz Intro

![2023-10-29_21-19.png](imgs%2F2023-10-29_21-19.png)

- Job: Tis repersents the actual business logic to be executed, such as sending SMS, email, accessing database, data synchronization, and so on.
  - JobDetail: An implementation class of Job interface, created by **JobBuilder** to encapsulate the specific definition od the task.
  - JobDataMap: An integration of a Map that stores additional information for the JobDetail using key-value pairs.
  - JobStore: Component này được sử dụng để lưu trũ các thông tin có quan hệ với tasks và triggers, như: taskName, counts, status. Quartz cung cấp hai cách để store task: in-memory(RAMJobStore) and in database (JDBCJobStore).
- Trigger: Triggers thì define khi nào? và bằng cách nào? để các job sẽ thực hiện. Triggers sẽ mô tả: conditions, execution times, internals, end times, and more. There are four major types of triggers: SimpleTriggers (simple triggers), CronTrigger (Cron expression trigger), DateInteralTrigger(date triggers), and CalendarIntervalTrigger(calendar trigger).
- Scheduler: The scheduler responsible for starting trigger to execute jobs. It manages the execution of jobs based on the specified trigger condition and time.

### Job
Job là một interface định nghĩa duy nhất một hàm `execute()`. Khi tạo một specific task classes, cần implement Job interface hoặc QuartzJobBean (vì QuartzJobBean cũng implement Job interface). Có thẻ sử dụng **JobBuilder** để đóng gói specific task class into JobDetail. which is then managed by the Scheduler. Each JobDetail has a unique identity based on its name and group.

### JobDetail
The purpose of JobDetail is to bind a Job. It represents an instance of task and allow to add various extension parameters to the job.

Main fields:
 - name: Task name.
 - group: Task group (default group is DEFAULT).
 - jobClass: Class định nghĩa logic của task (class implement QuartzJobBean or Job), giống như class SimpleJob.class
 - JobDataMap: Các tham số có thẻ truyền vào Task. Cả JobDetail và Trigger đều có thể sử dụng JobDataMap to set params or information.

Each time the Scheduler schedules and executes a Job. it first retrieves the corresoinding Job, create an instance of that Job, and then executes the business logic inside `execute()` method.

**Why is it designed as JobDetail + Job, instead of just using Job directly?** 

### JobExecutionContext
- When the Scheduler invokes a job, it passes the JobExecutionContext to execute() method in the Job.
- Jobs can access the runtime environment of Quartz and their own specific data though the JobExecutionContext object.

### Trigger (Định nghĩa Job đc chạy khi nào? và cách thức thực hiện Job?)
Có 4 loại trigger in Quartz:
- **SimpleTrigger**: Hỗ trợ việc define interval between task execution. Có thể define các rules theo 2 cách: định nghĩa số lần lặp lại (repeat count) hoặc define startTime and endTime. 
- **CronTrigger**: Hỗ trợ trigger dựa vào cronExpression.

###
https://levelup.gitconnected.com/distributed-timed-task-solution-quartz-8911f4e7d598

```sql
#
# Quartz seems to work best with the driver mm.mysql-2.0.7-bin.jar
#
# PLEASE consider using mysql with innodb tables to avoid locking issues
#
# In your Quartz properties file, you'll need to set
# org.quartz.jobStore.driverDelegateClass = org.quartz.impl.jdbcjobstore.StdJDBCDelegate
#

DROP TABLE IF EXISTS QRTZ_FIRED_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_PAUSED_TRIGGER_GRPS;
DROP TABLE IF EXISTS QRTZ_SCHEDULER_STATE;
DROP TABLE IF EXISTS QRTZ_LOCKS;
DROP TABLE IF EXISTS QRTZ_SIMPLE_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_SIMPROP_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_CRON_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_BLOB_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_JOB_DETAILS;
DROP TABLE IF EXISTS QRTZ_CALENDARS;


CREATE TABLE QRTZ_JOB_DETAILS
(
SCHED_NAME VARCHAR(120) NOT NULL,
JOB_NAME  VARCHAR(200) NOT NULL,
JOB_GROUP VARCHAR(200) NOT NULL,
DESCRIPTION VARCHAR(250) NULL,
JOB_CLASS_NAME   VARCHAR(250) NOT NULL,
IS_DURABLE VARCHAR(1) NOT NULL,
IS_NONCONCURRENT VARCHAR(1) NOT NULL,
IS_UPDATE_DATA VARCHAR(1) NOT NULL,
REQUESTS_RECOVERY VARCHAR(1) NOT NULL,
JOB_DATA BLOB NULL,
PRIMARY KEY (SCHED_NAME,JOB_NAME,JOB_GROUP)
);

CREATE TABLE QRTZ_TRIGGERS
(
SCHED_NAME VARCHAR(120) NOT NULL,
TRIGGER_NAME VARCHAR(200) NOT NULL,
TRIGGER_GROUP VARCHAR(200) NOT NULL,
JOB_NAME  VARCHAR(200) NOT NULL,
JOB_GROUP VARCHAR(200) NOT NULL,
DESCRIPTION VARCHAR(250) NULL,
NEXT_FIRE_TIME BIGINT(13) NULL,
PREV_FIRE_TIME BIGINT(13) NULL,
PRIORITY INTEGER NULL,
TRIGGER_STATE VARCHAR(16) NOT NULL,
TRIGGER_TYPE VARCHAR(8) NOT NULL,
START_TIME BIGINT(13) NOT NULL,
END_TIME BIGINT(13) NULL,
CALENDAR_NAME VARCHAR(200) NULL,
MISFIRE_INSTR SMALLINT(2) NULL,
JOB_DATA BLOB NULL,
PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
FOREIGN KEY (SCHED_NAME,JOB_NAME,JOB_GROUP)
REFERENCES QRTZ_JOB_DETAILS(SCHED_NAME,JOB_NAME,JOB_GROUP)
);

CREATE TABLE QRTZ_SIMPLE_TRIGGERS
(
SCHED_NAME VARCHAR(120) NOT NULL,
TRIGGER_NAME VARCHAR(200) NOT NULL,
TRIGGER_GROUP VARCHAR(200) NOT NULL,
REPEAT_COUNT BIGINT(7) NOT NULL,
REPEAT_INTERVAL BIGINT(12) NOT NULL,
TIMES_TRIGGERED BIGINT(10) NOT NULL,
PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
);

CREATE TABLE QRTZ_CRON_TRIGGERS
(
SCHED_NAME VARCHAR(120) NOT NULL,
TRIGGER_NAME VARCHAR(200) NOT NULL,
TRIGGER_GROUP VARCHAR(200) NOT NULL,
CRON_EXPRESSION VARCHAR(200) NOT NULL,
TIME_ZONE_ID VARCHAR(80),
PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
);

CREATE TABLE QRTZ_SIMPROP_TRIGGERS
(          
SCHED_NAME VARCHAR(120) NOT NULL,
TRIGGER_NAME VARCHAR(200) NOT NULL,
TRIGGER_GROUP VARCHAR(200) NOT NULL,
STR_PROP_1 VARCHAR(512) NULL,
STR_PROP_2 VARCHAR(512) NULL,
STR_PROP_3 VARCHAR(512) NULL,
INT_PROP_1 INT NULL,
INT_PROP_2 INT NULL,
LONG_PROP_1 BIGINT NULL,
LONG_PROP_2 BIGINT NULL,
DEC_PROP_1 NUMERIC(13,4) NULL,
DEC_PROP_2 NUMERIC(13,4) NULL,
BOOL_PROP_1 VARCHAR(1) NULL,
BOOL_PROP_2 VARCHAR(1) NULL,
PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
);

CREATE TABLE QRTZ_BLOB_TRIGGERS
(
SCHED_NAME VARCHAR(120) NOT NULL,
TRIGGER_NAME VARCHAR(200) NOT NULL,
TRIGGER_GROUP VARCHAR(200) NOT NULL,
BLOB_DATA BLOB NULL,
PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
);

CREATE TABLE QRTZ_CALENDARS
(
SCHED_NAME VARCHAR(120) NOT NULL,
CALENDAR_NAME  VARCHAR(200) NOT NULL,
CALENDAR BLOB NOT NULL,
PRIMARY KEY (SCHED_NAME,CALENDAR_NAME)
);

CREATE TABLE QRTZ_PAUSED_TRIGGER_GRPS
(
SCHED_NAME VARCHAR(120) NOT NULL,
TRIGGER_GROUP  VARCHAR(200) NOT NULL,
PRIMARY KEY (SCHED_NAME,TRIGGER_GROUP)
);

CREATE TABLE QRTZ_FIRED_TRIGGERS
(
SCHED_NAME VARCHAR(120) NOT NULL,
ENTRY_ID VARCHAR(95) NOT NULL,
TRIGGER_NAME VARCHAR(200) NOT NULL,
TRIGGER_GROUP VARCHAR(200) NOT NULL,
INSTANCE_NAME VARCHAR(200) NOT NULL,
FIRED_TIME BIGINT(13) NOT NULL,
SCHED_TIME BIGINT(13) NOT NULL,
PRIORITY INTEGER NOT NULL,
STATE VARCHAR(16) NOT NULL,
JOB_NAME VARCHAR(200) NULL,
JOB_GROUP VARCHAR(200) NULL,
IS_NONCONCURRENT VARCHAR(1) NULL,
REQUESTS_RECOVERY VARCHAR(1) NULL,
PRIMARY KEY (SCHED_NAME,ENTRY_ID)
);

CREATE TABLE QRTZ_SCHEDULER_STATE
(
SCHED_NAME VARCHAR(120) NOT NULL,
INSTANCE_NAME VARCHAR(200) NOT NULL,
LAST_CHECKIN_TIME BIGINT(13) NOT NULL,
CHECKIN_INTERVAL BIGINT(13) NOT NULL,
PRIMARY KEY (SCHED_NAME,INSTANCE_NAME)
);

CREATE TABLE QRTZ_LOCKS
(
SCHED_NAME VARCHAR(120) NOT NULL,
LOCK_NAME  VARCHAR(40) NOT NULL,
PRIMARY KEY (SCHED_NAME,LOCK_NAME)
);


commit;
```

https://github.com/javabypatel/spring-boot-quartz-demo/blob/master/src/main/resources/data.sql
https://medium.com/@manvendra.singh_58589/introduction-to-quartz-scheduling-af9b01708c24