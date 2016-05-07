package com.candlelight.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rancard.kudi.client.async.Callback;
import com.rancard.kudi.domain.User;

/**
 * Created by rancard on 8/12/15.
 */
public class MonitorFragment extends Fragment {
    private static Activity mActivity;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(com.candlelight.powerman.R.layout.fragment_monitor, container, false);

        mActivity = getActivity();
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onCreate(savedInstanceState);


    }
}
