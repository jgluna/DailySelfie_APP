package com.github.jgluna.dailyselfie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jgluna.dailyselfie.model.Selfie;
import com.squareup.picasso.Picasso;

import java.io.File;

public class FullSizeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selfie_item);
        Intent intent = getIntent();
        Selfie selfie = (Selfie) intent.getSerializableExtra("data");
        ImageView iv = (ImageView) findViewById(R.id.list_selfie_image);
        final float scale = this.getResources().getDisplayMetrics().density;
        int pixels = (int) (350 * scale + 0.5f);
        iv.setLayoutParams(new LinearLayout.LayoutParams(pixels, pixels));
        TextView tv = (TextView) findViewById(R.id.list_selfie_date);
        Picasso.with(this)
                .load(new File(selfie.getImagePath()))
                .resize(pixels, pixels)
                .onlyScaleDown()
                .centerCrop()
                .into(iv);
        tv.setText(selfie.getSelfieDate().toString());
    }

}
