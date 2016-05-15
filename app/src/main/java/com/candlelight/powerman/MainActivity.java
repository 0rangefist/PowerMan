package com.candlelight.powerman;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.candlelight.fragments.PowerPlanFragment;
import com.candlelight.fragments.ControlFragment;
import com.candlelight.fragments.ScheduleFragment;
import com.candlelight.fragments.MonitorFragment;


public class MainActivity extends AppCompatActivity
        implements NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private NavigationDrawerFragment mExchangeRatesFragment;
    private NavigationDrawerFragment mConvertCurrencyFragment;
    private Toolbar mToolbar;
    AppCompatActivity activity;

    public static final int MONITOR = 0;
    public static final int CONTROL = 1;
    public static final int SCHEDULE = 2;
    public static final int POWER_PLAN = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbar);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.fragment_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);
        activity = this;
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        FragmentManager frgManager;
        FragmentTransaction ft;

        if (position == MONITOR) {
            MonitorFragment f1 = new MonitorFragment();
            frgManager = getSupportFragmentManager();
            ft = frgManager.beginTransaction();
            ft.replace(R.id.container, f1); // f1_container is your FrameLayout container
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
            // set the toolbar title
            setTitle(Constants.Monitor);
        } else if (position == CONTROL) {
            ControlFragment f2 = new ControlFragment();
            frgManager = getSupportFragmentManager();
            ft = frgManager.beginTransaction();
            ft.replace(R.id.container, f2); // f1_container is your FrameLayout container
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
            // set the toolbar title
            setTitle(Constants.Control);

        } else if (position == SCHEDULE) {
            ScheduleFragment f3 = new ScheduleFragment();
            frgManager = getSupportFragmentManager();
            ft = frgManager.beginTransaction();
            ft.replace(R.id.container, f3); // f1_container is your FrameLayout container
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
            setTitle(Constants.Schedule);
        } else if (position == POWER_PLAN) {
            PowerPlanFragment f4 = new PowerPlanFragment();
            frgManager = getSupportFragmentManager();
            ft = frgManager.beginTransaction();
            ft.replace(R.id.container, f4); // f1_container is your FrameLayout container
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
            setTitle(Constants.PowerPlan);
        }
//        Toast.makeText(this, "Menu item selected -> " + position, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onBackPressed() {
        if (mNavigationDrawerFragment.isDrawerOpen())
            mNavigationDrawerFragment.closeDrawer();
        else
            super.onBackPressed();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}