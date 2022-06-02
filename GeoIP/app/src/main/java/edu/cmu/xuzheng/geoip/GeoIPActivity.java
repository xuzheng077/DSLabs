package edu.cmu.xuzheng.geoip;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GeoIPActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final GeoIPActivity ma = this;

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
                GetIPInfo getIPInfo = new GetIPInfo();
                getIPInfo.lookup(ip, ma);
            }
        });

        Button reset = (Button) findViewById(R.id.reset_button);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetResults();
            }
        });
    }

    public void IpInfoReady(IpInfoResult result) {
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
        LinearLayout results = (LinearLayout) findViewById(R.id.results);

        if (result != null) {
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
            results.setVisibility(View.VISIBLE);
            countryFlag.invalidate();
        } else {
            setErrorMessage(R.string.fail_to_retrieve);
        }
        ipInput.setText("");
    }

    private void resetResults() {
        LinearLayout results = (LinearLayout) findViewById(R.id.results);
        results.setVisibility(View.INVISIBLE);
        TextView error = (TextView) findViewById(R.id.error);
        error.setText("");
        error.setVisibility(View.INVISIBLE);
    }

    private void setErrorMessage(int errorId) {
        TextView error = (TextView) findViewById(R.id.error);
        error.setText(errorId);
        error.setVisibility(View.VISIBLE);
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

}