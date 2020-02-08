package com.kss.AudioRecordUploader.network.retrofit;

import com.kss.AudioRecordUploader.network.retrofit.responsemodels.RmResultResponse;

import retrofit2.Call;
import retrofit2.http.POST;

public interface RFInterface {

    @POST("auth/Request")
    Call<RmResultResponse> uploadFile();

}
