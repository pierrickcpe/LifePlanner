package com.example.bribri.myapplication.tasks;

import android.os.AsyncTask;
import android.util.Base64;

import com.example.bribri.myapplication.models.User;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by bribri on 12/01/2017.
 */

public class LoginAsyncTask extends AsyncTask<User,Void,Boolean> {
    public interface LoginListener{
        public void onLogin(boolean result);
    };

    private LoginListener Listener;
    private User user;

    protected Boolean doInBackground(User... credentials) {

        user=credentials[0];

        String loginURL = "https://training.loicortola.com/chat-rest/2.0/connect/";
return true;/*
        try {

            URL url = new URL(loginURL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            String basicAuth = "Basic "+ Base64.encodeToString((user.username+":"+user.password).getBytes(),Base64.NO_WRAP);
            urlConnection.setRequestProperty("Authorization", basicAuth );
            int result = urlConnection.getResponseCode();
            if(result==200)
                return true;
            return false;

        }catch(Exception e){
            String error =e.getMessage();
        }

        return null;*/
    }

    protected void onPostExecute(Boolean result) {
        Listener.onLogin(result);
    }

    public void setLoginListener(LoginListener LL){
        Listener = LL;
    }
}