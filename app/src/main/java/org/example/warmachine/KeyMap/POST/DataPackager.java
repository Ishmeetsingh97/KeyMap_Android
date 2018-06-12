package org.example.warmachine.KeyMap.POST;

/**
 * Created by WarMachine on 1/28/2017.
 */

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;

public class DataPackager {
    String Latitude,Longitude;
    String apikey;
    String IMEI;

    public DataPackager(String Latitude, String Longitude, String apikey, String IMEI) {
        this.Latitude = Latitude;
        this.Longitude = Longitude;
        this.apikey = apikey;
        this.IMEI=IMEI;

    }

    public String packData()
    {
        JSONObject jo=new JSONObject();
        StringBuffer packedData=new StringBuffer();
        try
        {

            jo.put("Latitude",Latitude);
            jo.put("Longitude",Longitude);
            jo.put("Apikey",apikey);
            jo.put("IMEI",IMEI);
            Boolean firstValue=true;
            Iterator it=jo.keys();
            do {
                String key=it.next().toString();
                String value=jo.get(key).toString();
                if(firstValue)
                {
                    firstValue=false;
                }else
                {
                    packedData.append("&");
                }
                packedData.append(URLEncoder.encode(key,"UTF-8"));
                packedData.append("=");
                packedData.append(URLEncoder.encode(value,"UTF-8"));
            }while (it.hasNext());
            return packedData.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}