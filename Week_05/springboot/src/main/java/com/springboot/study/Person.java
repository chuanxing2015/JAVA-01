package com.springboot.study;

import com.starter.School;
import com.starter.StarterConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Component
public class Person implements BeanFactoryAware{

    @Resource
    StarterConfig config;

    private  BeanFactory beanFactory;

    @PostConstruct
    public void init(){
        System.out.println(config.getStudent().getStudentName());
        System.out.println("--------------");
        System.out.println(config.getStudent().getSudentAge());
        Object object = beanFactory.getBean("studen");
        System.out.println("--------------" + object);
        if(object instanceof School){
            School school = (School) object;
            System.out.println(school.getStudentName() +  ":" +  school.getSudentAge());
        }
    }

    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
