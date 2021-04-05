package com.kss.AudioRecordUploaderPan.network.retrofit.responsemodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kss.AudioRecordUploaderPan.model.Data;

public class RmUploadFileResponse extends RmResultResponse {

    @SerializedName("data")
    @Expose
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
