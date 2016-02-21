package com.udacity.gradle.builditbigger;

import android.os.AsyncTask;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;

import volnjanskij.stas.jokes.backend.myApi.MyApi;

/**
 * Created by Stanislav Volnyansky on 21.02.16.
 */
public class JokeDownloadAsyncTask extends AsyncTask<Void, Void, String> {
    Exception error;
    public static interface Callback {
        public void onJoke(String joke);
        public void onError(Exception e);
    }
    private Callback callback;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }


    @Override
    protected String doInBackground(Void... params) {
        MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                // options for running against local devappserver
                // - 10.0.2.2 is localhost's IP address in Android emulator
                // - turn off compression when running against local devappserver
                // if using genymotion it seems to have the address of 10.0.3.2 instead of 10.0.2.2
                .setRootUrl("http://10.0.2.2:8080/_ah/api/")
                .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                    @Override
                    public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                        abstractGoogleClientRequest.setDisableGZipContent(true);
                    }
                });
        MyApi apiClient = builder.build();

        try {
            return apiClient.joke().execute().getData();
        } catch (IOException e) {
            e.printStackTrace();
            error=e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        if (callback!=null){
            if(s!=null){
                callback.onJoke(s);
            }else {
                callback.onError(error);
            }
        }
    }
};
