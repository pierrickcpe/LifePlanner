package com.example.bribri.myapplication.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.bribri.myapplication.R;
import com.example.bribri.myapplication.models.Event;

import java.util.Date;

/**
 * Created by bribri on 18/01/2017.
 */

public class EventDialogFragment extends DialogFragment {

    private Event mEvent;
    private Date mDate;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.action_event)
                .setItems(R.array.action, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent;
                        switch (which) {
                            case 0:
                                intent = new Intent(getActivity(), com.example.bribri.myapplication.activities.AddEventActivity.class);
                                intent.putExtra("toDelete", mEvent);
                                intent.putExtra("cellSelected", mDate);
                                startActivity(intent);
                                break;
                            case 1:
                                intent = new Intent(getActivity(), com.example.bribri.myapplication.activities.MainActivity.class);
                                intent.putExtra("toDelete", mEvent);
                                startActivity(intent);
                                break;
                        }
                    }
                });
        return builder.create();
    }

    public static EventDialogFragment newInstance(Event e, Date d){
        EventDialogFragment newI = new EventDialogFragment();
        newI.mDate = d;
        newI.mEvent = e;
        return newI;
    }
}
