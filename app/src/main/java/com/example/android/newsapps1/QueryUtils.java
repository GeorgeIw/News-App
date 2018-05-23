package com.example.android.newsapps1;

import android.text.TextUtils;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

public class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();
    //required private empty constructor
    private QueryUtils() {
    }

    //make an HTTP request to the given URL and return a string as a response
    private static String makeHttpRequest(URL url) throws IOException{
        String jsonResponseCode = "";
        //if the url is null, return early
        if(url == null){
            return jsonResponseCode;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try{
            //start the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            //set the timeout of the connection in milliseconds
            urlConnection.setConnectTimeout(15000);
            //set the request method for the connection(GET)
            urlConnection.setRequestMethod("GET");
            //now connect to the given url
            urlConnection.connect();
            //if the request was successful, and thus it gave a response code of 200
            //read the input stream and parse the response
            if(urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponseCode = streamReader(inputStream);
                //else, return a Log message
            } else {
                Log.e(LOG_TAG,"Error response code: " +urlConnection.getResponseCode());
            }
        }
        //if there were an exception at try block, "catch" the exception and print a Log message
        catch(IOException e){
            Log.e(LOG_TAG,"Cannot get the JSON reuslts",e);
            //finally, if the response from the urlConnection is null, disconnect
        }finally{
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            //and if the inputStream is not null, close the input stream
            if(inputStream != null){
                inputStream.close();
            }
        }

        return jsonResponseCode;
    }

    private static List<Articles> jsonResponseData(String jsonItems){
        //if the JSON string is empty or null, return early
        if(TextUtils.isEmpty(jsonItems)){
            return null;
        }
        //create an empty ArrayList
        List<Articles> articles = new ArrayList<>();
        //try to parse the JSON response string
        //and if there is an exception, "catch" it
        try{
            //create a JSONObject from the JSON response string
            JSONObject jsonObject = new JSONObject(jsonItems);
            //extract the JSONObject with the key:"response"
            JSONObject jsonObjectResponse = jsonObject.getJSONObject("response");
            //extract the JSONArray with the key:"results"
            JSONArray articlesArray = jsonObjectResponse.getJSONArray("results");
            //create an object for each Article in the articlesArray
            for(int i = 0 ; i < articlesArray.length() ; i++) {
                //get the Article at the (i) position in the ArrayList
                JSONObject jsonResponse = articlesArray.getJSONObject(i);
                //extract the JSONArray with the key:"tags"
                JSONArray tagsArray = jsonResponse.getJSONArray("tags");
                //create an object for each Tag in the tagsArray
                for (int j = 0; j < tagsArray.length(); j++) {
                    //get the tag at the (j) position in the ArrayList
                    JSONObject jsonArrayResponse = tagsArray.getJSONObject(j);
                    //extract the value for the key "sectionName"
                    String section = jsonResponse.optString("sectionName");
                    //extract the value for the key "webTitle"
                    String title = jsonResponse.optString("webTitle");
                    //extract the value for the key "tags"
                    String author = jsonArrayResponse.optString("webTitle");
                    //extract the value for the key "webPublicationDate"
                    String date = jsonResponse.optString("webPublicationDate");
                    //extract the value for the key "webUrl"
                    String url = jsonResponse.optString("webUrl");
                    //create a new Object with the variables section,title,date,url from the JSON response string
                    Articles article = new Articles(section, title, author, date, url);
                    //add the Article to the ArrayList of Articles
                    articles.add(article);
                }
            }
        } catch(JSONException e){
            //if there is an error while executing the "try" commands, catch the exception
            //and print a Log message
            Log.e(LOG_TAG,"Cannot parse the JSON results",e);
        }

        return articles;
    }

    public static List<Articles> fetchArticlesData(String requestUrl){
        //create a URL object
        URL url = createUrl(requestUrl);
        //perform an HTTP request to the URL and receive a JSON response
        String jsonResponse = null;
        try{
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e){
            Log.e(LOG_TAG,"It seems there is a problem while making the Http request",e);
        }
        //create a list from the required filed of the JSON response
        List<Articles> articles = jsonResponseData(jsonResponse);
        return articles;
    }

    //create a new URL object from the given String URL
    private static URL createUrl(String urlCreate){
        URL url = null;
        try {
            url = new URL(urlCreate);
            //if there is an exception at the try block, "catch" it and return a log message
        } catch(MalformedURLException e){
            Log.e(LOG_TAG,"Cannot build the URL",e);
        }

        return url;
    }

    private static String streamReader(InputStream inputStream) throws IOException{
        StringBuilder stringBuilder = new StringBuilder();
        if(inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();

            while (line != null) {
                stringBuilder.append(line);
                line = reader.readLine();
            }
        }

        return stringBuilder.toString();
    }


}
