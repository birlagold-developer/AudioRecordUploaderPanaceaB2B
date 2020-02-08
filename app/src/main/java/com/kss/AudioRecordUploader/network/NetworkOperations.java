package com.kss.AudioRecordUploader.network;

import android.app.ProgressDialog;
import android.content.Context;

import com.kss.AudioRecordUploader.Utility;
import com.kss.AudioRecordUploader.network.callback.NetworkCallback;


public abstract class NetworkOperations implements NetworkCallback {
    private ProgressDialog progressDialog;

    public void onStart(Context context, String msg) {
        progressDialog = Utility.createProgressDialog(context);
    }

    public void onComplete() {
        progressDialog.dismiss();
    }
}
