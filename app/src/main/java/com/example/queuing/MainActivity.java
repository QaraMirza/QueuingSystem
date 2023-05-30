package com.example.queuing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText channelIn, clientIn, serviceIn, queueIn;
    private double client, service;

    private int queue, channel;
    private Switch switchQueue;
    private Button calculateButton;
    private TextView systemLoadView, loadPerChannelView, downtimeProbabilityView,
            failureProbabilityView, numberOfApplicationsView, waitingTimeView, servedCustomersView,
            serviceTimeView, appsInSystemView, timeInSystemView, relativeAbilityView, absoluteAbilityView;
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
        loadPerChannelView = findViewById(R.id.loadPerChannelView);
        downtimeProbabilityView = findViewById(R.id.downtimeProbabilityView);
        failureProbabilityView = findViewById(R.id.failureProbabilityView);
        numberOfApplicationsView = findViewById(R.id.numberOfApplicationsView);
        waitingTimeView = findViewById(R.id.waitingTimeView);
        servedCustomersView = findViewById(R.id.servedCustomersView);
        serviceTimeView = findViewById(R.id.serviceTimeView);
        appsInSystemView = findViewById(R.id.appsInSystemView);
        timeInSystemView = findViewById(R.id.timeInSystemView);
        relativeAbilityView = findViewById(R.id.relativeAbilityView);
        absoluteAbilityView = findViewById(R.id.absoluteAbilityView);

        isQueueExists = false;

        //Colors
        ColorStateList neutralColorSL = queueIn.getBackgroundTintList();
        ColorStateList whiteColorSL = clientIn.getBackgroundTintList();

        CalculatorQS calculator = new CalculatorQS();

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
                if(!isQueueExists) {
                    channel = Integer.parseInt(String.valueOf(channelIn.getText()));
                    client = Double.parseDouble(String.valueOf(clientIn.getText()));
                    service = Double.parseDouble(String.valueOf(serviceIn.getText()));
                    calculator.calculate(channel, client, service);
                } else {
                    channel = Integer.parseInt(String.valueOf(channelIn.getText()));
                    client = Double.parseDouble(String.valueOf(clientIn.getText()));
                    service = Double.parseDouble(String.valueOf(serviceIn.getText()));
                    queue = Integer.parseInt(String.valueOf(queueIn.getText()));
                    calculator.calculate(channel, client, service, queue);
                }
                systemLoadView.setText(calculator.getSystemLoad());
                loadPerChannelView.setText(calculator.getLoadPerChannel());
                downtimeProbabilityView.setText(calculator.getDowntimeProbability());
                failureProbabilityView.setText(calculator.getFailureProbability());
                numberOfApplicationsView.setText(calculator.getNumberOfApplications());
                waitingTimeView.setText(calculator.getWaitingTime());
                servedCustomersView.setText(calculator.getServedApplications());
                serviceTimeView.setText(calculator.getServiceTime());
                appsInSystemView.setText(calculator.getAppsInSystem());
                timeInSystemView.setText(calculator.getTimeInSystem());
                relativeAbilityView.setText(calculator.getRelativeAbility());
                absoluteAbilityView.setText(calculator.getAbsoluteAbility());

            }
        });

    }
}