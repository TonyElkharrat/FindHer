package com.example.zivug.Api;

import android.content.Context;
import android.widget.Toast;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.zivug.notifier.cityListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonParser
{
    private static cityListener listener;

    public static   void ParseJsonFromLongitudeAndLatitudeToCity(String Url, final Context context, final String latitude, final String longitude)
    {
        RequestQueue queue = Volley.newRequestQueue(context);
        final StringBuilder country = new StringBuilder();
        country.append("");
        StringRequest request = new StringRequest(Url+latitude+"+"+longitude+"&key=2ce41e2e9c5e498dab1e3c0d8318b77e", new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                try {

                    JSONObject rootObject = new JSONObject(response);

                    JSONArray resultArray = rootObject.getJSONArray("results");

                    JSONObject resultObject = resultArray.getJSONObject(0);

                    JSONObject cityObject = resultObject.getJSONObject("components");

                    String city = cityObject.getString("city");
                    listener.getLocation(city,latitude,longitude);

                }

                catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(context, "Sorry we Cannot find your city please verify that you activated the location", Toast.LENGTH_LONG).show();
            }
        });

        queue.add(request);
    }

    public static void SetListener(cityListener  i_listener)
    {
        listener = i_listener;
    }
}
