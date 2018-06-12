package org.example.warmachine.KeyMap;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.net.URL;

public class PlotMapTrace extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String jsonResponse = "";
    String STRING_URL;
    String phon;
    PolylineOptions line;
    double pd1,pd2;
    boolean mapready=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plot_map_trace);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent i=getIntent();
        phon=i.getStringExtra("phone");

        STRING_URL="http://ishmeet7.esy.es/android/get_product_detail.php?Apikey="+phon;

        EarthquakeAsynctask eaa = new EarthquakeAsynctask();
        eaa.execute();

        final Handler ha=new Handler();
        ha.postDelayed(new Runnable() {
            @Override
            public void run() {
                EarthquakeAsynctask1 eaa1 = new EarthquakeAsynctask1();
                eaa1.execute();
                ha.postDelayed(this,6000);
            }
        }, 6000);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        mapready=true;

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
       // mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
       // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    public class EarthquakeAsynctask extends AsyncTask<URL, Void,String[]> {
        @Override
        protected String[] doInBackground(URL... urls) {
            URL url = QueryUtils.createUrl(STRING_URL);
            String merg[]=new String[2];
            try {
                jsonResponse = QueryUtils.makeHttpGetRequest(url);
                Log.i("JsonResposee",jsonResponse);
            } catch (Exception e) {

            }
            merg=QueryUtils.jsonParse(jsonResponse);
            return merg;
        }

        @Override
        protected void onPostExecute(String[] earthquakes) {
           // Toast.makeText(PlotMapTrace.this,earthquakes[0]+" "+earthquakes[1],Toast.LENGTH_SHORT).show();
            // Plot First PolyLine
            double d1 = Double.parseDouble(earthquakes[0]);
            double d2 = Double.parseDouble(earthquakes[1]);
            pd1=d1;
            pd2=d2;

            Log.i("mapppppp",earthquakes[0]+" "+earthquakes[1] );

            line = new PolylineOptions().add(new LatLng(d1,d2), new LatLng(pd1,pd2)).width(13).color(getApplicationContext().getResources().getColor(R.color.floroGreen));
            mMap.addPolyline(line);

            LatLng sydney = new LatLng(d1,d2);
            mMap.addMarker(new MarkerOptions().position(sydney).title("Marker"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

            CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(sydney, 14);
            mMap.animateCamera(yourLocation);

            super.onPostExecute(earthquakes);

        }
    }

    public class EarthquakeAsynctask1 extends AsyncTask<URL, Void,String[]> {
        @Override
        protected String[] doInBackground(URL... urls) {
            URL url = QueryUtils.createUrl(STRING_URL);
            String merg[]=new String[2];
            try {
                jsonResponse = QueryUtils.makeHttpGetRequest(url);
                Log.i("JsonResposee",jsonResponse);
            } catch (Exception e) {

            }
            merg= QueryUtils.jsonParseone(jsonResponse);
            return merg;
        }

        @Override
        protected void onPostExecute(String[] earthquakes) {
            //Toast.makeText(PlotMapTrace.this,earthquakes[0]+" "+earthquakes[1],Toast.LENGTH_SHORT).show();

            double d1 = Double.parseDouble(earthquakes[0]);
            double d2 = Double.parseDouble(earthquakes[1]);

            if(d1==pd1&&d2==pd2){

            }
            else {
                line = new PolylineOptions().add(new LatLng(d1,d2), new LatLng(pd1,pd2)).width(13).color(getApplicationContext().getResources().getColor(R.color.floroGreen));
                mMap.addPolyline(line);

                LatLng sydney = new LatLng(d1,d2);
                mMap.addMarker(new MarkerOptions().position(sydney).title("Marker"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(sydney, 14);
                mMap.animateCamera(yourLocation);
            }

            pd1=d1;
            pd2=d2;
            super.onPostExecute(earthquakes);

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.mapu:
                if(mapready){
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }
                return true;
            case R.id.satellite:
                if(mapready){
                    Log.i("jgaaaaaaaaaaaa","jogaaaaaaaaa");
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);}
                return true;
            case R.id.hybrid:
                if(mapready){
                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);}
                return true;
            case R.id.none:
                if(mapready){
                    mMap.setMapType(GoogleMap.MAP_TYPE_NONE);}
                return true;
            case R.id.terrain:
                if(mapready){
                    mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);}
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
