package com.github.jgluna.dailyselfie.comm;

import android.os.AsyncTask;

import com.github.jgluna.dailyselfie.MainActivity;
import com.github.jgluna.dailyselfie.model.EffectsRequestWrapper;
import com.github.jgluna.dailyselfie.model.Selfie;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import retrofit.RestAdapter;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

public class BackgroundTask extends AsyncTask<EffectsRequestWrapper, Void, Selfie> {

    private final WeakReference<MainActivity> activity;

    public BackgroundTask(MainActivity activity) {
        this.activity = new WeakReference<>(activity);
    }

    @Override
    protected Selfie doInBackground(EffectsRequestWrapper... params) {
        EffectsRequestWrapper wrapper = params[0];
        RestAdapter restAdapter = new RestAdapter.Builder().setLogLevel(RestAdapter.LogLevel.FULL).setEndpoint("http://10.0.2.2:8080/").build();
        EffectsControllerInterface restService = restAdapter.create(EffectsControllerInterface.class);
        File photo = new File(wrapper.getSelfie().getImagePath());
        TypedFile typedImage = new TypedFile("application/octet-stream", photo);
        try {
            List<String> bababa = new ArrayList<>();
            bababa.addAll(wrapper.getEffects());
            for (String str : bababa) {
                System.out.println(str);
            }
            Response response = restService.applyEffect(typedImage, bababa);
            File from = new File(wrapper.getSelfie().getImagePath());
            File to = new File(wrapper.getSelfie().getImagePath().replace(".jpg", "_old.jpg"));
            from.renameTo(to);
            InputStream is = response.getBody().in();
            File file = new File(wrapper.getSelfie().getImagePath());
            if (!file.exists()) {
                file.createNewFile();
            }
            OutputStream output = new FileOutputStream(file);
            byte[] buffer = new byte[4 * 1024];
            int read;
            while ((read = is.read(buffer)) != -1) {
                output.write(buffer, 0, read);
            }
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wrapper.getSelfie();
    }

    @Override
    protected void onPostExecute(Selfie selfie) {
        super.onPostExecute(selfie);
        Picasso.with(activity.get()).invalidate(selfie.getImagePath());
    }
}
