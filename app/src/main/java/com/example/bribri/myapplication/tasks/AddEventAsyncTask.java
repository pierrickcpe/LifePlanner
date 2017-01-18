package com.example.bribri.myapplication.tasks;

/**
 * Created by bribri on 17/01/2017.
 */
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.lang.reflect.Type;
import android.util.Base64;

import com.example.bribri.myapplication.models.Event;
import com.example.bribri.myapplication.models.Response;
import com.example.bribri.myapplication.models.User;
import com.example.bribri.myapplication.tools.InternalSearcher;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by bribri on 12/01/2017.
 */

public class AddEventAsyncTask extends AsyncTask<Event,Void,String> {
    public interface AddEventListener{
        public void onAddEvent(String result);
    }

    private AddEventListener Listener;
    private User user;
    private Event event;
    private Gson gson;

    protected String doInBackground(Event... newEvent) {

        event=newEvent[0];

        try {
            user = InternalSearcher.getCredential();

            String loginURL = InternalSearcher.getUrl()+"/event/create";
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

            json.put("author",user.id);
            json.put("title", event.title);
            json.put("description",event.description );
            json.put("startsAt", String.valueOf(event.start.getTime()));
            json.put("endsAt",String.valueOf(event.end.getTime()));
            json.put("id", event.id);

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
                gson = new Gson();
                Response res = gson.fromJson(test, Response.class);
                return res.response;
            }
            return null;

        }catch(Exception e){
            String error =e.getMessage();
        }

        return null;
    }

    protected void onPostExecute(String result) {
        Listener.onAddEvent(result);
    }

    public void setAddEventListener(AddEventListener LL){
        Listener = LL;
    }
}
