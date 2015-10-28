package com.github.jgluna.dailyselfie.comm;

import android.os.AsyncTask;

import com.github.jgluna.dailyselfie.model.EffectsRequestWrapper;
import com.github.jgluna.dailyselfie.model.Selfie;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import retrofit.RestAdapter;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

public class BackgroundTask extends AsyncTask<EffectsRequestWrapper, Void, Selfie> {
    @Override
    protected Selfie doInBackground(EffectsRequestWrapper... params) {
        EffectsRequestWrapper wrapper = params[0];
        RestAdapter restAdapter = new RestAdapter.Builder().setLogLevel(RestAdapter.LogLevel.FULL).setEndpoint("http://localhost:8080/").build();
        EffectsControllerInterface restService = restAdapter.create(EffectsControllerInterface.class);
        File photo = new File(wrapper.getSelfie().getImagePath());
        TypedFile typedImage = new TypedFile("application/octet-stream", photo);
        Response response = restService.applyEffect(typedImage, wrapper);
        try {
            InputStream is = response.getBody().in();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Selfie selfie) {
        super.onPostExecute(selfie);
    }
}
