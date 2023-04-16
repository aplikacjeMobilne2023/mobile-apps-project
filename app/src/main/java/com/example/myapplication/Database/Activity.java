package com.example.myapplication.Database;

public class Activity {
    private String activity;
    private String name;
    private int age;
    private String userId;
    private String text;
    private int thumbnail;
    private String date;
    private double latitude;
    private double longitude;
    private String sex;
    private double distance;
    private long daysTo;

    public Activity(){}

    public Activity(String activity, String name, int age, String userId, String text, int thumbnail, String date, double latitude, double longitude, String sex) {
        this.activity = activity;
        this.name = name;
        this.age = age;
        this.userId = userId;
        this.text = text;
        this.thumbnail = thumbnail;
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
        this.sex = sex;
    }

    public long getDaysTo() {
        return daysTo;
    }

    public void setDaysTo(long daysTo) {
        this.daysTo = daysTo;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getActivity() { return  activity; }

    public void setActivity(String activity) { this.activity = activity; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getUserId() {
        return userId;
    }

    public void setBirthday(String userId) {
        this.userId = userId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}