package edu.fsu.cs.mobile.pro2;

import org.json.JSONObject;


public class MyCard {
    public String title;
    public String description;
    public String url;
    public String image;

    MyCard() {}
    MyCard(String t, String d, String u, String z) {
        title = t;
        description = d;
        url = u;
        image = z;
    }

    static MyCard fromJSON(JSONObject j) {
        return new MyCard();
    }
}
