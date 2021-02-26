package com.starter;

public class School {


    private Student student;

    public School(Student student){
        this.student = student;
    }


    public String getStudentName(){
        return student.getName();
    }

    public int getSudentAge(){
        return student.getAge();
    }


}
