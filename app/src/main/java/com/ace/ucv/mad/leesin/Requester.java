package com.ace.ucv.mad.leesin;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by ctotolin on 01-May-17.
 */

public class Requester extends AsyncTask<String , Void ,String> {

    private String server_response;
    private static final String YOUTUBE_SEARCH_URL = "https://www.youtube.com/results?search_query=";
    private static final String WATCH_STRING = "/watch?v=";
    private IRequester dispatcher;
    private String query;

    public Requester(IRequester requester, String query) {
        this.dispatcher = requester;
        this.query = query;
    }

    @Override
    protected String doInBackground(String... strings) {

        URL url;
        HttpURLConnection urlConnection = null;

        try {
            url = new URL(YOUTUBE_SEARCH_URL + URLEncoder.encode(this.query, "UTF-8"));
            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Language", "en-US");

            urlConnection.setUseCaches(false);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");

            int responseCode = urlConnection.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK){
                server_response = readStream(urlConnection.getInputStream());
                Log.v("CatalogClient", server_response);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        String videoID = getVideoID(server_response);
        dispatcher.requestCallback(videoID);
        super.onPostExecute(videoID);
        Log.e("Response", "" + server_response);
    }

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }

    private String getVideoID(String response) {
        int firstVideoIndex = 0;
        firstVideoIndex = getFirstVideoIndex(response);

        return response.substring(firstVideoIndex + WATCH_STRING.length(), response.indexOf("\"", firstVideoIndex));
    }

    private static int getFirstVideoIndex(String response) {
        return response.indexOf(WATCH_STRING);
    }
}

