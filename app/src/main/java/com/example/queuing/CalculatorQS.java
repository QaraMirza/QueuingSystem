package com.example.queuing;

public class CalculatorQS {

    public String getSystemLoad() {
        return String.format("%.3f", systemLoad);
    }

    public String getLoadPerChannel() {
        return String.format("%.3f", loadPerChannel);
    }

    public String getDowntimeProbability() {
        return String.format("%.3f", downtimeProbability);
    }

    public String getFailureProbability() {
        return String.format("%.3f", failureProbability);
    }

    public String getNumberOfApplications() {
        return String.format("%.3f", numberOfApplications);
    }

    public String getWaitingTime() {
        return String.format("%.3f", waitingTime);
    }

    public String getServedCustomers() {
        return String.format("%.3f", servedCustomers);
    }

    public String getServiceTime() {
        return String.format("%.3f", serviceTime);
    }

    public String getAppsInSystem() {
        return String.format("%.3f", appsInSystem);
    }

    public String getTimeInSystem() {
        return String.format("%.3f", timeInSystem);
    }

    public String getRelativeAbility() {
        return String.format("%.3f", relativeAbility);
    }

    public String getAbsoluteAbility() {
        return String.format("%.3f", AbsoluteAbility);
    }

    private double systemLoad, loadPerChannel, downtimeProbability,
            failureProbability, numberOfApplications, waitingTime, servedCustomers,
            serviceTime, appsInSystem, timeInSystem, relativeAbility, AbsoluteAbility;

    //With unlimited queue
    public void calculate(int channel, double client, double service) {

        if(channel == 1) {
            systemLoad = client / service;
            loadPerChannel = systemLoad;
            downtimeProbability = 1 - systemLoad; //?What if SL > or = 0?
            failureProbability = 0;
            numberOfApplications = (loadPerChannel * loadPerChannel) / (1 - loadPerChannel);
            waitingTime = numberOfApplications / client;
            servedCustomers = systemLoad;
            serviceTime = servedCustomers / client;
            appsInSystem = systemLoad / (1 - systemLoad);
            timeInSystem = appsInSystem / client;
            relativeAbility = 1;
            AbsoluteAbility = client;
        } else { //Many channels and unlimited queue
            systemLoad = client / service;
            loadPerChannel = systemLoad / channel;
            downtimeProbability = manyChnDtProp(channel, systemLoad, loadPerChannel);
            failureProbability = 0;
            numberOfApplications = manyChNumOfApps(systemLoad, downtimeProbability, loadPerChannel, channel);
            waitingTime = numberOfApplications / client;
            servedCustomers = AbsoluteAbility / service;
            serviceTime = servedCustomers / client;
            appsInSystem = numberOfApplications + servedCustomers;
            timeInSystem = waitingTime + serviceTime;
            relativeAbility = 1;
            AbsoluteAbility = client;
        }
    }

    private double manyChNumOfApps(double systemLoad, double downtimeProbability, double loadPerChannel, int channel) {
        return (Math.pow(systemLoad, channel + 1) * downtimeProbability) / (channel * getFactorial(channel) * Math.pow(1 - loadPerChannel, 2));
    }

    private double manyChnDtProp(int channel, double systemLoad, double loadPerChannel) {
        double answer = 0;
        for (int k = 0; k <= channel; k++) {
            answer += (Math.pow(systemLoad, k) / getFactorial(k));
        }
        answer += (Math.pow(systemLoad, channel) * loadPerChannel) / (getFactorial(channel) * (1 - loadPerChannel));
        if(answer < 0) {
            answer = -answer;
        }
        answer = Math.pow(answer, -1);
        return answer;
    }

    private int getFactorial(int k) {
        int factorial = 1;
        for (int i = 1; i <= k; i++) {
            factorial *= i;
        }
        return factorial;
    }


    //With limited queue
    public void calculate(int channel, double client, double service, int queue) {


    }
}
