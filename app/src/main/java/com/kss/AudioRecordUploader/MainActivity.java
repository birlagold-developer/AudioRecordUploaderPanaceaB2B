package com.kss.AudioRecordUploader;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kss.AudioRecordUploader.utils.Constant;
import com.kss.AudioRecordUploader.utils.SharedPrefrenceObj;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextAgentMobileNumber;
    private EditText editTextAgentEmailAddress;
    private Button buttonSubmit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!TextUtils.isEmpty(SharedPrefrenceObj.getSharedValue(MainActivity.this, Constant.AGENT_NUMBBR))) {
            callNextActivity();
            return;
        }

        editTextAgentMobileNumber = findViewById(R.id.tVAgentNumber);
        editTextAgentEmailAddress = findViewById(R.id.tVAgentEmail);
        buttonSubmit = findViewById(R.id.btnSubmit);

        buttonSubmit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSubmit:
                String number = editTextAgentMobileNumber.getText().toString().replace("'", "");
                String email = editTextAgentEmailAddress.getText().toString().replace("'", "");

                if (TextUtils.isEmpty(number) || TextUtils.isEmpty(email)) {
                    Toast.makeText(MainActivity.this, "Please enter correct agent mobile no and email", Toast.LENGTH_LONG).show();
                } else {
                    SharedPrefrenceObj.setSharedValue(MainActivity.this, Constant.AGENT_NUMBBR, number);
                    SharedPrefrenceObj.setSharedValue(MainActivity.this, Constant.AGENT_EMAIL, email);
                    callNextActivity();
                }

                break;
        }
    }

    private void callNextActivity() {
        Intent intent = new Intent(MainActivity.this, UploaderActivity.class);
        startActivity(intent);
        finish();
    }

}
