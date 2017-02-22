package org.artek.app.main;


import android.util.Log;

import org.artek.app.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NewsJSONParser {

    public List<HashMap<String, String>> parse(JSONObject jObject) {

        JSONArray jArrayNews = null;
        try {
            jArrayNews = jObject.getJSONArray("response");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getArrayNews(jArrayNews);
    }

    private List<HashMap<String, String>> getArrayNews(JSONArray jArrayNews) {
        int arrayNewsCount = jArrayNews.length();
        List<HashMap<String, String>> arrayNewsList = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> news = null;

        for (int i = 0; i < arrayNewsCount; i++) {
            try {
                if (i == 0) {
                    i++;
                }

                news = getNews((JSONObject) jArrayNews.get(i));
                arrayNewsList.add(news);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return arrayNewsList;
    }

    private HashMap<String, String> getNews(JSONObject jNews) {

        HashMap<String, String> country = new HashMap<String, String>();

        String textName = "";
        String imageLogo = "";

        try {
            String tempName;
            tempName = jNews.getString("text");
            tempName = tempName.replaceAll("<br>", " ");
            textName = tempName;

            try {
                imageLogo = jNews.getJSONObject("attachment")
                        .getJSONObject("photo").getString("src_big");
            } catch (Exception e) {
                imageLogo = "http://ufland.moy.su/camera_a.gif";
            }

            country.put("likes",jNews.getJSONObject("likes").getString("count"));
            country.put("reposts",jNews.getJSONObject("reposts").getString("count"));
            country.put("text", textName);
            country.put("img", imageLogo);
            country.put("id",jNews.getString("id"));


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return country;
    }
}