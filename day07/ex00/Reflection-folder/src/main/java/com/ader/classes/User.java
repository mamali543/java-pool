package com.ader.classes;

import java.time.Month;

public class User {
    private final String firstName;
    private final String lastName;
    private final int age;

    public User(){
        this.firstName = "name none";
        this.lastName = "lastName none";
        this.age = 0;
    }

    public User(String firstName, String lastName, int age){
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    public int yearBirthday(){
        return 2024 - age;
    }

    public String addMonth(int mont){
        return Month.of(mont) + "/" + 2024;
    }

    @Override
    public String toString() {
        return "User[" +
                "name='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ']';
    }

}
