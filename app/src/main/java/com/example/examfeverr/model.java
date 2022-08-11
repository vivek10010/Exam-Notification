package com.example.examfeverr;

import java.io.Serializable;


public class model implements Serializable {
    String NAME, title, fee, dates, age, desc, id, link1, link2, link3;



    public model() {
    }

    public model(String NAME, String title, String fee, String dates, String age, String link1, String link2, String link3) {
        this.NAME = NAME;
        this.title = title;
        this.fee = fee;
        this.dates = dates;
        this.age = age;
        this.desc = desc;
        this.id = id;
        this.link1 = link1;
        this.link2 = link2;
        this.link3 = link3;

    }

    public String getLink1() {
        return link1;
    }

    public String getLink2() {
        return link2;
    }

    public String getLink3() {
        return link3;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTitle() { return title; }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setLink1(String link1) {
        this.link1 = link1;
    }

    public void setLink2(String link2) {
        this.link2 = link2;
    }

    public void setLink3(String link3) {
        this.link3 = link3;
    }

}
