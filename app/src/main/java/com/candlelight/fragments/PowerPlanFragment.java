package com.candlelight.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.candlelight.powerman.R;

/**
 * Created by rancard on 8/12/15.
 */
public class PowerPlanFragment extends Fragment{
    private EditText accountNameEditText;
    private Button submitButton;
    private static Activity mActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_power_plan, container, false);
        accountNameEditText = (EditText) v.findViewById(R.id.account_name_entry);
        submitButton = (Button) v.findViewById(R.id.create_account_button);

        mActivity = getActivity();
        return v;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

}
