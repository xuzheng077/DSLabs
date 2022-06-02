package edu.cmu.xuzheng.project4android;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author Xu Zheng
 * Andrew ID: xuzheng
 * <p>
 * This is the main activity for my application
 */
public class GeoIPActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //reference to self
        final GeoIPActivity ma = this;

        //set onClick for submit button
        Button submit = (Button) findViewById(R.id.submit_button);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ip = ((EditText) findViewById(R.id.ip_input)).getText().toString();
                resetResults();
                if (!validateIP(ip)) {
                    setErrorMessage(R.string.invalid_ip);
                    return;
                }
                GeoIPInfo getIPInfo = new GeoIPInfo();
                getIPInfo.lookup(ip, ma);
            }
        });

        //set onClick for reset button
        Button reset = (Button) findViewById(R.id.reset_button);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetResults();
            }
        });
    }

    /**
     * this method is called when response from webservice is ready
     *
     * @param result response
     */
    public void IpInfoReady(IPInfoResult result) {
        //find all the text and image view
        TextView ipInput = (EditText) findViewById(R.id.ip_input);
        TextView continent = (TextView) findViewById(R.id.continent);
        TextView country = (TextView) findViewById(R.id.country);
        TextView countryCapital = (TextView) findViewById(R.id.country_capital);
        ImageView countryFlag = (ImageView) findViewById(R.id.country_flag);
        TextView state = (TextView) findViewById(R.id.state_prov);
        TextView city = (TextView) findViewById(R.id.city);
        TextView district = (TextView) findViewById(R.id.district);
        TextView zipcode = (TextView) findViewById(R.id.zipcode);
        TextView isp = (TextView) findViewById(R.id.isp);
        TextView organization = (TextView) findViewById(R.id.organization);
        TextView latitude = (TextView) findViewById(R.id.latitude);
        TextView longitude = (TextView) findViewById(R.id.longitude);
        TableLayout results = (TableLayout) findViewById(R.id.results);

        //if result is not null
        if (result != null) {
            //set all the text and image views
            continent.setText(result.getContinent());
            country.setText(result.getCountry());
            countryCapital.setText(result.getCountryCapital());
            countryFlag.setImageBitmap(result.getCountryFlag());
            state.setText(result.getState());
            city.setText(result.getCity());
            district.setText(result.getDistrict());
            zipcode.setText(result.getZipcode());
            isp.setText(result.getIsp());
            organization.setText(result.getOrganization());
            latitude.setText(result.getLatitude());
            longitude.setText(result.getLongitude());
            //set the result area visibla
            results.setVisibility(View.VISIBLE);
            countryFlag.invalidate();
        } else {
            //set error message
            setErrorMessage(R.string.fail_to_retrieve);
        }
        //clear the input
        ipInput.setText("");
    }

    /**
     * this method clear the result area by setting them invisible
     */
    private void resetResults() {
        TableLayout results = (TableLayout) findViewById(R.id.results);
        results.setVisibility(View.INVISIBLE);
        TextView error = (TextView) findViewById(R.id.error);
        error.setText("");
        error.setVisibility(View.INVISIBLE);
    }

    /**
     * this method set the error message
     *
     * @param errorId
     */
    private void setErrorMessage(int errorId) {
        TextView error = (TextView) findViewById(R.id.error);
        error.setText(errorId);
        error.setVisibility(View.VISIBLE);
    }

    /**
     * cited from https://stackoverflow.com/questions/4581877/validating-ipv4-string-in-java
     *
     * @param ip ip input
     * @return true if valid, false otherwise
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

}