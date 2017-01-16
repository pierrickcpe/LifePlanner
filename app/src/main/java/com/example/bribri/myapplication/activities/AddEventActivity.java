package com.example.bribri.myapplication.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.bribri.myapplication.R;
import com.example.bribri.myapplication.models.Event;

import java.util.Calendar;
import java.util.Date;

import static com.example.bribri.myapplication.tools.Formatter.getDate;
import static com.example.bribri.myapplication.tools.Formatter.getTime;

public class AddEventActivity extends AppCompatActivity {

    TextView TVevent_start_date;
    TextView TVevent_start_time;
    TextView TVevent_end_date;
    TextView TVevent_end_time;
    AppCompatCheckBox CBevent_end_checkbox;
    Calendar startCalendar;
    Calendar endCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        Bundle extras = getIntent().getExtras();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Date date = (Date)getIntent().getSerializableExtra("cellSelected");
        startCalendar = Calendar.getInstance();
        startCalendar.setTime(date);
        endCalendar = Calendar.getInstance();
        endCalendar.setTime(date);
        endCalendar.add(Calendar.HOUR,1);

        TVevent_start_date = (TextView) findViewById(R.id.event_start_date);
        TVevent_start_time = (TextView) findViewById(R.id.event_start_time);
        TVevent_end_date = (TextView) findViewById(R.id.event_end_date);
        TVevent_end_time = (TextView) findViewById(R.id.event_end_time);

        TVevent_end_date.setText(getDate(endCalendar));
        TVevent_start_date.setText(getDate(startCalendar));
        TVevent_start_time.setText(getTime(startCalendar));
        TVevent_end_time.setText(getTime(endCalendar));

        final DatePickerDialog.OnDateSetListener startDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                startCalendar.set(Calendar.YEAR, year);
                startCalendar.set(Calendar.MONTH, monthOfYear);
                startCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                endCalendar = startCalendar;
                endCalendar.add(Calendar.HOUR,1);
                TVevent_end_date.setText(getDate(endCalendar));
                TVevent_start_date.setText(getDate(startCalendar));
            }
        };

        final DatePickerDialog.OnDateSetListener endDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                endCalendar.set(Calendar.YEAR, year);
                endCalendar.set(Calendar.MONTH, monthOfYear);
                endCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                TVevent_end_date.setText(getDate(endCalendar));
            }
        };

        final TimePickerDialog.OnTimeSetListener startTimeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                // TODO Auto-generated method stub
                startCalendar.set(Calendar.HOUR, selectedHour);
                startCalendar.set(Calendar.MINUTE, selectedMinute);
                TVevent_start_time.setText(getTime(startCalendar));
            }
        };

        final TimePickerDialog.OnTimeSetListener endTimeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                // TODO Auto-generated method stub
                endCalendar.set(Calendar.HOUR, selectedHour);
                endCalendar.set(Calendar.MINUTE, selectedMinute);
                TVevent_end_time.setText(getTime(endCalendar));
            }
        };

        TVevent_start_date.setText(getDate(startCalendar));
        TVevent_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddEventActivity.this, startDateListener, startCalendar
                        .get(Calendar.YEAR), startCalendar.get(Calendar.MONTH),
                        startCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        TVevent_start_time.setText(getTime(startCalendar));
        TVevent_start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(AddEventActivity.this, startTimeListener, startCalendar
                        .get(Calendar.HOUR), startCalendar.get(Calendar.MINUTE),true).show();
            }
        });

        TVevent_end_date.setText(getDate(endCalendar));
        TVevent_end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddEventActivity.this, endDateListener, endCalendar
                        .get(Calendar.YEAR), endCalendar.get(Calendar.MONTH),
                        endCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        TVevent_end_time.setText(getTime(endCalendar));
        TVevent_end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(AddEventActivity.this, endTimeListener, endCalendar
                        .get(Calendar.HOUR), endCalendar.get(Calendar.MINUTE),true).show();
            }
        });

        CBevent_end_checkbox = (AppCompatCheckBox) findViewById(R.id.event_end_checkbox);
        CBevent_end_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    TVevent_end_date.setVisibility(View.VISIBLE);
                    TVevent_end_time.setVisibility(View.VISIBLE);
                }
                else{
                    TVevent_end_date.setVisibility(View.GONE);
                    TVevent_end_time.setVisibility(View.GONE);
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.save) {
            EditText title   = (EditText)findViewById(R.id.event_title);
            EditText description   = (EditText)findViewById(R.id.event_description);
            if(!CBevent_end_checkbox.isChecked()) endCalendar = startCalendar;
            else{
                if(endCalendar.before(startCalendar)) {
                    Toast.makeText(this, "Wrong end date",
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
            if(title.getText().toString().equals("")){
                Toast.makeText(this, "Event needs a title",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
            Event newEvent = new Event(1,title.getText().toString(),description.getText().toString(),startCalendar.getTime(),endCalendar.getTime());

            Intent intent = new Intent(AddEventActivity.this, MainActivity.class);
            intent.putExtra("NewEvent", newEvent);
            Event toDelete = (Event)getIntent().getSerializableExtra("toDelete");
            intent.putExtra("toDelete", toDelete);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
