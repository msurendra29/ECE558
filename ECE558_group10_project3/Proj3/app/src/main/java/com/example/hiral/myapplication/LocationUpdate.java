package com.example.hiral.myapplication;

import android.*;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * Edited by Richa and Surendra on 11/24/2016.
 * LocationUpdate.java handles the UI appearing on Sign In
 */
public class LocationUpdate extends AppCompatActivity {

    private EditText send_message;
    private TextView current_location_text;
    private EditText edittext_location;
    private Button send,btn_cur_location;
    int Place_Picker_request = 1;
    Editable message;
    LoginDataBaseAdapter loginData;
    public double latitude, longitude, modalat, modalon;
    String locStr;
    public static final String EXTRA_USER_NAME="username";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_update);

        /**
         * TODO: Define onClick Listener for "get_location" text box to get user's current location
         * Hint: Need to define IntentBuilder for PlacePicker built-in UI widget
         * Reference : https://developers.google.com/places/android-api/placepicker
         */
        edittext_location = (EditText) findViewById(R.id.edit_location_textbox);
        edittext_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //object of PlacePicker's Intentbuilder to add the PlacePicker functionality
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    Intent intent = builder.build(LocationUpdate.this);
                    startActivityForResult(intent,Place_Picker_request);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }

            }
        });


        /**
         * TODO: Define TextView field to display the address of current location on the UI
         *
         */
          current_location_text = (TextView) findViewById(R.id.txt_cur_location);
        /**
         * TODO: Define onClick Listener for "Current_Location Button"
         * Hint: OnClick event should set the text for TextView field defined above
         */
         btn_cur_location = (Button) findViewById(R.id.current_location);
         btn_cur_location.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                current_location_text.setText(locStr);
             }
         }
        );
        /**
         * Do not edit the code below as it is dependent on server just fill the required snippets
         *
         */
        send_message = (EditText) findViewById(R.id.Send_Message);

        send = (Button) findViewById(R.id.Send_Button);
        loginData = new LoginDataBaseAdapter(this);
        loginData = loginData.open();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * OnClick event for send button gets username and location details
                 */
                message = send_message.getText();
                String rx_username = getIntent().getStringExtra(EXTRA_USER_NAME);

                /**
                 * TODO: Enable the code below after defining getLat() and getLng()
                 * TODO: methods in LoginDataBaseAdapter
                 */
                String rx_lat=loginData.getLat(rx_username);
                String rx_lon=loginData.getLng(rx_username);

                /**
                 * store in latitude , longitude variables to pass to json object
                 */
                modalat = Double.parseDouble(rx_lat);
                modalon = Double.parseDouble(rx_lon);

                try {

                    /**
                     * Creates a JSON object and uses toSend.put to send home, current location along with message
                     *Pass data as name/value pair where you cannot edit name written
                     *in " " ex:"home_lat" as this are hard coded on server side.
                     *You can change the variable name carrying value ex:modalat
                     */
                    JSONObject toSend = new JSONObject();
                    toSend.put("home_lat", modalat);
                    toSend.put("home_lon", modalon);
                    toSend.put("c_lat", latitude);
                    toSend.put("c_lon", longitude);
                    toSend.put("message", message);

                    /**
                     * Creates transmitter object to send data to server
                     */
                    JSONTransmitter transmitter = new JSONTransmitter();
                    transmitter.execute(new JSONObject[]{toSend});

                    /**
                     * Receives a message from the server which is displayed as toast
                     */
                    JSONObject output = transmitter.get();
                    String op = output.getString("message");
                    Toast.makeText(LocationUpdate.this, op, Toast.LENGTH_LONG).show();

                }
                //To handle exceptions
                catch (JSONException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mClient = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    /**
     * TODO: Define onActivityResult() method which would take Place_Picker_request
     * and extract current Latitude, Longitude and address string
     * Hint : Set the address String to "get_location" text box
     * Reference : https://developers.google.com/places/android-api/placepicker
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Place_Picker_request) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                locStr = String.format("Place: %s", place.getName());
                longitude = place.getLatLng().longitude;
                latitude = place.getLatLng().latitude;
                edittext_location.setText(locStr);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("LocationUpdate Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://76.105.208.49:8080/grp10.php"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mClient.connect();
        AppIndex.AppIndexApi.start(mClient, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(mClient, getIndexApiAction());
        mClient.disconnect();
    }
}

