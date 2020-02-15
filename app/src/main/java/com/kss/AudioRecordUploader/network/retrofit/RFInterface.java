package com.kss.AudioRecordUploader.network.retrofit;

import com.kss.AudioRecordUploader.network.retrofit.responsemodels.RmUploadFileResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RFInterface {

    @Multipart
    @POST("Save_CallRecordAgent")
    Call<RmUploadFileResponse> uploadFile(
            @Part("agent_number") RequestBody agentMobileNumber,
            @Part("email") RequestBody agentEmailID,
            @Part("customer_number") RequestBody clientMobileNumber,
            @Part("call_duration") RequestBody totalDuration,
            @Part MultipartBody.Part audioFile,
            @Part("isLast") RequestBody isLast
    );

}
