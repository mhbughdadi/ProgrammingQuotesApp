package com.example.mohammed.programmingquotesapp.services;

import android.os.AsyncTask;
import android.util.Log;


import com.example.mohammed.programmingquotesapp.sync.remote.RemoteEndpointUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Mohammed on 25/07/2017.
 */

public class NetworkService extends AsyncTask <Void,Void,JSONArray>{
    private  final String  TAG=NetworkService.class.getSimpleName();

    @Override
    protected JSONArray doInBackground(Void... params) {
        JSONArray jsonArray= RemoteEndpointUtil.fetchJsonArray();
        return jsonArray;
    }

    @Override
    protected void onPostExecute(JSONArray jsonArray) {
        super.onPostExecute(jsonArray);
        for (int i=0;i<jsonArray.length();i++) {
            try {
                JSONObject jsonObj=jsonArray.getJSONObject(i);
                Log.d("data ",jsonObj.getString("quote"));
            } catch (JSONException e) {
                Log.e(TAG,e.getMessage());
            }

        }
    }
}
