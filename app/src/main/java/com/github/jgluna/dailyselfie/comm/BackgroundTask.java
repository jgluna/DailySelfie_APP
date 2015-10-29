package com.github.jgluna.dailyselfie.comm;

import android.app.Activity;
import android.os.AsyncTask;

import com.github.jgluna.dailyselfie.model.EffectsRequestWrapper;
import com.github.jgluna.dailyselfie.model.Selfie;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import retrofit.RestAdapter;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

public class BackgroundTask extends AsyncTask<EffectsRequestWrapper, Void, Selfie> {

    private final WeakReference<Activity> activity;

    public BackgroundTask(Activity activity) {
        this.activity = new WeakReference<>(activity);
    }

    @Override
    protected Selfie doInBackground(EffectsRequestWrapper... params) {
        EffectsRequestWrapper wrapper = params[0];
        RestAdapter restAdapter = new RestAdapter.Builder().setLogLevel(RestAdapter.LogLevel.FULL).setEndpoint("http://10.0.2.2:8080/").build();
        EffectsControllerInterface restService = restAdapter.create(EffectsControllerInterface.class);
        File photo = new File(wrapper.getSelfie().getImagePath());
        TypedFile typedImage = new TypedFile("application/octet-stream", photo);
        Response response = restService.applyEffect(typedImage, new ArrayList<>(wrapper.getEffects()));
        try {
            File from = new File(wrapper.getSelfie().getImagePath());
            File to = new File(wrapper.getSelfie().getImagePath().replace(".jpg", "_old.jpg"));
            from.renameTo(to);
            //TODO si la de arriba no funciona usar esta
            //copy(from, to);
            //from.delete();
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
    }

    private void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }


}
