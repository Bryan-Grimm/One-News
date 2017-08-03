package edu.fsu.cs.mobile.pro2;


import android.net.Uri;
import android.os.AsyncTask;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Weather {

    private String location;
    private Exception error;
    private Weathercb cb;
    private String temp;
    private String condition;

    public Weather(Weathercb c) {
        this.cb = c;
    }

    public String getLocation() {
        return location;
    }
    public String getTemp(){
        return temp;
    }
    public String getCond(){
        return condition;
    }

    public void refreshWeather(String l) {
        this.location = l;

        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {

                //YQL request to Yahoo weather api

                String YQL = String.format("select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"%s\")", strings[0]);

                String endpoint = String.format("https://query.yahooapis.com/v1/public/yql?q=%s&format=json", Uri.encode(YQL));

                try {
                    URL url = new URL(endpoint);

                    URLConnection connection = url.openConnection();

                    InputStream inputStream = connection.getInputStream();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    return result.toString();

                } catch (Exception e) {
                    error = e;
                }

                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                 if (s == null && error != null) {
                    cb.SF(error);
                    return;
                }

                try {
                    JSONObject data = new JSONObject(s);

                    JSONObject data1 = data.optJSONObject("query");

                    int count = data1.optInt("count");

                    if (count == 0) {
                        cb.SF(new LocationWeatherException("Nothing found"));
                        return;
                    }


                    JSONObject data2;
                    JSONObject data3;
                    JSONObject data4;
                    JSONObject data5;

                    try {

                        data1 = data.getJSONObject("query");
                        data2 = data1.getJSONObject("results");
                        data3 = data2.getJSONObject("channel");

                        data4 = data3.getJSONObject("item");
                        data5 = data4.getJSONObject("condition");
                        temp = data5.getString("temp");
                        condition = data5.getString("text");
                        cb.SS();
                    }
                    catch(JSONException e) {
                        e.printStackTrace();
                    }

                } catch (JSONException e) {
                    cb.SF(e);
                }

            }

        }.execute(location);
    }

    public class LocationWeatherException extends Exception {
        public LocationWeatherException(String detailMessage) {
            super(detailMessage);
        }
    }
}
