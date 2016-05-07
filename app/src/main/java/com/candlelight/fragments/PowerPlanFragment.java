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

import com.rancard.kudi.client.async.Callback;
import com.rancard.kudi.domain.Account;
import com.candlelight.powerman.R;

/**
 * Created by rancard on 8/12/15.
 */
public class PowerPlanFragment extends Fragment implements MainFragment, View.OnClickListener{
    private EditText accountNameEditText;
    private Button submitButton;
    private static Activity mActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_power_plan, container, false);
        accountNameEditText = (EditText) v.findViewById(R.id.account_name_entry);
        submitButton = (Button) v.findViewById(R.id.create_account_button);
        submitButton.setOnClickListener(this);

        mActivity = getActivity();
        return v;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public void createAccount(String accName){
        Account account = new Account();
        account.setAccountName(accName);

        session.createAccount(account, new Callback<Long>() {
            @Override
            public void onFailure(String s, int i) {
                Log.d("account error", s);
                Toast.makeText(mActivity, "Failed to Create Account", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(final Long aLong) {

                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("Creating account", aLong.toString());
                        String result = aLong.toString();
                        Log.d("Long account", aLong.toString());
                        Toast toast = Toast.makeText(mActivity, accountNameEditText.getText() + " Account Created Successfully with Number: " + result, Toast.LENGTH_LONG);
                        fireLongToast(toast);
                        accountNameEditText.setText("");
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View view) {
        String accountNameString = accountNameEditText.getText().toString();
        if(TextUtils.isEmpty(accountNameString)) {
            accountNameEditText.setError("Cannot be empty");
            return;
        }
        createAccount(accountNameString);
    }

    private void fireLongToast(final Toast toast) {
        Thread t = new Thread() {
            public void run() {
                int count = 0;
                try {
                    while (true && count < 3) {
                        toast.show();
                        sleep(1850);
                        count++;
                    }
                } catch (Exception e) {
                    Log.e("LongToast", "", e);
                }
            }
        };
        t.start();
    }
}
