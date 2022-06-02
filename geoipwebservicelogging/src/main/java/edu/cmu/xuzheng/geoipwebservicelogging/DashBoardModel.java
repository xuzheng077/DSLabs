package edu.cmu.xuzheng.geoipwebservicelogging;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.json.JSONObject;

import java.util.*;

/**
 * @author Xu Zheng
 * @description
 */
public class DashBoardModel {
    public List<JSONObject> getAllLogs() {
        MongoCollection<Document> collection = MongoUtils.getLoggingCollection();
        List<JSONObject> logs = new ArrayList<>();
        for (Document document : collection.find()) {
            JSONObject jsonObject = new JSONObject(document.toJson());
            logs.add(jsonObject);
        }
        return logs;
    }

    public AnalyticResult getAnalytics(List<JSONObject> logs) {
        AnalyticResult result = new AnalyticResult();
        long totalWebserviceLatency = 0L;
        long totalAPILatency = 0L;

        Map<String, Integer> stateMap = new HashMap<>();
        Map<String, Integer> deviceMap = new HashMap<>();
        for (JSONObject log : logs) {
            String state = extractState(log.getString("dataSentToAndroid"));
            if (stateMap.containsKey(state)) {
                stateMap.put(state, stateMap.get(state) + 1);
            } else {
                stateMap.put(state, 0);
            }
            String device = log.getString("userDevice");
            if (deviceMap.containsKey(device)) {
                deviceMap.put(device, deviceMap.get(device) + 1);
            } else {
                deviceMap.put(device, 0);
            }
            totalWebserviceLatency += (log.getLong("timeSentToAndroid") - log.getLong("timeReceivedFromAndroid"));
            totalAPILatency += (log.getLong("timeReceivedFromAPI") - log.getLong("timeSentToAPI"));
        }
        long averageWebserviceLatency = totalWebserviceLatency / logs.size();
        long averageAPILatency = totalAPILatency / logs.size();
        result.setAverageLatencyOfWebservice(averageWebserviceLatency);
        result.setAverageLatencyOf3rdAPI(averageAPILatency);
        result.setTopFiveStates(getTopFiveFromMapSortByValue(stateMap));
        result.setTopFiveUserDevices(getTopFiveFromMapSortByValue(deviceMap));
        result.setMostRecentFiveSearchedIP(getMostRecentFiveSearchedIP(logs));
        return result;
    }

    private String extractState(String data) {
        JSONObject jsonObject = new JSONObject(data);
        return jsonObject.getString("state");
    }

    private List<String> getMostRecentFiveSearchedIP(List<JSONObject> logs){
        Collections.sort(logs, new Comparator<JSONObject>(){
            @Override
            public int compare(JSONObject o1, JSONObject o2) {
                return -(Long.compare(o1.getLong("timeReceivedFromAndroid"), o2.getLong("timeReceivedFromAndroid")));
            }
        });
        int lengthRecentIP = Math.min(logs.size(), 5);
        List<String> mostRecentFiveIP = new ArrayList<>();
        for (int i = 0; i < lengthRecentIP; i++) {
            mostRecentFiveIP.add(logs.get(i).getString("paramIP"));
        }
        return mostRecentFiveIP;
    }

    private List<String> getTopFiveFromMapSortByValue(Map<String, Integer> map){
        List<Map.Entry<String, Integer>> entryList = new ArrayList<>(map.entrySet());
        Collections.sort(entryList, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o1.getValue() - o2.getValue();
            }
        });
        List<String> results = new ArrayList<>();
        int lengthDevice = Math.min(entryList.size(), 5);
        for (int i = 0; i < lengthDevice; i++) {
            results.add(entryList.get(i).getKey());
        }
        return results;
    }

}
