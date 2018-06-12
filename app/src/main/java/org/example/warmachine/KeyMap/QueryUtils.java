package org.example.warmachine.KeyMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


public class QueryUtils {

    static List<Integer> list = new ArrayList<Integer>();
    public QueryUtils() {
    }

    public static URL createUrl(String Stringurl)
    {
        URL url = null;
        try{
            url= new URL(Stringurl);
        }
        catch (Exception e)
        {
            return null;
        }
        return url;
    }

    public static String makeHttpGetRequest(URL url) throws IOException
    {
        HttpURLConnection urlConnection=null;
        InputStream inputStream=null;
        String jsonResponse="";

        try{

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();
            if(urlConnection.getResponseCode()==200)
            {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }

        }
        catch(Exception e)
        {}
        finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }

        return jsonResponse;
    }


    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    public static String[] jsonParse(String jsonString)
    {
        String lat=null,lon=null;
        String merg[]=new String[2];
        try {
            JSONObject root = new JSONObject(jsonString);
            JSONArray feature = root.getJSONArray("Coordinates");
            int j=feature.length();
                JSONObject arrindex = feature.getJSONObject(j-1);
                lat = arrindex.getString("Latitude");
                lon=arrindex.getString("Longitude");
            merg[0]=lat;
            merg[1]=lon;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return merg;
    }

    public static String[] jsonParseone (String jsonString)
    {
        String lat=null,lon=null;
        String merg[]=new String[2];
        try {
            JSONObject root = new JSONObject(jsonString);
            JSONArray feature = root.getJSONArray("Coordinates");
            int j=feature.length();
            JSONObject arrindex = feature.getJSONObject(j-1);
                lat = arrindex.getString("Latitude");
                lon=arrindex.getString("Longitude");
                merg[0]=lat;
            merg[1]=lon;


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return merg;
    }
}




