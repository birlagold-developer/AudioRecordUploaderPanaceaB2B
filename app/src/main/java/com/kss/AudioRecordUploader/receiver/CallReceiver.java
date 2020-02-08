package com.kss.AudioRecordUploader.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.File;
import java.util.Arrays;

public class CallReceiver extends BroadcastReceiver {
    private static final String TAG = "CallReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equalsIgnoreCase("android.intent.action.PHONE_STATE")) {
            Bundle extras = intent.getExtras();
            if (extras != null) {

                String state = extras.getString(TelephonyManager.EXTRA_STATE);
//            Log.d(TAG, "onReceive: TelephonyManager.EXTRA_STATE: " + state);
//            Log.d(TAG, "onReceive: TelephonyManager.CALL_STATE_IDLE: " + TelephonyManager.CALL_STATE_IDLE);
//            Log.d(TAG, "onReceive: TelephonyManager.CALL_STATE_RINGING: " + TelephonyManager.CALL_STATE_RINGING);
//            Log.d(TAG, "onReceive: TelephonyManager.CALL_STATE_OFFHOOK: " + TelephonyManager.CALL_STATE_OFFHOOK);
//            System.out.print("<=======================>");

                if (state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_IDLE)) {
                    uploadFile();
                }
            }
        } else {
            uploadFile();
        }

    }

    private void uploadFile() {
        File[] datefolder = new File(Environment.getExternalStorageDirectory().getPath() + "/CallRecordings/").listFiles();
        Arrays.sort(datefolder, (f1, f2) -> Long.valueOf(f2.lastModified()).compareTo(f1.lastModified()));

        for (File dirFile : datefolder) {
            if (dirFile.isDirectory()) {
                for (File audioFile : dirFile.listFiles()) {
                    if (audioFile.getName().contains("+")) {
                        Log.i(TAG, "uploadFile: audioFile: " + audioFile.getAbsolutePath());

                        String clientMobileNo = getClientNumber(audioFile.getName());
                        Log.i(TAG, "uploadFile: audioFile: Client Mobile No:" + clientMobileNo);

                    }
                }
            }
        }
    }

    private String getClientNumber(String fileName) {
        return fileName.substring(fileName.lastIndexOf("_") + 1, fileName.indexOf("."));
    }

}
