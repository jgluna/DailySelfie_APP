package com.github.jgluna.dailyselfie.comm;

import android.os.AsyncTask;
import android.util.Log;

import com.github.jgluna.dailyselfie.model.EffectsRequestWrapper;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class BackgroundTask extends AsyncTask<EffectsRequestWrapper, Void, String> {
    @Override
    protected String doInBackground(EffectsRequestWrapper... params) {
        RestAdapter restAdapter = new RestAdapter.Builder().setLogLevel(RestAdapter.LogLevel.FULL).setEndpoint("http://localhost:8080/").build();
        EffectsControllerInterface restService = restAdapter.create(EffectsControllerInterface.class);
        //TODO asignar imagen a parametro 1
        restService.applyEffect(null, params[0], new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                //TODO modificar la imagen enviada por la obtenida
                Log.v("BackgroundTask", "yay!!");
            }

            @Override
            public void failure(RetrofitError error) {
                //TODO toast de error
                Log.v("BackgroundTask", "buu :(");
            }
        });
        return null;
    }
//TODO deberia sobreescribir postExecute? quizas para marcar las imagenes como actualizando?
}
