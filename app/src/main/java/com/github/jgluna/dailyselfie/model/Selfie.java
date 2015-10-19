package com.github.jgluna.dailyselfie.model;

import java.io.Serializable;
import java.util.Date;

public class Selfie implements Serializable{

    private Date selfieDate;
    private String imagePath;

    public Date getSelfieDate() {
        return selfieDate;
    }

    public void setSelfieDate(Date selfieDate) {
        this.selfieDate = selfieDate;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
