package com.example.myapplication.Database;

public class User {
    private String name;
    private String sex;
    private String birthday;
    private int Image;
    public User(){}
    public User(String name, String sex, String birthdate, int Image) {
        this.name = name;
        this.sex = sex;
        this.birthday = birthdate;
    }

    public int getImage() {
        return Image;
    }

    public void setImage(int image) {
        Image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}
