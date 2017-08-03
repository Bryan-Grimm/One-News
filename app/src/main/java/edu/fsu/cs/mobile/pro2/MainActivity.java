package edu.fsu.cs.mobile.pro2;

import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static edu.fsu.cs.mobile.pro2.LoginOptions.PREFS_NAME;

public class MainActivity extends AppCompatActivity implements Weathercb {
    private static final String NEWS_API_KEY = "d7bf025f075049248ea1bde65586aa23";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<MyCard> cardList;
    private String news_source;
    private String cat; //categories
    private Weather weather;
    TextView temp; // temperature
    TextView cond; //conditions

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv);
        SharedPreferences settings  = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        weather = new Weather(this);
        weather.refreshWeather("Tallahassee, FL"); //Async task to get weather


        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        cardList = new ArrayList<MyCard>();
        //get news source selected
        news_source =
                getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                        .getString("news_source", null);
        //get category selected
        cat = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .getString("cat",null );


        if (news_source != null) {
            new JSONAsyncTest().execute("https://newsapi.org/v1/articles?source=" + news_source + "&apiKey=" +
                    NEWS_API_KEY);
            settings.edit().putString("news_source", null).commit();
        }
        else if(cat != null){
            if(cat.equals("top")){
                new JSONAsyncTest().execute("https://newsapi.org/v1/articles?source=" + "google-news" + "&apiKey=" +
                        NEWS_API_KEY);
            }
            else if(cat.equals("business")){
                new JSONAsyncTest().execute("https://newsapi.org/v1/articles?source=" + "the-economist" + "&apiKey=" +
                        NEWS_API_KEY);
            }
            else if(cat.equals("entertainment")){
                new JSONAsyncTest().execute("https://newsapi.org/v1/articles?source=" + "buzzfeed" + "&apiKey=" +
                        NEWS_API_KEY);
            }
            else if(cat.equals("gaming")){
                new JSONAsyncTest().execute("https://newsapi.org/v1/articles?source=" + "polygon" + "&apiKey=" +
                        NEWS_API_KEY);
            }
            else if(cat.equals("music")){
                new JSONAsyncTest().execute("https://newsapi.org/v1/articles?source=" + "mtv-news" + "&apiKey=" +
                        NEWS_API_KEY);
            }
            else if(cat.equals("politics")){
                new JSONAsyncTest().execute("https://newsapi.org/v1/articles?source=" + "the-washington-post" + "&apiKey=" +
                        NEWS_API_KEY);
            }
            else if(cat.equals("science")){
                new JSONAsyncTest().execute("https://newsapi.org/v1/articles?source=" + "new-scientist" + "&apiKey=" +
                        NEWS_API_KEY);
            }
            else if(cat.equals("sport")){
                new JSONAsyncTest().execute("https://newsapi.org/v1/articles?source=" + "espn" + "&apiKey=" +
                        NEWS_API_KEY);
            }
            else if(cat.equals("technology")){
                new JSONAsyncTest().execute("https://newsapi.org/v1/articles?source=" + "techcrunch" + "&apiKey=" +
                        NEWS_API_KEY);
            }
            settings.edit().putString("cat", null).commit();

        }
        //if no news selected get top news from google
        else
            new JSONAsyncTest().execute("https://newsapi.org/v1/articles?source=" + "google-news" + "&apiKey=" +
                    NEWS_API_KEY);

        mAdapter = new MyCardAdapter(cardList);
        mRecyclerView.setAdapter(mAdapter);
    }
    @Override
    public void SS(){
        //when async task finishes update weather
        temp = (TextView)findViewById(R.id.temp);
        cond = (TextView)findViewById(R.id.cond);
        temp.setText(weather.getTemp() + "\u00B0" + " F");
        cond.setText(weather.getCond());

    }

    @Override
    public void SF(Exception ex) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
             startActivity(new Intent(MainActivity.this, LoginOptions.class));
        }

        return super.onOptionsItemSelected(item);
    }
    public class JSONAsyncTest extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            Log.i("in back", "going");
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                }
                return buffer.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }


        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);
            JSONObject res_json;
            try {
                res_json = new JSONObject(result);
                JSONArray articles = res_json.getJSONArray("articles");
                for (int i = 0; i < articles.length(); ++i) {
                    JSONObject j = articles.getJSONObject(i);
                    cardList.add(NewsCard.fromJSON(j));
                }
                mAdapter = new MyCardAdapter(cardList);
                mRecyclerView.setAdapter(mAdapter);
            }
            catch(JSONException e) {
                e.printStackTrace();
            }
        }
    }


}
