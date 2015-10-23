package com.github.jgluna.dailyselfie.comm;

import android.os.AsyncTask;

import com.github.jgluna.dailyselfie.model.EffectsRequestWrapper;

public class BackgroundTask extends AsyncTask<EffectsRequestWrapper,Void,String> {
    @Override
    protected String doInBackground(EffectsRequestWrapper... params) {
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
