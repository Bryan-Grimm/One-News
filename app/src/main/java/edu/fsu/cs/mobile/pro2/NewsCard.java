package edu.fsu.cs.mobile.pro2;

import android.support.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

public class NewsCard extends MyCard {

    public static String TITLE_TAG = "title";
    public static String DESCR_TAG = "description";
    public static String URL_TAG = "url";


    public NewsCard() {}
    public NewsCard(String t, String d, String nURL, String z)
    {
        super(t,d,nURL,z);
    }

    @Nullable
    public static NewsCard fromJSON(JSONObject jsonObject)
    {
        try {
            String t = jsonObject.getString(TITLE_TAG);
            String d = jsonObject.getString(DESCR_TAG);
            String su = jsonObject.getString(URL_TAG);
            String z = jsonObject.getString("urlToImage");
            return new NewsCard(t,d,su, z);
        }
        catch(JSONException e) {
        }
        return null;
    }

}
