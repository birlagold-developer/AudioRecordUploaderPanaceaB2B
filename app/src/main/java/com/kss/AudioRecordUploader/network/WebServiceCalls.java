package com.kss.AudioRecordUploader.network;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.kss.AudioRecordUploader.Constant;
import com.kss.AudioRecordUploader.Utility;
import com.kss.AudioRecordUploader.network.retrofit.RFInterface;
import com.kss.AudioRecordUploader.network.retrofit.responsemodels.RmResultResponse;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WebServiceCalls {

    private static final String TAG = "WebServiceCalls";

    private static RFInterface rfInterface = Utility.getRetrofitInterface(Constant.URL);

    public static class File {

        public static void upload(Context context,
                                  final NetworkOperations nwCall) {

            nwCall.onStart(context, "");

            rfInterface.uploadFile().enqueue(new Callback<RmResultResponse>() {

                @Override
                public void onResponse(@NonNull Call<RmResultResponse> call, @NonNull Response<RmResultResponse> response) {
                    nwCall.onComplete();
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("valid", response.body().getValid());
                    bundle.putString(Constant.MESSAGE, response.body().getComment());
                    nwCall.onSuccess(bundle);
                }

                @Override
                public void onFailure(@NonNull Call<RmResultResponse> call, @NonNull Throwable t) {
                    Log.e(TAG, Objects.requireNonNull(t.getMessage()));
                    nwCall.onComplete();
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.MESSAGE, t.getMessage());
                    nwCall.onFailure(bundle);
                }

            });
        }
    }

}