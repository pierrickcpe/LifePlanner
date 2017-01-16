package com.example.bribri.myapplication.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.bribri.myapplication.R;
import com.example.bribri.myapplication.models.Event;

import java.util.List;

/**
 * Created by bribri on 10/01/2017.
 */


public class EventAdapter extends BaseAdapter {
    List<Event> events;

    Context context;
    LayoutInflater layoutInflater;
    String login;
    String pwd;
    Event event;

    public EventAdapter(List<Event> events, Context context, String login, String pwd) {
        super();
        this.events = events;
        this.context = context;
        this.login = login;
        this.pwd = pwd;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override public int getCount() {
        return events.size();
    }

    @Override public Object getItem(int position) {
        return events.get(position);
    }

    @Override public long getItemId(int position) {
        return position;
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.event, null);
        TextView message = (TextView) convertView.findViewById(R.id.title);
        event = events.get(position);
        message.setText(event.title);
        TextView description = (TextView) convertView.findViewById(R.id.description);
        description.setText(event.description);
        TextView time = (TextView) convertView.findViewById(R.id.event_time);
        time.setText(event.getTime());
        return convertView;
    }

}