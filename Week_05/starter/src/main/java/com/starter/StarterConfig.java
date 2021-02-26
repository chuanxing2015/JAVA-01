package com.starter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(Student.class)
@ConditionalOnProperty(
        prefix = "student",
        name = "open",
        havingValue = "true"

)
public class StarterConfig {

    @Autowired
    private Student student;

    @Bean(name = "studen")
    public School getStudent(){
        return new School(student);
    }
}
