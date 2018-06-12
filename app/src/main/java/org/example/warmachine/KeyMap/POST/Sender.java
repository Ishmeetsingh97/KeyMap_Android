package org.example.warmachine.KeyMap.POST;

/**
 * Created by WarMachine on 1/28/2017.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.security.SecureRandom;

import org.example.warmachine.KeyMap.LocationPost;
import org.example.warmachine.KeyMap.MainActivity;

public class Sender extends AsyncTask<Void,Void,String> {
    Context c;
    String urlAddress;
    String Latitude,Longitude;
    ProgressDialog pd;
    static String apikey;

    private static SecureRandom random = new SecureRandom();
    public static String nextSessionId() {
        apikey = new BigInteger(50, random).toString(32);
        return apikey;
    }

    public Sender(Context c, String urlAddress, String Latitude, String Longitude) {
        this.c = c;
        this.urlAddress = urlAddress;
        this.Latitude = Latitude;
        this.Longitude = Longitude;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd = new ProgressDialog(c);
        pd.setTitle("Send");
        pd.setMessage("Sending..Please wait");
        pd.show();
    }

    @Override
    protected String doInBackground(Void... params) {
        return this.send();
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        pd.dismiss();
        if(response != null)
        {
            Toast.makeText(c,"sucesss"+response, Toast.LENGTH_LONG).show();

        }else
        {
            Toast.makeText(c,"Unsuccessful "+response, Toast.LENGTH_LONG).show();
        }
    }



    private String send()
    {
        HttpURLConnection con=Connector.connect(urlAddress);
        if(con==null)
        {
            return null;
        }
        try
        {
            String phonu=MainActivity.phones;

            OutputStream os=con.getOutputStream();
            BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
            bw.write(new DataPackager(Latitude,Longitude,phonu,LocationPost.IMEI).packData());

            bw.flush();
            bw.close();
            Log.i("url",urlAddress);

            os.close();
            int responseCode = con.getResponseCode();
            if(responseCode == con.HTTP_OK)
            {
                BufferedReader br=new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuffer response=new StringBuffer();
                String line;
                while ((line=br.readLine()) != null)
                {
                    response.append(line);
                }
                br.close();
                return response.toString();
            }else
            {
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
