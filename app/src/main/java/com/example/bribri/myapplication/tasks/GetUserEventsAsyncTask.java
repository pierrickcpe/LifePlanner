package com.example.bribri.myapplication.tasks;

import java.util.Calendar;
import android.os.AsyncTask;

import com.example.bribri.myapplication.models.Event;
import com.example.bribri.myapplication.models.ListEvents;
import com.example.bribri.myapplication.models.User;

/**
 * Created by bribri on 13/01/2017.
 */

public class GetUserEventsAsyncTask extends AsyncTask<User,Void,ListEvents> {
    public interface GetUserEventsListener{
        public void onGetUserEvents(ListEvents result);
    };

    private GetUserEventsListener Listener;
    private User user;

    protected ListEvents doInBackground(User... credentials) {

        user=credentials[0];

        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE,2);
        String loginURL = "https://training.loicortola.com/chat-rest/2.0/connect/";
        Event newEvent = new Event(1,"t","t", Calendar.getInstance().getTime(),c.getTime());
        ListEvents n = new ListEvents();
        n.add(newEvent);
        return n;/*
        try {

            URL url = new URL(loginURL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            String basicAuth = "Basic "+ Base64.encodeToString((login+":"+pwd).getBytes(),Base64.NO_WRAP);

            urlConnection.setRequestProperty("Authorization", basicAuth);
            int result = urlConnection.getResponseCode();


            Reader reader = new InputStreamReader(urlConnection.getInputStream(), "utf-8");
            Type listType = new TypeToken<List<Message>>(){}.getType();
            Gson gson = new Gson();
            String test =gson.toJson(reader);
            List<Message> list= (List<Message>)gson.fromJson(reader,listType);



            if(result==200)
                return list;


        }catch(Exception e){
            String error =e.getMessage();
        }

        return null;*/
    }

    protected void onPostExecute(ListEvents result) {
        Listener.onGetUserEvents(result);
    }

    public void setUserEventsListener(GetUserEventsListener LL){
        Listener = LL;
    }
}