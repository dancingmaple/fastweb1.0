package com.maple.fastweb.po;

import com.maple.fastweb.base.po.Table;
import com.maple.fastweb.util.validator.CannotContainSpaces;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * Created by zsj on 2017/9/26.
 */
public class User extends Table {
    private long id;
    @Range(min = 10)
    private int age;
    @NotBlank(message = "user name cannot be empty")
    @Length(min = 5,max = 20)
    private String name;
    @Email
    private String email;
    @CannotContainSpaces(message = "请不要包含空格")
    private String test;

    public User() {
    }

    public User(long id, @Range(min = 10) int age, @NotBlank(message = "user name cannot be empty") @Length(min = 5, max = 20) String name, @Email String email, String test) {
        this.id = id;
        this.age = age;
        this.name = name;
        this.email = email;
        this.test = test;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
