package com.example.bribri.myapplication.models;

import android.text.format.DateFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by bribri on 10/01/2017.
 */


public class Event implements Serializable{
    public String id;
    public String title;
    public String description;
    String color;
    String status;
    public Date start;
    public Date end;
    public String reminder;
    String locationsCoord;
    String locationsName;
    String author;

    public Event(String id, String title, String description, Date start, Date end)
    {
        this.id = id;
        this.description = description;
        this.title = title;
        this.start = start;
        this.end = end;
    }

    public boolean isDateInEvent(Date date){
        String dateDebut = DateFormat.format("yyyy.MM.dd", start.getTime()).toString();
        boolean isSameDay = dateDebut.equals(DateFormat.format("yyyy.MM.dd", date.getTime()).toString());
        boolean result = (isSameDay||(start.before(date)&&end.after(date)));
        return result;
    }

    public String getTime(){
        String startDate = DateFormat.format("dd/MM, HH:mm", start).toString();
        String endDate = DateFormat.format("dd/MM, HH:mm", end).toString();
        return startDate + " - " + endDate;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Event)
        {
            Event object = (Event)obj;
            String dateDebut = DateFormat.format("yyyy.MM.dd", start.getTime()).toString();
            String tets = DateFormat.format("yyyy.MM.dd", object.start.getTime()).toString();
            if(!dateDebut.equals(tets))
                return false;
            String dateFin = DateFormat.format("yyyy.MM.dd", end.getTime()).toString();
            if(!dateFin.equals(DateFormat.format("yyyy.MM.dd", object.end.getTime()).toString()))
                return false;
            if(object.title.equals(this.title) &&
                ((object.description==null && this.description==null)|| object.description.equals(this.description))
                    && object.id.equals(this.id));
                return true;
        }
        return false;
    }

    public boolean valid(){
        return(start.getTime()<end.getTime());
    }
}
