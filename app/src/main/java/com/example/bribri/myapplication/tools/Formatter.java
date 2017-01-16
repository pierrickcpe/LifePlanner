package com.example.bribri.myapplication.tools;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by bribri on 11/01/2017.
 */

public class Formatter {

    public static String getTime(Calendar c)
    {
        return new SimpleDateFormat("HH:mm").format(c.getTime());
    }
    public static String getDate(Calendar c)
    {
        return new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
    }

}
