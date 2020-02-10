package com.kss.AudioRecordUploader.network.retrofit;

import com.kss.AudioRecordUploader.network.retrofit.responsemodels.RmResultResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RFInterface {
    @Multipart
    @POST("Save_CallRecordAgent")
    Call<RmResultResponse> uploadFile(@Part MultipartBody.Part audioFile);

}
