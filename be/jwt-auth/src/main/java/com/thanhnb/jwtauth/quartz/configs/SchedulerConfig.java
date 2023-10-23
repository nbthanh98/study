package com.thanhnb.jwtauth.quartz.configs;

import com.thanhnb.jwtauth.quartz.listener.JobsListener;
import com.thanhnb.jwtauth.quartz.listener.TriggerListner;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

@Configuration
@AllArgsConstructor
public class SchedulerConfig {

    private final DataSource dataSource;
    private final JobsListener jobsListener;
    private final TriggerListner triggerListner;
    private final QuartzProperties quartzProperties;
    private final ApplicationContext applicationContext;

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() throws IOException {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setOverwriteExistingJobs(true);
        factory.setDataSource(dataSource);

        Properties properties = new Properties();
        properties.putAll(quartzProperties.getProperties());
        factory.setQuartzProperties(properties);

        //Register listeners to get notification on Trigger misfire etc
        factory.setGlobalTriggerListeners(triggerListner);
        factory.setGlobalJobListeners(jobsListener);

        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        factory.setJobFactory(jobFactory);

        return factory;
    }
}
