package org.example.warmachine.KeyMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Toast;
import android.Manifest;


import com.yarolegovich.lovelydialog.LovelyTextInputDialog;

public class MainActivity extends AppCompatActivity {

    CardView send, receive;
    public static String phones;
    private static final int CONTACT_PICKER_RESULT = 1001;
    LocationManager locationManager;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle(R.string.title_location_permission)
                        .setMessage(R.string.text_location_permission)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:

                        String provider = locationManager.getBestProvider(new Criteria(), false);
                        locationManager.requestLocationUpdates(provider, 400, 1, (LocationListener) this);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        send = (CardView) findViewById(R.id.send);
        receive = (CardView) findViewById(R.id.receive);

        checkLocationPermission();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent i=new Intent(MainActivity.this,ContactsListActivity.class);
                //startActivity(i);

                Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                        ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);
            }
        });

        receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Intent i=new Intent(MainActivity.this,GetPhoneNumber.class);
                //startActivity(i);

                new LovelyTextInputDialog(MainActivity.this, R.style.EditTextTintTheme)
                        .setTopColorRes(R.color.darkDeepOrange)
                        .setTitle(R.string.text_input_title)
                        //.setMessage(R.string.text_input_message)
                        .setIcon(R.drawable.ic_assignment_white_36dp)
                        .setInputFilter(R.string.text_input_error_message, new LovelyTextInputDialog.TextFilter() {
                            @Override
                            public boolean check(String text) {
                                return text.matches("\\w+");
                            }
                        })
                        .setConfirmButton(android.R.string.ok, new LovelyTextInputDialog.OnTextInputConfirmListener() {
                            @Override
                            public void onTextInputConfirmed(String text) {
                                Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();

                                Intent i=new Intent(MainActivity.this,PlotMapTrace.class);
                                i.putExtra("phone",text);
                                startActivity(i);
                            }
                        })
                        .show();
            }
        });
    }



    public void onActivityResult(int reqCode, int resultCode, Intent data) {

        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {

            case (CONTACT_PICKER_RESULT):

                if (resultCode == Activity.RESULT_OK) {

                    Uri contactData = data.getData();
                    Cursor contactCursor = getContentResolver().query(contactData,
                            new String[]{ContactsContract.Contacts._ID}, null, null,
                            null);
                    String id = null;
                    if (contactCursor.moveToFirst()) {
                        id = contactCursor.getString(contactCursor
                                .getColumnIndex(ContactsContract.Contacts._ID));
                    }
                    contactCursor.close();
                    String phoneNumber = null;
                    Cursor phoneCursor = getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "= ? ",
                            new String[]{id}, null);
                    if (phoneCursor.moveToFirst()) {
                        phoneNumber = phoneCursor
                                .getString(phoneCursor
                                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    }
                    String withoutspaces = "";
                    for (int i = 0; i < phoneNumber.length(); i++) {
                        if (phoneNumber.charAt(i) != ' ')
                            withoutspaces += phoneNumber.charAt(i);

                    }

                    if(withoutspaces.length()>10){
                        phones=withoutspaces.substring(3);
                    }
                    else {
                        phones=withoutspaces;
                    }

                    Toast.makeText(MainActivity.this,phones,Toast.LENGTH_SHORT).show();
                    phoneCursor.close();

                    Intent i=new Intent(MainActivity.this,LocationPost.class);
                    startActivity(i);

                    break;
                }
        }
    }


}
