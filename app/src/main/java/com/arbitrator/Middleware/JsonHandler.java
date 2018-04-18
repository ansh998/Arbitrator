package com.arbitrator.Middleware;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class JsonHandler extends AsyncTask<Helper, Void, JSONObject> {


    public String url;
    public int c;
    public String arr[][];
    public Context con;
    ProgressDialog pD;


    private String TAG = this.getClass().getSimpleName();


    @Override
    protected JSONObject doInBackground(Helper... helpers) {

        url = helpers[0].url;
        c = helpers[0].c;
        arr = helpers[0].arr;
        con = helpers[0].con;

        //pD=ProgressDialog.show(con,"Working...",null);

        HttpHandler hh = new HttpHandler();
        JSONObject jsonObj = null;

        if (c == 1) {
            String jsonStr = hh.makeServiceCallget(url);

            if (jsonStr != null) {
                try {
                    JSONArray jsonArr = new JSONArray(jsonStr);
                    jsonObj = (JSONObject) (jsonArr.get(0));
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                }
            } else {
                Log.e(TAG, "Couldn't get json from Server.");
            }
        } else if (c == 2) {
            try {
                JSONObject obj = new JSONObject();
                for (int i = 0; i < arr.length; i++)
                    obj.put(arr[i][0], arr[i][1]);
                HttpHandler service = new HttpHandler();
                String jsonStr = service.makeServiceCallpost(url, obj);
                if (jsonStr != null) {
                    try {
                        JSONArray jsonArr = new JSONArray(jsonStr);
                        jsonObj = (JSONObject) (jsonArr.get(0));
                    } catch (final JSONException e) {
                        Log.e(TAG, "Json parsing error: " + e.getMessage());
                    }
                } else {
                    Log.e(TAG, "Couldn't get json from Server.");
                }
            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
            }
        } else if (c == 3) {
            String jsonStr = hh.makeServiceCalldelete(url);

            if (jsonStr != null) {
                try {
                    JSONArray jsonArr = new JSONArray(jsonStr);
                    jsonObj = (JSONObject) (jsonArr.get(0));
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                }
            } else {
                Log.e(TAG, "Couldn't get json from Server.");
            }
        } else if (c == 4) {
            try {
                JSONObject obj = new JSONObject();
                for (int i = 0; i < arr.length; i++)
                    obj.put(arr[i][0], arr[i][1]);
                HttpHandler service = new HttpHandler();
                String jsonStr = service.makeServiceCallput(url, obj);
                if (jsonStr != null) {
                    try {
                        JSONArray jsonArr = new JSONArray(jsonStr);
                        jsonObj = (JSONObject) (jsonArr.get(0));
                    } catch (final JSONException e) {
                        Log.e(TAG, "Json parsing error: " + e.getMessage());
                    }
                } else {
                    Log.e(TAG, "Couldn't get json from Server.");
                }
            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
            }
        }
        return jsonObj;
    }

    @Override
    protected void onPostExecute(JSONObject aVoid) {
        super.onPostExecute(aVoid);
        //pD.dismiss();
    }

    @Override
    protected void onPreExecute() {

    }
}