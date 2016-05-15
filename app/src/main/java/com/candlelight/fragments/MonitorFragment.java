package com.candlelight.fragments;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.candlelight.chart.MyYAxisValueFormatter;
import com.candlelight.powerman.Constants;
import com.candlelight.powerman.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import io.socket.emitter.Emitter;
import io.socket.client.IO;
import io.socket.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by rancard on 8/12/15.
 */
public class MonitorFragment extends Fragment {
    private static Activity mActivity;
    private LineChart lineChart;
    private Socket mSocket;
    private static final long DELAY = 1000L;
    private long currentTime = System.currentTimeMillis();
    private long previousTime = currentTime - DELAY;
    private TextView tvOutsideLights;
    private TextView tvInsideLights;
    private TextView tvSockets;
    private TextView tvAirConditioners;


    {
        try {
            mSocket = IO.socket(Constants.ServerUrl);
            // mSocket= IO.socket("http://192.168.43.30:3465");
            Log.d("socket.io", "connected successfully");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(com.candlelight.powerman.R.layout.fragment_monitor, container, false);
        mActivity = getActivity();

        lineChart = (LineChart) v.findViewById(R.id.chart);
        tvOutsideLights = (TextView) v.findViewById(R.id.tv1);
        tvInsideLights = (TextView) v.findViewById(R.id.tv2);
        tvSockets = (TextView) v.findViewById(R.id.tv3);
        tvAirConditioners = (TextView) v.findViewById(R.id.tv4);

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onCreate(savedInstanceState);
        mSocket.connect();

        lineChart.setDrawGridBackground(true);
        lineChart.setDescription("Live Power Consumption");

        // enable touch gestures
        lineChart.setTouchEnabled(true);

        // enable scaling and dragging
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setDrawGridBackground(false);

        // if disabled, scaling can be done on x- and y-axis separately
        lineChart.setPinchZoom(true);

        // add an empty data object
        lineChart.setData(new LineData());
//        lineChart.getXAxis().setDrawLabels(false);
//        lineChart.getXAxis().setDrawGridLines(false);

        //disable the right y-axis
        lineChart.getAxisRight().setEnabled(false);

        //set position of x-axis
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        //set colour of x and y axes
        lineChart.getXAxis().setAxisLineColor(Color.argb(150, 226, 76, 96));
        lineChart.getAxisLeft().setAxisLineColor(Color.argb(150, 226, 76, 96));

        //set width of x and y axes
        lineChart.getAxisLeft().setAxisLineWidth(2);
        lineChart.getXAxis().setAxisLineWidth(2);

        lineChart.getAxisLeft().setValueFormatter(new MyYAxisValueFormatter());
        addEntry(0, 0);
        addEntry(1, 0);
        addEntry(2, 0);
        addEntry(3, 0);

        mSocket.on("stream", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                final JSONObject dataReceive = (JSONObject) args[0];
                Log.d("SocketIO", "StreamChannel:" + " " + dataReceive);
                currentTime = System.currentTimeMillis();
                try {
                    if (currentTime - previousTime >= DELAY) {
                        addEntry(0, (float) dataReceive.getDouble("value1"));
                        addEntry(1, (float) dataReceive.getDouble("value2"));
                        addEntry(2, (float) dataReceive.getDouble("value3"));
                        addEntry(3, (float) dataReceive.getDouble("value4"));
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    tvOutsideLights.setText(dataReceive.getDouble(("value1")) + " W");
                                    tvInsideLights.setText(dataReceive.getDouble(("value2"))+" W");
                                    tvSockets.setText(dataReceive.getDouble(("value3"))+" W");
                                    tvAirConditioners.setText(dataReceive.getDouble(("value4"))+" W");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        previousTime = currentTime;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    private void addEntry(int setNumber, float input) {

        LineData data = lineChart.getData();
        if(data != null) {

            ILineDataSet set = data.getDataSetByIndex(setNumber);

            // set.addEntry(...); // can be called as well

            if (set == null) {
                set = createSet(setNumber);
                data.addDataSet(set);
            }


            Calendar calendar = Calendar.getInstance();
            int hour = calendar.HOUR_OF_DAY;
            int min = calendar.MINUTE;
            int sec = calendar.SECOND;

            // add a new x-value first
            //data.addXValue(set.getEntryCount() + "");
            if(setNumber == 0) {
                data.addXValue(calendar.get(hour)+ ":" + calendar.get(min) + ":" + calendar.get(sec) + " GMT");
            }

            // choose a random dataSet
            data.addEntry(new Entry((float) input, set.getEntryCount()), setNumber);

            // let the chart know it's data has changed
            lineChart.notifyDataSetChanged();

            lineChart.setVisibleXRangeMaximum(10);
            //lineChart.setVisibleYRangeMaximum(100, YAxis.AxisDependency.LEFT);

            // this automatically refreshes the chart (calls invalidate())
            lineChart.moveViewToX(data.getXValCount() - 11);
        }
    }

    private LineDataSet createSet(int pos) {

        if(pos == 0) {
            LineDataSet set = new LineDataSet(null, "Outside Lights");
            set.setAxisDependency(YAxis.AxisDependency.LEFT);
            set.setColor(ColorTemplate.getHoloBlue());
            set.setDrawCircles(false);
            //set.setCircleColor(Color.WHITE);
            //set.setCircleRadius(4f);
            set.setLineWidth(2f);
            set.setFillAlpha(65);
            set.setFillColor(ColorTemplate.getHoloBlue());
            set.setHighLightColor(Color.rgb(244, 117, 117));
            set.setValueTextColor(Color.WHITE);
            set.setValueTextSize(9f);
            set.setDrawValues(false);
            //set.setDrawCubic(true); //makes the curve smooth
            //set.setDrawFilled(true); //fills area under curve
            return set;
        }else if (pos==1) {
            LineDataSet set = new LineDataSet(null, "Inside Lights");
            set.setAxisDependency(YAxis.AxisDependency.LEFT);
            set.setColor(Color.MAGENTA);
            set.setDrawCircles(false);
            //set.setCircleColor(Color.WHITE);
            //set.setCircleRadius(4f);
            set.setLineWidth(2f);
            set.setFillAlpha(65);
            set.setFillColor(Color.MAGENTA);
            set.setHighLightColor(Color.rgb(244, 117, 117));
            set.setValueTextColor(Color.WHITE);
            set.setValueTextSize(9f);
            set.setDrawValues(false);
            //set.setDrawCubic(true); //makes the curve smooth
            //set.setDrawFilled(true); //fills area under curve
            return set;
        }else if (pos==2) {
            LineDataSet set = new LineDataSet(null, "Sockets");
            set.setAxisDependency(YAxis.AxisDependency.LEFT);
            set.setColor(Color.GREEN);
            set.setDrawCircles(false);
            //set.setCircleColor(Color.WHITE);
            //set.setCircleRadius(4f);
            set.setLineWidth(2f);
            set.setFillAlpha(65);
            set.setFillColor(Color.GREEN);
            set.setHighLightColor(Color.rgb(244, 117, 117));
            set.setValueTextColor(Color.WHITE);
            set.setValueTextSize(9f);
            set.setDrawValues(false);
            //set.setDrawCubic(true); //makes the curve smooth
            //set.setDrawFilled(true); //fills area under curve
            return set;
        }else if (pos==3) {
            LineDataSet set = new LineDataSet(null, "Air Conditioners");
            set.setAxisDependency(YAxis.AxisDependency.LEFT);
            set.setColor(Color.rgb(255,153, 21));
            set.setDrawCircles(false);
            //set.setCircleColor(Color.WHITE);
            //set.setCircleRadius(4f);
            set.setLineWidth(2f);
            set.setFillAlpha(65);
            set.setFillColor(Color.rgb(255, 153, 21));
            set.setHighLightColor(Color.rgb(244, 117, 117));
            set.setValueTextColor(Color.WHITE);
            set.setValueTextSize(9f);
            set.setDrawValues(false);
            //set.setDrawCubic(true); //makes the curve smooth
            //set.setDrawFilled(true); //fills area under curve
            return set;
        }
        LineDataSet set = new LineDataSet(null, "");
        return set;
    }
}
