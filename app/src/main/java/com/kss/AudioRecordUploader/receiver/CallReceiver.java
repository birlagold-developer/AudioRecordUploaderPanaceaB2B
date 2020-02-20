package com.kss.AudioRecordUploader.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.kss.AudioRecordUploader.model.Data;
import com.kss.AudioRecordUploader.network.retrofit.RFInterface;
import com.kss.AudioRecordUploader.network.retrofit.responsemodels.RmUploadFileResponse;
import com.kss.AudioRecordUploader.utils.Constant;
import com.kss.AudioRecordUploader.utils.Networkstate;
import com.kss.AudioRecordUploader.utils.SharedPrefrenceObj;
import com.kss.AudioRecordUploader.utils.Utility;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

public class CallReceiver extends BroadcastReceiver {
    private static final String TAG = "CallReceiver";

    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        if (Networkstate.haveNetworkConnection(context)) {
            if (Objects.requireNonNull(intent.getAction()).equalsIgnoreCase("android.intent.action.PHONE_STATE")) {
                Bundle extras = intent.getExtras();
                if (extras != null) {

                    String state = extras.getString(TelephonyManager.EXTRA_STATE);
//            Log.d(TAG, "onReceive: TelephonyManager.EXTRA_STATE: " + state);
//            Log.d(TAG, "onReceive: TelephonyManager.CALL_STATE_IDLE: " + TelephonyManager.CALL_STATE_IDLE);
//            Log.d(TAG, "onReceive: TelephonyManager.CALL_STATE_RINGING: " + TelephonyManager.CALL_STATE_RINGING);
//            Log.d(TAG, "onReceive: TelephonyManager.CALL_STATE_OFFHOOK: " + TelephonyManager.CALL_STATE_OFFHOOK);
//            System.out.print("<=======================>");

                    if (state != null && state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_IDLE)) {
                        checkFolderAndUploadFile();
                    }
                }
            } else {
                checkFolderAndUploadFile();
            }
        } else {
            Toast toast = Toast.makeText(context, "NO INTERNET CONNECTION", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

            context.sendBroadcast(
                    new Intent().setAction("MANUAL_FILE_UPLOAD_COMPLETE")
            );
        }

    }

    private void checkFolderAndUploadFile() {

        File[] datefolder = Constant.getCallRecordingDir().listFiles();

        if (datefolder != null) {
            Arrays.sort(datefolder, (f1, f2) -> Long.compare(f2.lastModified(), f1.lastModified()));
            if (datefolder.length != 0) {
                new uploadFile(datefolder).execute("");
            } else {
                Toast toast = Toast.makeText(context, "NO FILE AVAILABLE", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                context.sendBroadcast(
                        new Intent().setAction("MANUAL_FILE_UPLOAD_COMPLETE")
                );
            }
        }
    }

    class uploadFile extends AsyncTask<String, String, String> {

        private File[] datefolder = null;
        RFInterface rfInterface;

        public uploadFile(File[] datefolder) {
            this.datefolder = datefolder;
            rfInterface = Utility.getRetrofitInterface(Constant.URL);
        }

        @Override
        protected String doInBackground(String... strings) {

            for (int i = 0; i < datefolder.length; i++) {
                File audioFile = datefolder[i];
                if (audioFile.getName().contains("Call")) {

                    String agentMobileNumber = SharedPrefrenceObj.getSharedValue(context, Constant.AGENT_NUMBBR);
                    String agentEmailID = SharedPrefrenceObj.getSharedValue(context, Constant.AGENT_EMAIL);

                    String clientMobileNo = getClientNumber(audioFile.getName());
                    String audioFileExtension = audioFile.getName().substring(audioFile.getName().lastIndexOf('.') + 1);
                    int durationInSeconds = getDurationInSecond(audioFile);

                    RequestBody agentMobileNumberRequestBody = RequestBody.create(MediaType.parse("text"), agentMobileNumber);
                    RequestBody agentEmailIDRequestBody = RequestBody.create(MediaType.parse("text"), agentEmailID);
                    RequestBody clientMobileNumberRequestBody = RequestBody.create(MediaType.parse("text"), clientMobileNo);
                    RequestBody totalDurationRequestBody = RequestBody.create(MediaType.parse("text"), "" + durationInSeconds);
                    RequestBody audioFileRequestBody = RequestBody.create(MediaType.parse("audio"), audioFile);

                    RequestBody isLastRequestBody = null;
                    if (i == datefolder.length - 1) {
                        isLastRequestBody = RequestBody.create(MediaType.parse("text"), "1");
                    } else {
                        isLastRequestBody = RequestBody.create(MediaType.parse("text"), "0");
                    }

                    MultipartBody.Part audioMultipartBodyPart = MultipartBody.Part.createFormData("audio", audioFile.getName(), audioFileRequestBody);

                    upload(agentMobileNumberRequestBody, agentEmailIDRequestBody, clientMobileNumberRequestBody, totalDurationRequestBody, audioMultipartBodyPart, isLastRequestBody, i);
                }
            }

            return null;
        }

        private void upload(RequestBody agentMobileNumberRequestBody, RequestBody agentEmailIDRequestBody, RequestBody clientMobileNumberRequestBody,
                            RequestBody totalDurationRequestBody, MultipartBody.Part audioMultipartBodyPart, RequestBody isLastRequestBody, int fileCounter) {
            try {
                Response<RmUploadFileResponse> executeUploadFileResponse = rfInterface.uploadFile(
                        agentMobileNumberRequestBody, agentEmailIDRequestBody, clientMobileNumberRequestBody,
                        totalDurationRequestBody, audioMultipartBodyPart, isLastRequestBody
                ).execute();

                if (executeUploadFileResponse.isSuccessful()) {
                    if (executeUploadFileResponse.body().getSuccess()) {

                        Data data = executeUploadFileResponse.body().getData();

                        File uploadedAudioFile = new File(Constant.getCallRecordingDir(), data.getFileName().substring(data.getFileName().indexOf("/")));

                        Log.d(TAG, "onSuccess: uploadedAudioFile: exists:" + uploadedAudioFile.exists());

                        if (uploadedAudioFile.exists()) uploadedAudioFile.delete();

                        if (data.getIsLast().equalsIgnoreCase("1")) {
                            context.sendBroadcast(
                                    new Intent().setAction("MANUAL_FILE_UPLOAD_COMPLETE")
                            );
                        }

                    } else {
                        publishProgress((fileCounter + 1) + " file found error while uploading");
                    }
                } else {
                    publishProgress((fileCounter + 1) + " file found http connection fails while uploading");
                }

            } catch (Exception e) {
                System.err.println(e);
                publishProgress(e.getLocalizedMessage());
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            //TODO
            Toast toast = Toast.makeText(context, values[0], Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    private String getClientNumber(String fileName) {
        String number = fileName.substring(fileName.lastIndexOf(" ") + 1, fileName.indexOf("_"));
        if (number.startsWith("+91")) {
            return number;
        } else if (number.length() > 10) {
            return "+91" + number.substring(number.length() - 10);
        } else {
            return "+91" + number;
        }
    }

    private int getDurationInSecond(File audioFile) {
        Uri uri = Uri.parse(audioFile.getAbsolutePath());
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(context, uri);
        String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        mmr.release();
        int millSecond = Integer.parseInt(durationStr == null ? "0" : durationStr);
        return millSecond / 1000;
    }

}
