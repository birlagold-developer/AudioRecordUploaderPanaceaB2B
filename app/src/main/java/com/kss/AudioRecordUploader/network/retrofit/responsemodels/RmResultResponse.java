package com.kss.AudioRecordUploader.network.retrofit.responsemodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RmResultResponse implements Serializable {

    @SerializedName("valid")
    @Expose
    private Boolean valid;

    @SerializedName("comment")
    @Expose
    private String comment;

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
