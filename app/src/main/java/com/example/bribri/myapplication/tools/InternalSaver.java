package com.example.bribri.myapplication.tools;

import android.os.Environment;

import com.example.bribri.myapplication.models.ListEvents;
import com.example.bribri.myapplication.models.User;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by bribri on 12/01/2017.
 */

public class InternalSaver {

    public static void saveEnvents(ListEvents events){
        File sdcard = Environment.getExternalStorageDirectory();
        if( sdcard == null || !sdcard.isDirectory()) {
            //fail("sdcard not available");
            return;
        }
        File datadir = new File(sdcard, "LifePlanner/");

        if( !datadir.exists() && !datadir.mkdirs() ) {
            //fail("unable to create data directory");
            return;
        }
        if( !datadir.isDirectory() ) {
            //fail("exists, but is not a directory");
            return;
        }
        try {
            File file = new File(datadir, "AutoSave");

            Gson gson = new Gson();
            String json = gson.toJson(events);

            FileWriter writer = new FileWriter(file);
            writer.append(json);
            writer.flush();
            writer.close();
        }
        catch (IOException e) {

        }
    }

    public static void saveCredentials(User user){
        File sdcard = Environment.getExternalStorageDirectory();
        if( sdcard == null || !sdcard.isDirectory()) {
            //fail("sdcard not available");
            return;
        }
        File datadir = new File(sdcard, "LifePlanner/");

        if( !datadir.exists() && !datadir.mkdirs() ) {
            //fail("unable to create data directory");
            return;
        }
        if( !datadir.isDirectory() ) {
            //fail("exists, but is not a directory");
            return;
        }
        try {
            File file = new File(datadir, "Credentials");

            Gson gson = new Gson();
            String json = gson.toJson(user);

            FileWriter writer = new FileWriter(file);
            writer.append(json);
            writer.flush();
            writer.close();
        }
        catch (IOException e) {

        }
    }
}
