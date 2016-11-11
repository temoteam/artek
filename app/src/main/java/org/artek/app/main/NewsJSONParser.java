package org.artek.app.main;


import org.artek.app.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NewsJSONParser {

    public List<HashMap<String, Object>> parse(JSONObject jObject) {

        JSONArray jArrayNews = null;
        try {
            jArrayNews = jObject.getJSONArray("response");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getArrayNews(jArrayNews);
    }

    private List<HashMap<String, Object>> getArrayNews(JSONArray jArrayNews) {
        int arrayNewsCount = jArrayNews.length();
        List<HashMap<String, Object>> arrayNewsList = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> news = null;

        for (int i = 0; i < arrayNewsCount; i++) {
            try {
                if (i == 0) {
                    System.out.println("The transition towards the next object"
                            + i);
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

    private HashMap<String, Object> getNews(JSONObject jNews) {

        HashMap<String, Object> country = new HashMap<String, Object>();
        String textName = "";
        String imageLogo = "";
        String likes = "";
        String reposts = "";

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
            likes = jNews.getJSONObject("likes").getString("count");
            reposts = jNews.getJSONObject("reposts").getString("count");
            String details = "Share: " + reposts + "                Like: "
                    + likes;

            country.put("text", textName);
            country.put("imageLogo", R.drawable.logo);
            country.put("imageLogo_path", imageLogo);
            country.put("details", details);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return country;
    }
}