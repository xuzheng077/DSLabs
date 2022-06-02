package edu.cmu.xuzheng.geoip;

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

public class GetIPInfo {
    GeoIPActivity geoIPActivity = null;


    public void lookup(String ip, GeoIPActivity geoIPActivity) {
        this.geoIPActivity = geoIPActivity;
        new AsyncLookUpIp().execute(ip);
    }

    private class AsyncLookUpIp extends AsyncTask<String, Void, IpInfoResult> {

        @Override
        protected IpInfoResult doInBackground(String... strings) {
            return lookupIp(strings[0]);
        }

        @Override
        protected void onPostExecute(IpInfoResult ipInfoResult) {
            geoIPActivity.IpInfoReady(ipInfoResult);
        }

        private IpInfoResult lookupIp(String ip) {
            IpInfoResult result = null;
            try {
                URL url = new URL("https://frozen-lowlands-01360.herokuapp.com//GetIPGeoInfo?ip=" + ip);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                // sending plain text
                connection.setRequestProperty("Content-Type", "text/plain; charset=utf-8");
                // receiving plain text
                connection.setRequestProperty("Accept", "text/plain");
                int status = connection.getResponseCode();
                System.out.println("status code: "+status);
                if (status == 200) {
                    //get response body
                    String response = getResponse(connection);
                    //parse response
                    result = parseResponse(response);
                }
                connection.disconnect();
            } catch (MalformedURLException e) {
                System.out.println("URL exception");
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("IOException");
                e.printStackTrace();
            } catch(Exception e){
                System.out.println("Exception");
                e.printStackTrace();
            }
            return result;
        }

        private String getResponse(HttpURLConnection connection) {
            StringBuilder response = new StringBuilder();
            try {
                String output = "";
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

        private IpInfoResult parseResponse(String response) {
            Gson gson = new Gson();
            IpInfoResult result = gson.fromJson(response, IpInfoResult.class);
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
