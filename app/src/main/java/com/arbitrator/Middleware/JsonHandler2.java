package com.arbitrator.Middleware;


import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class JsonHandler2 extends AsyncTask<Helper, Void, JSONArray> {


    public String url;
    public int c;
    public String arr[][];


    private String TAG = this.getClass().getSimpleName();


    @Override
    protected JSONArray doInBackground(Helper... helpers) {

        url = helpers[0].url;
        c = helpers[0].c;
        arr = helpers[0].arr;

        HttpHandler hh = new HttpHandler();
        JSONObject jsonObj = null;
        JSONArray jsonArr = null;

        if (c == 1) {
            String jsonStr = hh.makeServiceCallget(url);

            if (jsonStr != null) {
                try {
                    jsonArr = new JSONArray(jsonStr);
                    jsonObj = (JSONObject) (jsonArr.get(0));
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                }
            } else {
                Log.e(TAG, "Couldn't get json from Server.");
            }
        }
        return jsonArr;
    }

    @Override
    protected void onPostExecute(JSONArray aVoid) {
        super.onPostExecute(aVoid);
    }

}