package com.example.bribri.myapplication.tools;

import android.os.Environment;

import com.example.bribri.myapplication.models.Event;
import com.example.bribri.myapplication.models.ListEvents;
import com.example.bribri.myapplication.models.User;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by bribri on 12/01/2017.
 */

public class InternalSearcher {

    public static User getCredential()
    {
        User save = null;
        try {
            File file = new File(Environment.getExternalStorageDirectory(), "LifePlanner/Credentials");
            FileInputStream fis = new FileInputStream (file);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            save = new Gson().fromJson(sb.toString(), User.class);
        }
        catch(Exception e){
            return null;
        }
        return save;
    }

    public static ListEvents getEvents()
    {
        ListEvents save = null;
        try {
            File file = new File(Environment.getExternalStorageDirectory(), "LifePlanner/AutoSave");
            FileInputStream fis = new FileInputStream (file);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            save = new Gson().fromJson(sb.toString(), ListEvents.class);
            fis.close();
        }
        catch(Exception e){
            return null;
        }
        return save;
    }

    public static void deleteCredentials(){
        File file = new File(Environment.getExternalStorageDirectory(), "LifePlanner/Credentials");
        file.delete();
    }

    public static void deleteAutoSave(){
        File file = new File(Environment.getExternalStorageDirectory(), "LifePlanner/AutoSave");
        file.delete();
    }

    public static String getUrl()
    {
        String save = null;
        try {
            File file = new File(Environment.getExternalStorageDirectory(), "LifePlanner/Url");
            FileInputStream fis = new FileInputStream (file);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            save = sb.toString();
            fis.close();
        }
        catch(Exception e){
            return null;
        }
        return save;
    }
}
