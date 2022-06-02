package edu.cmu.xuzheng.geoipwebservicelogging;

import java.util.List;

/**
 * @author Xu Zheng
 * @description
 */
public class AnalyticResult {
    /**
     * 10 mostly recently searched ip
     */
    private List<String> mostRecentFiveSearchedIP;
    /**
     * top 5 states where the searched ip belongs
     */
    private List<String> topFiveStates;
    /**
     * top 5 user device models
     */
    private List<String> topFiveUserDevices;
    /**
     * average latency of my webservice
     */
    private double averageLatencyOfWebservice;
    /**
     * average latency of 3rd api
     */
    private double averageLatencyOf3rdAPI;


    public List<String> getMostRecentFiveSearchedIP() {
        return mostRecentFiveSearchedIP;
    }

    public void setMostRecentFiveSearchedIP(List<String> mostRecentFiveSearchedIP) {
        this.mostRecentFiveSearchedIP = mostRecentFiveSearchedIP;
    }

    public List<String> getTopFiveStates() {
        return topFiveStates;
    }

    public void setTopFiveStates(List<String> topFiveStates) {
        this.topFiveStates = topFiveStates;
    }

    public List<String> getTopFiveUserDevices() {
        return topFiveUserDevices;
    }

    public void setTopFiveUserDevices(List<String> topFiveUserDevices) {
        this.topFiveUserDevices = topFiveUserDevices;
    }

    public double getAverageLatencyOfWebservice() {
        return averageLatencyOfWebservice;
    }

    public void setAverageLatencyOfWebservice(double averageLatencyOfWebservice) {
        this.averageLatencyOfWebservice = averageLatencyOfWebservice;
    }

    public double getAverageLatencyOf3rdAPI() {
        return averageLatencyOf3rdAPI;
    }

    public void setAverageLatencyOf3rdAPI(double averageLatencyOf3rdAPI) {
        this.averageLatencyOf3rdAPI = averageLatencyOf3rdAPI;
    }
}
