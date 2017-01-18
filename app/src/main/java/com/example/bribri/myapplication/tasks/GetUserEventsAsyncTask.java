package com.example.bribri.myapplication.tasks;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.List;

import android.os.AsyncTask;

import com.example.bribri.myapplication.models.Event;
import com.example.bribri.myapplication.models.EventData;
import com.example.bribri.myapplication.models.ListEvents;
import com.example.bribri.myapplication.models.User;
import com.example.bribri.myapplication.tools.InternalSearcher;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

/**
 * Created by bribri on 13/01/2017.
 */

public class GetUserEventsAsyncTask extends AsyncTask<User,Void,ListEvents> {
    public interface GetUserEventsListener{
        public void onGetUserEvents(ListEvents result);
    };

    private GetUserEventsListener Listener;
    private User user;
    List<EventData> ev;
    protected ListEvents doInBackground(User... credentials) {

        user=credentials[0];
        ListEvents list=new ListEvents();
        try {
            String loginURL = InternalSearcher.getUrl()+"/user/getevents";
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
            json.put("id", user.id);

            OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
            wr.write(json.toString());
            wr.flush();

            //String basicAuth = "Basic "+ Base64.encodeToString((user.username+":"+user.password).getBytes(),Base64.NO_WRAP);
            //urlConnection.setRequestProperty("Authorization", basicAuth );
            int result = con.getResponseCode();
            if(result==200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
                StringBuilder sb = new StringBuilder();
                String output;
                while ((output = br.readLine()) != null) {
                    sb.append(output);
                }
                String test = sb.toString();
                if (!test.equals(null)) {
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<EventData>>() {
                    }.getType();
                    ev = gson.fromJson(test, listType);
                    for (int i = 0; i < ev.size(); i++)
                    {
                        list.add(ev.get(i));
                    }
                    return list;
                }
            }

        }catch(Exception e){
            String error =e.getMessage();
        }
        return null;
    }

    protected void onPostExecute(ListEvents result) {
        Listener.onGetUserEvents(result);
    }

    public void setUserEventsListener(GetUserEventsListener LL){
        Listener = LL;
    }
}