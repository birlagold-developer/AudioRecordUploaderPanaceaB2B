package com.kss.AudioRecordUploader.model;

public class MissedCallLog {

    private int id;
    private String mobileNumber;

    public MissedCallLog(int id, String mobileNumber) {
        this.id = id;
        this.mobileNumber = mobileNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
}
