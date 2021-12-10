package com.techtitudetribe.ncertkitabadmin;

import android.content.Context;

public class Subject {
    private String  name, image;


    public Subject() {

    }

    public Subject(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
