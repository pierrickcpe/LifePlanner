package com.example.bribri.myapplication.tasks;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.lang.reflect.Type;
import android.util.Base64;

import com.example.bribri.myapplication.models.Event;
import com.example.bribri.myapplication.models.Response;
import com.example.bribri.myapplication.models.User;
import com.example.bribri.myapplication.tools.InternalSearcher;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.UUID;

/**
 * Created by bribri on 12/01/2017.
 */

public class LoginAsyncTask extends AsyncTask<User,Void,String> {
    public interface LoginListener{
        public void onLogin(String result);
    }

    private LoginListener Listener;
    private User user;
    private Gson gson;

    protected String doInBackground(User... credentials) {

        user=credentials[0];

        try {
            String loginURL = InternalSearcher.getUrl()+"/user/auth";
            if(loginURL==null)
                return null;
            URL url_test = new URL(loginURL);
            HttpURLConnection con = (HttpURLConnection) url_test.openConnection();

            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestMethod("POST");

            JSONObject json = new JSONObject();

            json.put("login",user.username);
            json.put("pwd", user.password);

            OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
            wr.write(json.toString());
            wr.flush();

            //String basicAuth = "Basic "+ Base64.encodeToString((user.username+":"+user.password).getBytes(),Base64.NO_WRAP);
            //urlConnection.setRequestProperty("Authorization", basicAuth );
            int result = con.getResponseCode();
            if(result==200){
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
                StringBuilder sb = new StringBuilder();
                String output;
                while ((output = br.readLine()) != null) {
                    sb.append(output);
                }
                String test = sb.toString();
                if(!test.equals(null))
                    gson = new Gson();
                    Response res = gson.fromJson(test, Response.class);
                    return res.response;
            }
            return "fail";

        }catch(Exception e){
            return e.getMessage();
        }

    }

    protected void onPostExecute(String result) {
        Listener.onLogin(result);
    }

    public void setLoginListener(LoginListener LL){
        Listener = LL;
    }
}