package edu.self.weatherapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class WeatherInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_info);
        Intent intent = getIntent();
        String Area = intent.getStringExtra("Area");
        Toast.makeText(getApplicationContext(),Area,Toast.LENGTH_LONG).show();

        TextView tvCityName = (TextView) findViewById(R.id.tvCityName);
        TextView tvWeatherTelop = (TextView) findViewById(R.id.tvWeatherTelop);
        TextView tvWeatherDesc = (TextView) findViewById(R.id.tvWeatherDesc);

        WeatherInfoReceiver reciever = new WeatherInfoReceiver(Area, tvCityName, tvWeatherTelop, tvWeatherDesc);
        reciever.execute("130010");

    }

    public void onBackButtonClick(View view){
        finish();
    }

    private class WeatherInfoReceiver extends AsyncTask<String,String,String>{
        private String _cityName;
        private TextView _tvCityName;
        private TextView _tvWeatherTelop;
        private TextView _tvWeatherDesc;
        public WeatherInfoReceiver(String cityName, TextView tvCityName, TextView tvWeatherTelop, TextView tvWeatherDesc) {
            _cityName = cityName;
            _tvCityName = tvCityName;
            _tvWeatherTelop = tvWeatherTelop;
            _tvWeatherDesc = tvWeatherDesc;
        }

        public String doInBackground(String... params){

            String id = params[0];
            String urlStr = "http://weather.livedoor.com/forecast/webservice/json/v1?city=" + id;
            HttpURLConnection con = null;
            InputStream is = null;
            String result = "";

            try{
                URL url = new URL(urlStr);
                con = (HttpURLConnection)url.openConnection();
                con.setRequestMethod("GET");
                con.connect();
                is = con.getInputStream();
                result = IOUtils.toString(is, StandardCharsets.UTF_8);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if(con != null){
                    con.disconnect();
                }
                if(is != null){
                    try{
                        is.close();
                    }
                    catch (IOException e){

                    }
                }
            }

            return result;

        }

        public void onPostExecute(String result){

            String desc = "";
            String dateLabel = "";
            String telop = "";
            try {
                JSONObject rootJSON = new JSONObject(result);
                JSONObject descriptionJSON = rootJSON.getJSONObject("description");
                desc = descriptionJSON.getString("text");
                JSONArray forecasts = rootJSON.getJSONArray("forecasts");
                JSONObject forecastNow = forecasts.getJSONObject(0);
                dateLabel = forecastNow.getString("dateLabel");
                telop = forecastNow.getString("telop");
            }
            catch(JSONException ex) {
            }
            _tvCityName.setText(_cityName + "の" + dateLabel + "の天気: ");
            _tvWeatherTelop.setText(telop);
            _tvWeatherDesc.setText(desc);

        }
    }
}
