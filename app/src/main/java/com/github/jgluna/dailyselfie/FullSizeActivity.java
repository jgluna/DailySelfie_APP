package com.github.jgluna.dailyselfie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
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
        TextView tv = (TextView) findViewById(R.id.list_selfie_date);
        Picasso.with(this).load(new File(selfie.getImagePath())).into(iv);
        tv.setText(selfie.getSelfieDate().toString());
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_full_size, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
