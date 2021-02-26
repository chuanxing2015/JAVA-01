package com.spring.study;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class School {

    @Autowired
    private Student student;

    public void printStudent(){
        System.out.println(student);
    }
}
