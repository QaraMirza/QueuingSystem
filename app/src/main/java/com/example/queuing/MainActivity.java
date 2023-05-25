package com.example.queuing;

import androidx.annotation.ColorRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private View channelIn, clientIn, serviceIn, queueIn;
    private Switch switchQueue;
    private Button calculateButton;
    private TextView systemLoadView, loadPerChannelView, downtimeProbabilityView,
            failureProbabilityView, numberOfApplicationsView, waitingTimeView, servedCustomersView,
            serviceTimeView, appsInSystemView, timeInSystemView, relativeAbiliryView, AbsoluteAbilityView;
    private boolean isQueueExists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialisation of variables
        channelIn = findViewById(R.id.channelIn);
        clientIn = findViewById(R.id.clientIn);
        serviceIn = findViewById(R.id.serviceIn);
        queueIn = findViewById(R.id.queueIn);

        switchQueue = findViewById(R.id.switchQueue);
        calculateButton = findViewById(R.id.calculateButton);

        systemLoadView = findViewById(R.id.systemLoadView);

        isQueueExists = false;

        //Neutral color
        ColorStateList neutralColorSL = queueIn.getBackgroundTintList();
        ColorStateList whiteColorSL = clientIn.getBackgroundTintList();


        //Switch button listener
        switchQueue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    queueIn.setBackgroundTintList(whiteColorSL);
                    queueIn.setEnabled(true);
                    isQueueExists = true;
                } else {
                    queueIn.setBackgroundTintList(neutralColorSL);
                    queueIn.setEnabled(false);
                    isQueueExists = false;
                }
            }
        });


        //Calculate Button listener
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}