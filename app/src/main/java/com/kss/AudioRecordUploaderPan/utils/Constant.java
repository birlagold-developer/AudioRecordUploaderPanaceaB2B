package com.kss.AudioRecordUploaderPan.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Constant {

    public static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);//2019-11-22 05:52:47

    public static String URL = "https://panaceaglobe.com/api/";

    public static String MESSAGE = "message";

    public static String AGENT_NUMBBR = "agent_number";
    public static String AGENT_EMAIL = "agent_email";

    public static final String TAG = "CallRecord";

    public static File getExternalStorageDirectory() {
        return Environment.getExternalStorageDirectory();
    }

    public static File getCallRecordingDir() {




        return new File(getExternalStorageDirectory(), "Call");
    }

    public static String getClientNumber(Context context, String fileName) {
        String fileName1 = fileName.substring(fileName.indexOf(" ", 5) + 1);
        String value = fileName1.substring(0, fileName1.indexOf("_"));
        Log.e(TAG,"ClientNumber===> "+value);
        if (value.startsWith("+") || value.equalsIgnoreCase("Conference call") || value.equalsIgnoreCase("Emergency number")) {
            return value;
        } else if (TextUtils.isDigitsOnly(value)) {
            return addPrefixIfRequire(value);
        } else {
            return addPrefixIfRequire(getContactNumber(context, value));
        }
    }

    private static String addPrefixIfRequire(String value) {
        if (value.startsWith("+")) {
            return value;
        } else if (value.length() > 10) {
            return "+91" + value.substring(value.length() - 10);
        } else {
            return "+91" + value;
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

    public static String getContactName(Context context, String phoneNumber) {
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        Cursor cursor = cr.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
        if (cursor == null) {
            return null;
        }
        String contactName = null;
        if (cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return contactName;
    }

    private static String getContactNumber(Context context, String contactName) {
        String name="";
        /*try
        {*/
        ContentResolver cr = context.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                name = ""+cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                //Log.e("Before_Exception","===>"+name);
                if (name.equalsIgnoreCase(contactName)) {
                    if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                        Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                        while (pCur.moveToNext()) {
                            String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            cur.close();
                            return phoneNo;
                        }
                        pCur.close();
                    }
                }
            }
        }
        if (cur != null) {
            cur.close();
        }
        /*}
        catch (Exception e)
        {
            //Log.e("Exception_Null","===>"+name);
            e.printStackTrace();
        }*/
        return null;
    }
}
