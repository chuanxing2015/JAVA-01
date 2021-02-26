package com.spring.study;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;


@Configuration
public class SpringBeanAssemble {

    public static void main(String[] args){

        // xml 方式装配
        ApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("bean.xml");
        Student student = (Student) classPathXmlApplicationContext.getBean("student");
        System.out.println(student);



        // java配置类显示配置
        ApplicationContext configApplicationContext = new AnnotationConfigApplicationContext(SpringBeanAssemble.class);
        Student student1 = (Student) configApplicationContext.getBean("student1");
        System.out.println(student1);

        //自动扫描以及自动装配, 在xml中定义component-scan
        School school = (School) classPathXmlApplicationContext.getBean("school");
        school.printStudent();

        Student student11 = (Student) classPathXmlApplicationContext.getBean("student1");
        System.out.println(student11);


    }



    @Bean( name = "student1")
    public Student getStudent(){
        Student student = new Student();
        student.setName("lisi");
        student.setAge(20);
        return student;
    }
}
