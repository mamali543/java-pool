package com.ader.models;

import com.ader.annotations.OrmEntity;

import com.ader.annotations.OrmColumn;
import com.ader.annotations.OrmColumnId;

@OrmEntity(table="users")
public class User {

    @OrmColumnId()
    private Long userId;

    @OrmColumn(name = "first_name", length = 50)
    private String firstName;

    @OrmColumn(name = "last_name", length = 50)
    private String lastName;

    @OrmColumn(name = "age")
    private int age;

    @OrmColumn(name = "email", length = 100)
    private String email;

    @OrmColumn(name = "is_admin")
    private boolean isAdmin;

    public User()
    {
    }

    public User(Long id, String firstName, String lastName, int age, String email)
    {
        this.userId = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.email = email;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public Long getId() {
        return userId;
    }

    public void setId(Long id) {
        this.userId = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", firstName='" + firstName + '\'' +
                ", lastName=" + lastName +
                ", age=" + age +
                ", email=" + email +
                '}';
    }
}