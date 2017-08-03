package edu.fsu.cs.mobile.pro2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;


public class LoginOptions extends AppCompatActivity{
    public static final String PREFS_NAME = "NEWS_PREFS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_login_options);

        Button update_button = (Button) findViewById(R.id.update_button);
        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginOptions.this, MainActivity.class);
                startActivity(i);
            }
        });
        Spinner spinner = (Spinner) findViewById(R.id.news_spinner);
        Spinner spinner1 = (Spinner)findViewById(R.id.cat);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                String news_select = (String)parent.getItemAtPosition(position);
                String news_source = null;
                switch(news_select) {
                    case "AP News": news_source = "associated-press"; break;
                    case "BBC Sport": news_source = "bbc-sport"; break;
                    case "Reuters": news_source = "reuters"; break;
                    case "ESPN": news_source = "espn"; break;
                    case "Ars Technica": news_source = "ars-technica"; break;

                }
                settings.edit().putString("news_source", news_source).commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

                String news_source = "none";
                switch(position) {
                    case 1: news_source = "top"; break;
                    case 2: news_source = "business"; break;
                    case 3: news_source = "entertainment"; break;
                    case 4: news_source = "gaming"; break;
                    case 5: news_source = "music"; break;
                    case 6: news_source = "politics"; break;
                    case 7: news_source = "science"; break;
                    case 8: news_source = "sport"; break;
                    case 9: news_source = "technology"; break;
                }
                settings.edit().putString("cat", news_source).commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

}
