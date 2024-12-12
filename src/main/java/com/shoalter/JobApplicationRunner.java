package com.shoalter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JobApplicationRunner implements ApplicationRunner {

    private final ConfigurableApplicationContext configurableApplicationContext;

    private final ApplicationContext applicationContext;

    private final static String JOB = "JOB";

    public JobApplicationRunner(ConfigurableApplicationContext configurableApplicationContext,
                                ApplicationContext applicationContext) {
        this.configurableApplicationContext = configurableApplicationContext;
        this.applicationContext = applicationContext;
    }

    @Override
    public void run(ApplicationArguments args) {
        var jobValues = args.getOptionValues(JOB);

        if (jobValues != null && !jobValues.isEmpty()) {
            String jobName = jobValues.getFirst();

            try {
                log.info("Execute job {} start.", jobName);
                CommonJobAction bean = (CommonJobAction) applicationContext.getBean(jobName);
                bean.run(paramsMapper(args));
                log.info("Execute job {} end.", jobName);
            } catch (NoSuchElementException e) {
                log.error("NoSuchBeanDefinitionException, name: {}", jobName, e);
            } catch (Exception e) {
                log.error("Execute job failed", e);
            } finally {
                configurableApplicationContext.close();
            }
        }
    }

    private Map<String, String> paramsMapper(ApplicationArguments args) {
        return args.getOptionNames()
                .stream()
                .filter(key -> args.getOptionValues(key) != null && !args.getOptionValues(key).isEmpty())
                .collect(Collectors.toMap(key -> key, key -> args.getOptionValues(key).getFirst()));
    }
}
