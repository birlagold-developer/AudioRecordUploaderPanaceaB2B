package com.kss.AudioRecordUploader.utils;

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


}
