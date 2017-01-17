package com.example.bribri.myapplication.tasks;

/**
 * Created by bribri on 17/01/2017.
 */

import android.os.AsyncTask;
import android.util.Pair;

import com.example.bribri.myapplication.models.User;
import com.example.bribri.myapplication.tools.InternalSearcher;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pierrick.vinot on 12/10/16.
 */


public class SignUpAsyncTask extends AsyncTask<User, String, Boolean> {
    public interface SignUpListener{
        public void onSignUp(boolean result);
    };

    private SignUpListener Listener;
    private User user;

    protected Boolean doInBackground(User... credential) {
        int count =credential.length;

        user=credential[0];

        String loginURL = InternalSearcher.getUrl()+"/user/create";
        if(loginURL==null)
            return false;

        StringBuffer chaine = new StringBuffer("");
        try{
            URL url = new URL(loginURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

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

            StringBuilder sb = new StringBuilder();
            int HttpResult = con.getResponseCode();
            if(HttpResult==200){
                Reader reader = new InputStreamReader(con.getInputStream(), "utf-8");
                Gson gson = new Gson();
                String test =gson.toJson(reader);
                if(test.equals("success"))
                    return true;
            }
            return false;

        } catch (IOException e) {
            // writing exception to log
            e.printStackTrace();
        }catch (JSONException e) {

            // Do something to recover ... or kill the app.
        }

        return false;
    }

    protected void onPostExecute(Boolean result) {
        Listener.onSignUp(result);
    }

    public void setSignUpListener(SignUpListener LL){
        Listener = LL;
    }
}