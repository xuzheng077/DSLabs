package edu.cmu.xuzheng.geoipwebservice;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Pattern;

/**
 * @author Xu Zheng
 * @description
 */
public class GeoInfoModel {

    public String lookupIp(String ip) {
        if (!validateIP(ip)) {
            return "{\"message\":\"Illegal Input\"}";
        }

        String ipInfoUrl = "https://api.ipgeolocation.io/ipgeo?apiKey=7725a84ed7114cfcbaaf639ecdd24ee6&ip=" + ip;
        String response = fetch(ipInfoUrl);
        if (response == null) {
            //third-party api unavailable
            return "{\"message\":\"API unavailable\"}";
        }
        JSONObject result = new JSONObject();
        try {
            JSONObject object = new JSONObject(response);
            result.put("message", "success");
            result.put("continent", object.getString("continent_name"));
            result.put("country", object.getString("country_name"));
            result.put("countryCapital", object.getString("country_capital"));
            result.put("countryFlagUrl", object.getString("country_flag"));
            result.put("state", object.getString("state_prov"));
            result.put("city", object.getString("city"));
            result.put("district", object.getString("district"));
            result.put("zipcode", object.getString("zipcode"));
            result.put("isp", object.getString("isp"));
            result.put("organization", object.getString("organization"));
            result.put("latitude", object.getString("latitude"));
            result.put("longitude", object.getString("longitude"));
        } catch (JSONException e) {
            System.out.println("JSONException");
            e.printStackTrace();
            return "{\"message\":\"API invalid data\"}";
        }
        return result.toString();
    }

    /**
     * cited from https://stackoverflow.com/questions/4581877/validating-ipv4-string-in-java
     *
     * @param ip
     * @return
     */
    private boolean validateIP(String ip) {
        try {
            if (ip == null || ip.isEmpty()) {
                return false;
            }

            String[] parts = ip.split("\\.");
            if (parts.length != 4) {
                return false;
            }

            for (String s : parts) {
                int i = Integer.parseInt(s);
                if ((i < 0) || (i > 255)) {
                    return false;
                }
            }
            if (ip.endsWith(".")) {
                return false;
            }
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    /*
     * Make an HTTP request to a given URL
     * cited from InterestingPicture example from class
     *
     * @param urlString The URL of the request
     * @return A string of the response from the HTTP GET.  This is identical
     * to what would be returned from using curl on the command line.
     */
    private static String fetch(String urlString) {
        StringBuilder response = new StringBuilder();
        try {
            URL url = new URL(urlString);
            /*
             * Create an HttpURLConnection.
             */
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // Read all the text returned by the server
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String str;
            // Read each line of "in" until done, adding each to "response"
            while ((str = in.readLine()) != null) {
                // str is one line of text readLine() strips newline characters
                response.append(str);
            }
            in.close();
        } catch (IOException e) {
            System.out.println("IOException");
            return null;
        }
        return response.toString();
    }
}
