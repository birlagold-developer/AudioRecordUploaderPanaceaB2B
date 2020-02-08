package com.kss.AudioRecordUploader;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class UploaderActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "UploaderActivity";

    private EditText editTextAgentMobileNumber;
    private EditText editTextAgentEmailAddress;
    private Button btnUploader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploader);

        editTextAgentMobileNumber = findViewById(R.id.tVAgentNumber);
        editTextAgentEmailAddress = findViewById(R.id.tVAgentEmail);
        btnUploader = findViewById(R.id.btnUploader);

        editTextAgentMobileNumber.setInputType(InputType.TYPE_NULL);
        editTextAgentEmailAddress.setInputType(InputType.TYPE_NULL);

        editTextAgentMobileNumber.setText(SharedPrefrenceObj.getSharedValue(UploaderActivity.this, Constant.AGENT_NUMBBR));
        editTextAgentEmailAddress.setText(SharedPrefrenceObj.getSharedValue(UploaderActivity.this, Constant.AGENT_EMAIL));

        btnUploader.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnUploader:
                sendBroadcast(
                        new Intent(UploaderActivity.this, CallReceiver.class)
                                .setAction("MANUAL_UPLOAD")
                );
                break;
        }
    }

}
