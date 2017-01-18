package com.example.bribri.myapplication.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bribri.myapplication.R;
import com.example.bribri.myapplication.adapters.EventAdapter;
import com.example.bribri.myapplication.fragments.EventDialogFragment;
import com.example.bribri.myapplication.models.Event;
import com.example.bribri.myapplication.models.ListEvents;
import com.example.bribri.myapplication.models.User;
import com.example.bribri.myapplication.tasks.AddEventAsyncTask;
import com.example.bribri.myapplication.tasks.DeleteEventAsyncTask;
import com.example.bribri.myapplication.tasks.GetUserEventsAsyncTask;
import com.example.bribri.myapplication.tools.Formatter;
import com.example.bribri.myapplication.tools.InternalSaver;
import com.example.bribri.myapplication.tools.InternalSearcher;
import com.google.gson.Gson;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.bribri.myapplication.tools.InternalSaver.saveEnvents;

public class MainActivity extends AppCompatActivity {

    private CaldroidFragment caldroidFragment;
    private Date cellSelected;
    private List<Event> eventsOfSelectedDate;
    private Calendar current;
    private ListEvents events;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, com.example.bribri.myapplication.activities.AddEventActivity.class);
                intent.putExtra("cellSelected", cellSelected);
                startActivity(intent);
            }
        });

        current = Calendar.getInstance();
        events = new ListEvents();
        events.getSavedDatas();
        user = InternalSearcher.getCredential();

        if(user!=null)
        {
            GetUserEventsAsyncTask GetListThread = new GetUserEventsAsyncTask();
            GetUserEventsAsyncTask.GetUserEventsListener GetUserEventsListener = new GetUserEventsAsyncTask.GetUserEventsListener(){
                @Override public void onGetUserEvents(ListEvents result) {
                    events.merge(result);
                    refreshEventsOnView();
                }
            };
            GetListThread.setUserEventsListener(GetUserEventsListener);
            GetListThread.execute(user);
        }

        Event newEvent = (Event)getIntent().getSerializableExtra("NewEvent");
        if(newEvent!=null) {
            current.setTime(newEvent.start);
            events.add(newEvent);
            saveEnvents(events);
            getIntent().removeExtra("NewEvent");
            if(user!=null)
            {
                AddEventAsyncTask AddEventThread = new AddEventAsyncTask();
                AddEventAsyncTask.AddEventListener AddEventListener = new AddEventAsyncTask.AddEventListener(){
                    @Override public void onAddEvent(String result) {
                        if(result.equals("success")) {
                            Toast.makeText(MainActivity.this, "Event added",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(MainActivity.this, "Failed to synch with server",
                                Toast.LENGTH_SHORT).show();
                        }
                        refreshEventsOnView();
                    }
                };
                AddEventThread.setAddEventListener(AddEventListener);
                AddEventThread.execute(newEvent);
            }
        }

        Event toDelete = (Event)getIntent().getSerializableExtra("toDelete");
        if(toDelete!=null) {
            events.delete(toDelete);
            saveEnvents(events);

            getIntent().removeExtra("toDelete");
            if(user!=null)
            {
                DeleteEventAsyncTask DeleteEventThread = new DeleteEventAsyncTask();
                DeleteEventAsyncTask.DeleteEventListener DeleteEventListener = new DeleteEventAsyncTask.DeleteEventListener(){
                    @Override public void onDeleteEvent(String result) {
                        if(result.equals("success")) {
                            Toast.makeText(MainActivity.this, "Event deleted",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(MainActivity.this, "Failed to synch with server",
                                    Toast.LENGTH_SHORT).show();
                        }
                        refreshEventsOnView();
                    }
                };
                DeleteEventThread.setDeleteEventListener(DeleteEventListener);
                DeleteEventThread.execute(toDelete);
            }
        }

        caldroidFragment = new CaldroidFragment();
        if (savedInstanceState != null) {
            caldroidFragment.restoreStatesFromKey(savedInstanceState,
                    "CALDROID_SAVED_STATE");
        }
        else {
            Bundle args = new Bundle();
            args.putInt(CaldroidFragment.MONTH, current.get(Calendar.MONTH) + 1);
            args.putInt(CaldroidFragment.YEAR, current.get(Calendar.YEAR));
            args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
            args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);

            caldroidFragment.setArguments(args);
        }
        if(events.getEventlist()!=null)
            refreshEventsOnView();

        // Attach to the activity
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.add(R.id.content_calendar, caldroidFragment);
        t.commit();
        // Setup listener
        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {
                ColorDrawable blue = new ColorDrawable(getResources().getColor(R.color.blue));
                if(cellSelected!=null){
                    if(events.isDateWithEvent(cellSelected)) {
                        caldroidFragment.setBackgroundDrawableForDate(new ColorDrawable(getResources().getColor(R.color.event)), cellSelected);
                    }
                    else {
                        caldroidFragment.setBackgroundDrawableForDate(new ColorDrawable(getResources().getColor(R.color.white)), cellSelected);
                    }/*
                    String test = (String)android.text.format.DateFormat.format("MM", cellSelected);
                    String tesst =(String)android.text.format.DateFormat.format("MM", current.getTime());
                    if(test.equals(tesst))
                        caldroidFragment.setTextColorForDate(R.color.black, cellSelected);
                    else
                        caldroidFragment.setTextColorForDate(R.color.grey, cellSelected);*/
                }
                cellSelected = date;
                caldroidFragment.setBackgroundDrawableForDate(new ColorDrawable(getResources().getColor(R.color.current)), Calendar.getInstance().getTime());
                caldroidFragment.setBackgroundDrawableForDate(blue, date);
                eventsOfSelectedDate = events.getEventsFromDate(date);
                ListView listView = (ListView) findViewById(R.id.selected_events);
                EventAdapter adapter = new EventAdapter(eventsOfSelectedDate, MainActivity.this, "", "");
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position,
                                            long id) {
                        EventDialogFragment edf = EventDialogFragment.newInstance(eventsOfSelectedDate.get(position),cellSelected);
                        edf.show(getFragmentManager(),"frag");
                    }
                });
                registerForContextMenu(listView);
                caldroidFragment.refreshView();
            }

            @Override
            public void onChangeMonth(int month, int year) {
                String text = "month: " + month + " year: " + year;
                //current.set(Calendar.MONTH,month-1);
                //current.set(Calendar.YEAR,year);
            }

            @Override
            public void onLongClickDate(Date date, View view) {
                /*Toast.makeText(getApplicationContext(),
                        "Long click " + formatter.format(date),
                        Toast.LENGTH_SHORT).show();*/
            }

            @Override
            public void onCaldroidViewCreated() {

                caldroidFragment.getCaldroidListener().onSelectDate(Calendar.getInstance().getTime(),caldroidFragment.getView());
                if (caldroidFragment.getLeftArrowButton() != null) {

                }
            }

        };

        // Setup Caldroid
        caldroidFragment.setCaldroidListener(listener);

    }

    @Override
    protected void onStop() {
        saveEnvents(events);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
        Intent intent = new Intent();
        switch(id){
            case R.id.action_allevents:
                saveEnvents(events);
                intent = new Intent(MainActivity.this, com.example.bribri.myapplication.activities.AllEventsActivity.class);
                startActivity(intent);
                break;
            case R.id.action_settings:
                intent = new Intent(MainActivity.this, com.example.bribri.myapplication.activities.SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.action_profile:
                if(InternalSearcher.getCredential()!=null)
                    intent = new Intent(MainActivity.this, com.example.bribri.myapplication.activities.ProfileActivity.class);
                else intent = new Intent(MainActivity.this, com.example.bribri.myapplication.activities.LoginActivity.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId()==R.id.selected_events) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_event, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Intent intent;
        switch(item.getItemId()) {
            case R.id.edit:
                intent = new Intent(this, com.example.bribri.myapplication.activities.AddEventActivity.class);
                intent.putExtra("toDelete", eventsOfSelectedDate.get(info.position));
                intent.putExtra("cellSelected", cellSelected);
                startActivity(intent);
                return true;
            case R.id.delete:
                intent = new Intent(this, com.example.bribri.myapplication.activities.MainActivity.class);
                intent.putExtra("toDelete", eventsOfSelectedDate.get(info.position));
                startActivity(intent);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void refreshEventsOnView(){
        for (int i = 0; i < events.size(); i++)
        {
            Event event = events.get(i);
            Calendar var = Calendar.getInstance();
            var.setTime(event.start);
            ColorDrawable color;
            Calendar fin = Calendar.getInstance();
            fin.setTime(event.end);
            String varDate;
            String endDate = Formatter.getDate(fin);
            do{
                varDate = Formatter.getDate(var);
                color = new ColorDrawable(getResources().getColor(R.color.event));
                caldroidFragment.setBackgroundDrawableForDate(color, var.getTime());
                var.add(Calendar.DATE,1);
            }while(!varDate.equals(endDate));
            caldroidFragment.refreshView();
        }
    }
}
