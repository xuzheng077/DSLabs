package edu.cmu.xuzheng.project4android;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author Xu Zheng
 * Andrew ID: xuzheng
 * <p>
 * Note that on line 67 is the commented url for task 1 deployed webservice if task 2 is not working
 * This class is responsible for requesting the webservice
 */
public class GeoIPInfo {
    GeoIPActivity geoIPActivity = null;

    /**
     * this method create a seperate thread to make a request to web service
     *
     * @param ip
     * @param geoIPActivity
     */
    public void lookup(String ip, GeoIPActivity geoIPActivity) {
        this.geoIPActivity = geoIPActivity;
        //async task
        new AsyncLookUpIp().execute(ip);
    }

    private class AsyncLookUpIp extends AsyncTask<String, Void, IPInfoResult> {

        @Override
        protected IPInfoResult doInBackground(String... strings) {
            //search the ip
            return lookupIp(strings[0]);
        }

        @Override
        protected void onPostExecute(IPInfoResult ipInfoResult) {
            //display results
            geoIPActivity.IpInfoReady(ipInfoResult);
        }

        /**
         * this method looks up the ip using the web service
         *
         * @param ip ip input
         * @return response
         */
        private IPInfoResult lookupIp(String ip) {
            IPInfoResult result = null;
            try {
                //URL for task 1
                //URL url = new URL("https://nameless-bayou-70205.herokuapp.com/GetIPGeoInfo?ip=" + ip);
                //URL for task 2
                URL url = new URL("https://immense-chamber-89415.herokuapp.com/GetIPGeoInfo?ip=" + ip);
                //URL for local
                //URL url = new URL("http://10.0.2.2:8080/Project4Task2-1.0-SNAPSHOT/GetIPGeoInfo?ip=" + ip);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                // sending plain text
                connection.setRequestProperty("Content-Type", "text/plain; charset=utf-8");
                // receiving plain text
                connection.setRequestProperty("Accept", "text/plain");
                connection.setConnectTimeout(1000);
                connection.setReadTimeout(1000);
                int status = connection.getResponseCode();
                System.out.println("status code: " + status);
                if (status == 200) {
                    //get response body
                    String response = getResponse(connection);

                    //parse response
                    result = parseResponse(response);
                }
                connection.disconnect();
            } catch (MalformedURLException e) {
                System.out.println("URL exception");
            } catch (IOException e) {
                System.out.println("IOException");
            } catch (Exception e) {
                System.out.println("Exception");
            }
            return result;
        }

        /**
         * this method get the response JSON
         *
         * @param connection HttpURLConnection
         * @return JSON response
         */
        private String getResponse(HttpURLConnection connection) {
            //use a StringBuilder
            StringBuilder response = new StringBuilder();
            try {
                String output = "";
                //read the response line by line
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        (connection.getInputStream())));
                while ((output = br.readLine()) != null) {
                    response.append(output);
                }
            } catch (IOException e) {
                System.out.println("IOException");
                e.printStackTrace();
            }
            return response.toString();
        }

        /**
         * this method parse the response JSON
         *
         * @param response JSON
         * @return IPInfoResult
         */
        private IPInfoResult parseResponse(String response) {
            Gson gson = new Gson();
            //parse the response
            IPInfoResult result = gson.fromJson(response, IPInfoResult.class);
            if (!result.getMessage().equals("success")) {
                return null;
            }
            //get the image bitmap and set
            result.setCountryFlag(getRemoteImage(result.getCountryFlagUrl()));
            return result;
        }

        /*
         * Given a URL referring to an image, return a bitmap of that image
         * Cited from AndroidInterestingPicture example from class
         */
        @RequiresApi(api = Build.VERSION_CODES.P)
        private Bitmap getRemoteImage(String urlStr) {
            try {
                URL url = new URL(urlStr);
                URLConnection conn = url.openConnection();
                conn.connect();
                BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
                Bitmap bm = BitmapFactory.decodeStream(bis);
                return bm;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
