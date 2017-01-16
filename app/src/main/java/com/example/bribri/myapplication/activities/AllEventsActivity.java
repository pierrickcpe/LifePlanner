package com.example.bribri.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bribri.myapplication.R;
import com.example.bribri.myapplication.adapters.EventAdapter;
import com.example.bribri.myapplication.models.ListEvents;
import com.example.bribri.myapplication.tools.InternalSearcher;

public class AllEventsActivity extends AppCompatActivity {

    private TextView textView;
    private ListView listView;
    private EventAdapter adapter;
    private ListEvents events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_events);
        Toolbar toolbar = (Toolbar) findViewById(R.id.event_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_events);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AllEventsActivity.this, com.example.bribri.myapplication.activities.AddEventActivity.class);
                startActivity(intent);
            }
        });
        events = new ListEvents();
        events.getSavedDatas();

        listView = (ListView) findViewById(R.id.list_events);
        adapter = new EventAdapter(events.getEventlist(), this, "", "");
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id){
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_month:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            case R.id.action_allevents:
                intent = new Intent(this, com.example.bribri.myapplication.activities.AllEventsActivity.class);
                startActivity(intent);
                break;
            case R.id.action_settings:
                intent = new Intent(this, com.example.bribri.myapplication.activities.SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.action_profile:
                if(InternalSearcher.getCredential()!=null)
                    intent = new Intent(this, com.example.bribri.myapplication.activities.ProfileActivity.class);
                else intent = new Intent(this, com.example.bribri.myapplication.activities.LoginActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
