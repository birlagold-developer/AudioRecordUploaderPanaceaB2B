package com.kss.AudioRecordUploader.network;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.kss.AudioRecordUploader.network.retrofit.RFInterface;
import com.kss.AudioRecordUploader.network.retrofit.responsemodels.RmResultResponse;
import com.kss.AudioRecordUploader.utils.Constant;
import com.kss.AudioRecordUploader.utils.Utility;

import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WebServiceCalls {
    private static final String TAG = "WebServiceCalls";

    private static RFInterface rfInterface = Utility.getRetrofitInterface(Constant.URL);

    private WebServiceCalls() {
    }

    public static class File {
        private static final String TAG = "File";

        private File() {
        }

        public static synchronized void upload(Context context, String agentMobileNumber, String agentEmailID, String clientMobileNumber, String totalDuration, java.io.File audioFile,
                                               final NetworkOperations nwCall) {

            //nwCall.onStart(context, "");
            RequestBody agentMobileNumberRequestBody = RequestBody.create(MediaType.parse("text"), agentMobileNumber);
            RequestBody agentEmailIDRequestBody = RequestBody.create(MediaType.parse("text"), agentEmailID);
            RequestBody clientMobileNumberRequestBody = RequestBody.create(MediaType.parse("text"), clientMobileNumber);
            RequestBody totalDurationRequestBody = RequestBody.create(MediaType.parse("text"), totalDuration);
            RequestBody audioFileRequestBody = RequestBody.create(MediaType.parse("audio"), audioFile);

            MultipartBody.Part audioMultipartBodyPart = MultipartBody.Part.createFormData("audio", audioFile.getName(), audioFileRequestBody);

            rfInterface.uploadFile(agentMobileNumberRequestBody, agentEmailIDRequestBody, clientMobileNumberRequestBody,
                    totalDurationRequestBody, audioMultipartBodyPart
            ).enqueue(new Callback<RmResultResponse>() {

                @Override
                public void onResponse(@NonNull Call<RmResultResponse> call, @NonNull Response<RmResultResponse> response) {

                    //nwCall.onComplete();

                    Bundle bundle = new Bundle();
                    nwCall.onSuccess(bundle);
                }

                @Override
                public void onFailure(@NonNull Call<RmResultResponse> call, @NonNull Throwable t) {
                    Log.e(TAG, Objects.requireNonNull(t.getMessage()));

                    //nwCall.onComplete();

                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.MESSAGE, t.getMessage());
                    nwCall.onFailure(bundle);
                }

            });
        }
    }

}