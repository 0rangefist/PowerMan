package com.candlelight.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.candlelight.powerman.R;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * Created by rancard on 8/11/15.
 */
public class ScheduleFragment extends Fragment implements MainFragment{
    private static Activity mActivity;
    private TimePicker onTimePicker;
    private TimePicker offTimePicker;
    private CheckBox monCheckBox;
    private CheckBox tuesCheckBox;
    private CheckBox wedCheckBox;
    private CheckBox thurCheckBox;
    private CheckBox friCheckBox;
    private CheckBox satCheckBox;
    private CheckBox sunCheckBox;
    private Calendar calendar;
    private String days;
    private Button submitButton;
    private Socket mSocket;
    private String onCron;
    private String offCron;
    private String socketChannel;
    private Spinner dropDown;

    private int onValue;
    private int offValue;

    //strings for loading and saving states
    private String onHour;
    private String offHour;
    private String onMin;
    private String offMin;
    private String monCheckState;
    private String tuesCheckState;
    private String wedCheckState;
    private String thurCheckState;
    private String friCheckState;
    private String satCheckState;
    private String sunCheckState;


    {
        try {
            mSocket = IO.socket("http://192.168.43.98:8080");
            // mSocket= IO.socket("http://192.168.43.30:3465");
            Log.d("socket.io", "connected successfully");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(com.candlelight.powerman.R.layout.fragment_schedule, container, false);
        mActivity = getActivity();
        //associate pickers
        onTimePicker = (TimePicker) v.findViewById(R.id.timePicker1);
        offTimePicker = (TimePicker) v.findViewById(R.id.timePicker2);

        //associate checkboxes
        monCheckBox = (CheckBox) v.findViewById(R.id.monCheckBox);
        tuesCheckBox = (CheckBox) v.findViewById(R.id.tuesCheckBox);
        wedCheckBox = (CheckBox) v.findViewById(R.id.wedCheckBox);
        thurCheckBox = (CheckBox) v.findViewById(R.id.thurCheckBox);
        friCheckBox = (CheckBox) v.findViewById(R.id.friCheckBox);
        satCheckBox = (CheckBox) v.findViewById(R.id.satCheckBox);
        sunCheckBox = (CheckBox) v.findViewById(R.id.sunCheckBox);

        //associate spinner(dropdown list)
        dropDown = (Spinner) v.findViewById(R.id.spinner);

        //add items to the dropdown list
        List<String> dropDownItems = new ArrayList<>();
        dropDownItems.add("Outside Lighting");
        dropDownItems.add("Inside Lighting");
        dropDownItems.add("Sockets");
        dropDownItems.add("Air Conditioners");

        //use an adapter to conform the String list to the dropDown
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>( mActivity, android.R.layout.simple_spinner_dropdown_item, dropDownItems);
        dropDown.setAdapter(dataAdapter);

        submitButton = (Button) v.findViewById(R.id.submitButton);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onCreate(savedInstanceState);

        //connect to socket
        mSocket.connect();

        //create a shared preference file
        final SharedPreferences sharedPref = mActivity.getSharedPreferences(
                getString(R.string.preference_file_key2), Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();

        //what to do when an item is selected from the dropDown list
        dropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    socketChannel = "led1s";
                    onValue = 4;
                    offValue = 0;
                    onHour = "onHour1";
                    offHour = "offHour1";
                    onMin = "onMin1";
                    offMin = "offMin1";
                    monCheckState = "monCheckState1";
                    tuesCheckState = "tuesCheckState1";
                    wedCheckState = "wedCheckState1";
                    thurCheckState = "thurCheckState1";
                    friCheckState = "friCheckState1";
                    satCheckState = "satCheckState1";
                    sunCheckState = "sunCheckState1";
                } else if (position == 1) {
                    socketChannel = "led2s";
                    onValue = 5;
                    offValue = 1;
                    onHour = "onHour2";
                    offHour = "offHour2";
                    onMin = "onMin2";
                    offMin = "offMin2";
                    monCheckState = "monCheckState2";
                    tuesCheckState = "tuesCheckState2";
                    wedCheckState = "wedCheckState2";
                    thurCheckState = "thurCheckState2";
                    friCheckState = "friCheckState2";
                    satCheckState = "satCheckState2";
                    sunCheckState = "sunCheckState2";
                } else if (position == 2) {
                    socketChannel = "led3s";
                    onValue = 6;
                    offValue = 2;
                    onHour = "onHour3";
                    offHour = "offHour3";
                    onMin = "onMin3";
                    offMin = "offMin3";
                    monCheckState = "monCheckState3";
                    tuesCheckState = "tuesCheckState3";
                    wedCheckState = "wedCheckState3";
                    thurCheckState = "thurCheckState3";
                    friCheckState = "friCheckState3";
                    satCheckState = "satCheckState3";
                    sunCheckState = "sunCheckState3";
                } else if (position == 3) {
                    socketChannel = "led4s";
                    onValue = 7;
                    offValue = 3;
                    onHour = "onHour4";
                    offHour = "offHour4";
                    onMin = "onMin4";
                    offMin = "offMin4";
                    monCheckState = "monCheckState4";
                    tuesCheckState = "tuesCheckState4";
                    wedCheckState = "wedCheckState4";
                    thurCheckState = "thurCheckState4";
                    friCheckState = "friCheckState4";
                    satCheckState = "satCheckState4";
                    sunCheckState = "sunCheckState4";
                }//load picker values from shared preferences else use current hour and min
                calendar = Calendar.getInstance();
                onTimePicker.setCurrentHour(sharedPref.getInt(onHour, calendar.get(Calendar.HOUR)));
                onTimePicker.setCurrentMinute(sharedPref.getInt(onMin, calendar.get(Calendar.MINUTE)));
                offTimePicker.setCurrentHour(sharedPref.getInt(offHour, calendar.get(Calendar.HOUR)));
                offTimePicker.setCurrentMinute(sharedPref.getInt(offMin, calendar.get(Calendar.MINUTE)));

                //load checkbox states from shared preferences else default to unchecked
                monCheckBox.setChecked(sharedPref.getBoolean(monCheckState, false));
                tuesCheckBox.setChecked(sharedPref.getBoolean(tuesCheckState, false));
                wedCheckBox.setChecked(sharedPref.getBoolean(wedCheckState, false));
                thurCheckBox.setChecked(sharedPref.getBoolean(thurCheckState, false));
                friCheckBox.setChecked(sharedPref.getBoolean(friCheckState, false));
                satCheckBox.setChecked(sharedPref.getBoolean(satCheckState, false));
                sunCheckBox.setChecked(sharedPref.getBoolean(sunCheckState, false));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //when nothing is selected, use current time and clear all checkboxes
                calendar = Calendar.getInstance();
                onTimePicker.setCurrentHour(calendar.get(Calendar.HOUR));
                onTimePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
                offTimePicker.setCurrentHour(calendar.get(Calendar.HOUR));
                offTimePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
                monCheckBox.setChecked(false);
                tuesCheckBox.setChecked(false);
                wedCheckBox.setChecked(false);
                thurCheckBox.setChecked(false);
                friCheckBox.setChecked(false);
                satCheckBox.setChecked(false);
                sunCheckBox.setChecked(false);
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                days = "";

                if (sunCheckBox.isChecked()) {
                    if (days.isEmpty()) {
                        days = days + "0";
                    } else {
                        days = days + ",0";
                    }
                }
                if (monCheckBox.isChecked()) {
                    if (days.isEmpty()) {
                        days = days + "1";
                    } else {
                        days = days + ",1";
                    }
                }
                if (tuesCheckBox.isChecked()) {
                    if (days.isEmpty()) {
                        days = days + "2";
                    } else {
                        days = days + ",2";
                    }
                }
                if (wedCheckBox.isChecked()) {
                    if (days.isEmpty()) {
                        days = days + "3";
                    } else {
                        days = days + ",3";
                    }
                }
                if (thurCheckBox.isChecked()) {
                    if (days.isEmpty()) {
                        days = days + "4";
                    } else {
                        days = days + ",4";
                    }
                }
                if (friCheckBox.isChecked()) {
                    if (days.isEmpty()) {
                        days = days + "5";
                    } else {
                        days = days + ",5";
                    }
                }
                if (satCheckBox.isChecked()) {
                    if (days.isEmpty()) {
                        days = days + "6";
                    } else {
                        days = days + ",6";
                    }
                }

                int onHourValue = onTimePicker.getCurrentHour();
                int onMinValue = onTimePicker.getCurrentMinute();
                int offHourValue = offTimePicker.getCurrentHour();
                int offMinValue = offTimePicker.getCurrentMinute();
                //save this hour and min
                Log.d("PowerMan: ON TIME", onHourValue + ":" + onMinValue);
                Log.d("PowerMan: OFF TIME", offHourValue + ":" + offMinValue);
                Log.d("PowerMan: DAYS", days);

                //save the current ON/OFF times and days to file
                editor.putInt(onHour, onHourValue);
                editor.putInt(onMin, onMinValue);
                editor.putInt(offHour, offHourValue);
                editor.putInt(offMin, offMinValue);
                editor.putBoolean(monCheckState, monCheckBox.isChecked());
                editor.putBoolean(tuesCheckState, tuesCheckBox.isChecked());
                editor.putBoolean(wedCheckState, wedCheckBox.isChecked());
                editor.putBoolean(thurCheckState, thurCheckBox.isChecked());
                editor.putBoolean(friCheckState, friCheckBox.isChecked());
                editor.putBoolean(satCheckState, satCheckBox.isChecked());
                editor.putBoolean(sunCheckState, sunCheckBox.isChecked());
                editor.commit();

                //send them via a socket to the node.js server
                //reformat the on and off cron data when submit button is pressed
                onCron = "";
                offCron = "";
                Log.d("PowerMan: CronFormat", onCron);
                Log.d("PowerMan: CronFormat", offCron);

                onCron = onCron + onMinValue + " " + onHourValue + " *" + " * " + days;
                offCron = offCron + offMinValue + " " + offHourValue + " *" + " * " + days;
//                Log.d("PowerMan: CronFormat", onCron);
//                Log.d("PowerMan: CronFormat", offCron);
//                Log.d("PowerMan: SocketName", socketChannel);
//                Log.d("PowerMan: ON/OFF value", onValue + "/" + offValue);

                JSONObject schedule = new JSONObject();
                try {
                    schedule.put("onDate", onCron);
                    schedule.put("offDate", offCron);
                    schedule.put("onValue", onValue);
                    schedule.put("offValue", offValue);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d("PowerMan: Schedule", socketChannel + " " + schedule.toString());
                mSocket.emit(socketChannel, schedule);
                mSocket.on(socketChannel, new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        final JSONObject dataReceive = (JSONObject) args[0];
                        Log.d("SocketIO", socketChannel + "" + dataReceive);
                    }
                });
            }
        });
    }
}
