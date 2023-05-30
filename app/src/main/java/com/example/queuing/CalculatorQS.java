package com.example.queuing;
//Нужен обработчик ошибок. Например ввод некорректных данных, по типу отрицательных значений
//Нужно сменить некоторые поля на целочисленные
//Поменять формать вывода результатов в данном классе создав функцию вывода
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

    public String getServedApplications() {
        return String.format("%.3f", servedApplications);
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
        return String.format("%.3f", absoluteAbility);
    }

    private double systemLoad, loadPerChannel, downtimeProbability,
            failureProbability, numberOfApplications, waitingTime, servedApplications,
            serviceTime, appsInSystem, timeInSystem, relativeAbility, absoluteAbility;


    private int getFactorial(int k) {
        int factorial = 1;
        for (int i = 1; i <= k; i++) {
            factorial *= i;
        }
        System.out.println(factorial);
        return factorial;
    }

    //With unlimited queue
    public void calculate(int channel, double client, double service) {

        if(channel == 1) {
            systemLoad = client / service;
            loadPerChannel = systemLoad;
            downtimeProbability = 1 - systemLoad; //?What if SL > or = 0?
            failureProbability = 0;
            numberOfApplications = (loadPerChannel * loadPerChannel) / (1 - loadPerChannel);
            waitingTime = numberOfApplications / client;
            servedApplications = systemLoad;
            serviceTime = servedApplications / client;
            appsInSystem = systemLoad / (1 - systemLoad);
            timeInSystem = appsInSystem / client;
            relativeAbility = 1;
            absoluteAbility = client;
        } else { //Many channels and unlimited queue
            systemLoad = client / service;
            loadPerChannel = systemLoad / channel;
            downtimeProbability = getManyChannelsNoLimitQueueDowntimeProbability(channel, systemLoad, loadPerChannel);
            failureProbability = 0;
            numberOfApplications = getManyChannelsNoLimitNumberOfApplications(systemLoad, downtimeProbability, loadPerChannel, channel);
            waitingTime = numberOfApplications / client;
            servedApplications = absoluteAbility / service;
            serviceTime = servedApplications / client;  //Maybe sA/channel
            appsInSystem = numberOfApplications + servedApplications;
            timeInSystem = waitingTime + serviceTime;
            relativeAbility = 1;
            absoluteAbility = client;
        }
    }

    private double getManyChannelsNoLimitQueueDowntimeProbability(int channel, double systemLoad, double loadPerChannel) {
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

    private double getManyChannelsNoLimitNumberOfApplications(double systemLoad, double downtimeProbability, double loadPerChannel, int channel) {
        return (Math.pow(systemLoad, channel + 1) * downtimeProbability) / (channel * getFactorial(channel) * Math.pow(1 - loadPerChannel, 2));
    }


    //With limited queue
    public void calculate(int channel, double client, double service, int queue) {

        if(channel == 1 && queue == 0) { //One channel and no queue
            systemLoad = client / service;
            loadPerChannel = systemLoad;
            downtimeProbability = 1 / (1 + systemLoad);
            failureProbability = systemLoad / (1 + systemLoad);
            numberOfApplications = 0;
            waitingTime = relativeAbility / service;
            servedApplications = absoluteAbility / service;
            serviceTime = servedApplications / client;
            appsInSystem = systemLoad / (1 + systemLoad);
            timeInSystem = appsInSystem / client;
            relativeAbility = downtimeProbability;
            absoluteAbility = client * relativeAbility;
        } else if(channel == 1) {  //One channel and queue > 0
            systemLoad = client / service;
            loadPerChannel = systemLoad;
            downtimeProbability = (1 - systemLoad) / (1 - Math.pow(systemLoad, queue + 2));
            failureProbability = Math.pow(systemLoad, queue + 1) * downtimeProbability;
            numberOfApplications = Math.pow(systemLoad, 2) * downtimeProbability * ((1 - (queue + 1) * Math.pow(systemLoad, queue) + queue * Math.pow(systemLoad, queue + 1)) / Math.pow((1- systemLoad), 2));
            waitingTime = numberOfApplications / client;
            servedApplications = systemLoad * relativeAbility;
            serviceTime = servedApplications / client;
            appsInSystem = servedApplications + numberOfApplications;
            timeInSystem = appsInSystem / client;
            relativeAbility = 1 - Math.pow(systemLoad, queue + 1) * downtimeProbability;
            absoluteAbility = client * relativeAbility;
        } else if(queue == 0) { //Many channels and no queue
            systemLoad = client / service;
            loadPerChannel = systemLoad / channel;
            downtimeProbability = getManyChannelsNoQueueDowntimeProbability(channel, systemLoad);
            failureProbability = downtimeProbability * Math.pow(systemLoad, channel) / getFactorial(channel);
            numberOfApplications = 0;
            waitingTime = 0;
            servedApplications = absoluteAbility / service;
            serviceTime = servedApplications / channel;
            appsInSystem = servedApplications;
            timeInSystem = appsInSystem / client;
            relativeAbility = 1 - failureProbability;
            absoluteAbility = client * relativeAbility;
        } else { //Many channels and queue > 0
            systemLoad = client / service;
            loadPerChannel = systemLoad / channel;
            downtimeProbability = getManyChannelsLimitQueueDowntimeProbability(channel, systemLoad, loadPerChannel, queue);
            failureProbability = (Math.pow(systemLoad, channel + queue) * downtimeProbability) / (Math.pow(channel, queue) * getFactorial(channel));
            numberOfApplications = getManyChannelQueueNumberOfApplications(channel, systemLoad, downtimeProbability, queue, loadPerChannel);
            waitingTime = numberOfApplications / client;
            servedApplications = absoluteAbility / service;
            serviceTime = servedApplications / client;
            appsInSystem =  numberOfApplications + servedApplications;
            timeInSystem = appsInSystem / client;
            relativeAbility = 1 - failureProbability;
            absoluteAbility = client * relativeAbility;
        }
    }

    private double getManyChannelsNoQueueDowntimeProbability(int channel, double systemLoad) {
        double answer = 0;
        for (int k = 0; k <= channel; k++) {
            answer += (Math.pow(systemLoad, k) / getFactorial(k));
        }
        if(answer < 0) {
            answer = -answer;
        }
        answer = Math.pow(answer, -1);
        return answer;
    }

    private double getManyChannelsLimitQueueDowntimeProbability(int channel, double systemLoad, double loadPerChannel, int queue) {
        double answer = 0;
        for (int k = 0; k <= channel; k++) {
            answer += (Math.pow(systemLoad, k) / getFactorial(k));
        }
        answer += (Math.pow(systemLoad, channel) * loadPerChannel * (1 - Math.pow(loadPerChannel, queue))) / (getFactorial(channel) * (1 - loadPerChannel));
        if(answer < 0) {
            answer = -answer;
        }
        answer = Math.pow(answer, -1);
        return answer;
    }

    double getManyChannelQueueNumberOfApplications(int channel, double systemLoad, double downtimeProbability, int queue, double loadPerChannel) {
        double answer;
        answer = (Math.pow(systemLoad, channel + 1) * downtimeProbability) / (channel * getFactorial(channel));
        answer *= (1 - (queue + 1) * Math.pow(loadPerChannel, queue) + queue * Math.pow(loadPerChannel, queue + 1)) / (Math.pow(1 - loadPerChannel, 2));
        return  answer;
    }
}
