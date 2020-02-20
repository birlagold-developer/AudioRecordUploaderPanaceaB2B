package com.kss.AudioRecordUploader.utils;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;

import java.io.File;

public class Constant {

    public static String URL = "http://chhotumaharajb2b.com/api/";

    public static String MESSAGE = "message";

    public static String AGENT_NUMBBR = "agent_number";
    public static String AGENT_EMAIL = "agent_email";


    public static File getExternalStorageDirectory() {
        return Environment.getExternalStorageDirectory();
    }

    public static File getCallRecordingDir() {
        return new File(getExternalStorageDirectory(), "Call");
    }

    public static String getClientNumber(String fileName) {
        String number = fileName.substring(fileName.lastIndexOf(" ") + 1, fileName.indexOf("_"));
        if (number.startsWith("+91")) {
            return number;
        } else if (number.length() > 10) {
            return "+91" + number.substring(number.length() - 10);
        } else {
            return "+91" + number;
        }
    }

    public static int getDurationInSecond(Context context, File audioFile) {
        Uri uri = Uri.parse(audioFile.getAbsolutePath());
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(context, uri);
        String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        mmr.release();
        int millSecond = Integer.parseInt(durationStr == null ? "0" : durationStr);
        return millSecond / 1000;
    }

}
