package com.candlelight.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.candlelight.powerman.R;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

/**
 * Created by rancard on 8/11/15.
 */
public class ControlFragment extends Fragment implements MainFragment{
    public static Activity mActivity;
    private Socket mSocket;
    private JSONObject led1On = new JSONObject();
    private JSONObject led2On = new JSONObject();
    private JSONObject led3On = new JSONObject();
    private JSONObject led4On = new JSONObject();
    private JSONObject led1Off = new JSONObject();
    private JSONObject led2Off = new JSONObject();
    private JSONObject led3Off = new JSONObject();
    private JSONObject led4Off = new JSONObject();
    private Switch switch1 = null;
    private Switch switch2 = null;
    private Switch switch3 = null;
    private Switch switch4 = null;
    private boolean canEmit = true;

    {
        try {
            led1On.put("value", 4);
            led2On.put("value", 5);
            led3On.put("value", 6);
            led4On.put("value", 7);
            led1Off.put("value", 0);
            led2Off.put("value", 1);
            led3Off.put("value", 2);
            led4Off.put("value", 3);
//            led1On.put("value", 113);
//            led2On.put("value", 119);
//            led3On.put("value", 101);
//            led4On.put("value", 114);
//            led1Off.put("value", 97);
//            led2Off.put("value", 115);
//            led3Off.put("value", 100);
//            led4Off.put("value", 102);
            mSocket = IO.socket("http://192.168.43.98:8080");
            // mSocket= IO.socket("http://192.168.43.30:3465");
            Log.d("socket.io", "connected successfully");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View v =inflater.inflate(com.candlelight.powerman.R.layout.fragment_control, container, false);
        switch1 = (Switch) v.findViewById(R.id.switch1);
        switch2 = (Switch) v.findViewById(R.id.switch2);
        switch3 = (Switch) v.findViewById(R.id.switch3);
        switch4 = (Switch) v.findViewById(R.id.switch4);

        mActivity = getActivity();
        return v;
    }


    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onCreate(savedInstanceState);
        mSocket.connect();

        final SharedPreferences sharedPref = mActivity.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();

        //get last saved states from file
        boolean led1state = sharedPref.getBoolean("state1", true);
        boolean led2state = sharedPref.getBoolean("state2", true);
        boolean led3state = sharedPref.getBoolean("state3", true);
        boolean led4state = sharedPref.getBoolean("state4", true);
        switch1.setChecked(led1state);
        switch2.setChecked(led2state);
        switch3.setChecked(led3state);
        switch4.setChecked(led4state);

        //listen back for data and set the android switches to the right state
        //the socket listens for data coming on each channel
        //the data received represents the state of our switches
        //the switch is turned on or off depending the value it receives from the socket
        try {
            mSocket.on("led1", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    final JSONObject dataReceive = (JSONObject) args[0];
                    Log.d("SocketIO", "" + dataReceive);
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (dataReceive.get("value").toString().equals(led1Off.get("value").toString())) {
                                    //when setChecked() is called, the listener on the switch gets activated
                                    //however if this happens, the received value will be re-emitted over the socket
                                    //this will create an infinite loop of the same received value
                                    //the solution is the canEmit boolean value
                                    //when false, the code in the switch listener prevents the data from being emitted
                                    canEmit = false;
                                    switch1.setChecked(false);
                                } else if (dataReceive.get("value").toString().equals(led1On.get("value").toString())) {
                                    canEmit = false;
                                    switch1.setChecked(true);
                                }
                                //canEmit is set back to true so toggling of the switch by a user can be emitted
                                //over the socket
                                canEmit = true;

                                //the current state of the switch is saved on the device
                                editor.putBoolean("state1", switch1.isChecked());
                                editor.commit();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });
            mSocket.on("led2", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    final JSONObject dataReceive = (JSONObject) args[0];
                    Log.d("SocketIO", "" + dataReceive);
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (dataReceive.get("value").toString().equals(led2Off.get("value").toString())) {
                                    //when setChecked() is called, the listener on the switch gets activated
                                    //however if this happens, the received value will be re-emitted over the socket
                                    //this will create an infinite loop of the same received value
                                    //the solution is the canEmit boolean value
                                    //when false, the code in the switch listener prevents the data from being emitted
                                    canEmit = false;
                                    switch2.setChecked(false);
                                } else if (dataReceive.get("value").toString().equals(led2On.get("value").toString())) {
                                    canEmit = false;
                                    switch2.setChecked(true);
                                }
                                //canEmit is set back to true so toggling of the switch by a user can be emitted
                                //over the socket
                                canEmit = true;

                                //the current state of the switch is saved on the device
                                editor.putBoolean("state2", switch2.isChecked());
                                editor.commit();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });
            mSocket.on("led3", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    final JSONObject dataReceive = (JSONObject) args[0];
                    Log.d("SocketIO", "" + dataReceive);
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (dataReceive.get("value").toString().equals(led3Off.get("value").toString())) {
                                    canEmit = false;
                                    switch3.setChecked(false);
                                } else if (dataReceive.get("value").toString().equals(led3On.get("value").toString())) {
                                    canEmit = false;
                                    switch3.setChecked(true);
                                }
                                canEmit = true;
                                editor.putBoolean("state3", switch3.isChecked());
                                editor.commit();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });
            mSocket.on("led4", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    final JSONObject dataReceive = (JSONObject) args[0];
                    Log.d("SocketIO", "" + dataReceive);
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (dataReceive.get("value").toString().equals(led4Off.get("value").toString())) {
                                    canEmit = false;
                                    switch4.setChecked(false);
                                } else if (dataReceive.get("value").toString().equals(led4On.get("value").toString())) {
                                    canEmit = false;
                                    switch4.setChecked(true);
                                }
                                canEmit = true;
                                editor.putBoolean("state4", switch4.isChecked());
                                editor.commit();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });
        } catch (Exception ex) {
            Log.i("Exception", ex.getMessage());
        }

        //each switch when turned on or off by a user emits that state over the socket to the server
        //the listener knows when this has happened and executes the emit code
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked && canEmit) {
                    mSocket.emit("led1", led1On);
                } else if (!isChecked && canEmit) {
                    mSocket.emit("led1", led1Off);
                }else {
                    Log.d("NO EMIT", "1");
                }
            }
        });

        switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && canEmit) {
                    mSocket.emit("led2", led2On);
                } else if (!isChecked && canEmit) {
                    mSocket.emit("led2", led2Off);
                }else {
                    Log.d("NO EMIT", "2");
                }
            }
        });

        switch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && canEmit) {
                    mSocket.emit("led3", led3On);
                } else if (!isChecked && canEmit) {
                    mSocket.emit("led3", led3Off);
                }
            }
        });

        switch4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && canEmit) {
                    mSocket.emit("led4", led4On);
                } else if (!isChecked && canEmit) {
                    mSocket.emit("led4", led4Off);
                }
            }
        });
    }
}
