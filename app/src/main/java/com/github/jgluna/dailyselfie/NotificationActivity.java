package com.github.jgluna.dailyselfie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class NotificationActivity extends AppCompatActivity {

    private static final int REQUEST_TAKE_PHOTO = 1;
    private String selfiePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selfiePath = SelfieHelper.addOneSelfie(this, REQUEST_TAKE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SelfieHelper.storeSelfie(this, selfiePath);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
