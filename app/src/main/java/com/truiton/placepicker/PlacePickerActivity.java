package com.truiton.placepicker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.twitter.sdk.android.core.TwitterCore;
import io.fabric.sdk.android.Fabric;
//import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.tweetui.TweetUi;
import com.twitter.sdk.android.Twitter;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.List;
import java.util.Locale;


public class PlacePickerActivity extends AppCompatActivity {
    private static final int PLACE_PICKER_REQUEST = 1;
    private double lat,lng;
    private TextView mName;
    private TextView mAddress;
    private TextView mAttributions;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig =  new TwitterAuthConfig("consumerKey", "consumerSecret");
        Fabric.with(this, new TweetUi(), new Twitter(authConfig));
        setContentView(R.layout.activity_place_picker);
        mName = (TextView) findViewById(R.id.textView);
        mAddress = (TextView) findViewById(R.id.textView2);
        mAttributions = (TextView) findViewById(R.id.textView3);
        Button pickerButton = (Button) findViewById(R.id.pickerButton);
        Button tweetButton=(Button) findViewById(R.id.tweetButton);
        pickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PlacePicker.IntentBuilder intentBuilder =
                            new PlacePicker.IntentBuilder();
                    intentBuilder.setLatLngBounds(BOUNDS_MOUNTAIN_VIEW);
                    Intent intent = intentBuilder.build(PlacePickerActivity.this);
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);

                } catch (GooglePlayServicesRepairableException
                        | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
        tweetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),TweetTimeline.class);
                i.putExtra("lat",lat);
                i.putExtra("lng",lng);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {

        if (requestCode == PLACE_PICKER_REQUEST
                && resultCode == Activity.RESULT_OK) {

            final Place place = PlacePicker.getPlace(this, data);
            final CharSequence name = place.getName();
            final CharSequence address = place.getAddress();
            final String placeid=place.getId();
            final Uri placeuri=place.getWebsiteUri();
            final LatLng latilongi=place.getLatLng();
            lat = latilongi.latitude;
            lng = latilongi.longitude;


            String attributions = (String) place.getAttributions();
            if (attributions == null) {
                attributions = "";
            }
            //added to get place name
            /*Toast.makeText(PlacePickerActivity.this, attributions,
                    Toast.LENGTH_LONG).show();*/


            mName.setText(name);

            mAddress.setText(address);
            mAttributions.setText(Html.fromHtml(attributions));
            //added to get place id
            /*Toast.makeText(PlacePickerActivity.this, placeid,
                    Toast.LENGTH_LONG).show();

            Toast.makeText(PlacePickerActivity.this, name,
                    Toast.LENGTH_LONG).show();

            Toast.makeText(PlacePickerActivity.this,"" +placeuri+"",
                    Toast.LENGTH_LONG).show();

            Toast.makeText(PlacePickerActivity.this,""+latilongi+"" ,
                    Toast.LENGTH_LONG).show();
*/
            Geocoder gcd = new Geocoder(PlacePickerActivity.this, Locale.getDefault());
            try {
                List<Address> addresses = gcd.getFromLocation(lat, lng, 1);

                if (addresses.size() > 0) {
                    //System.out.println(addresses.get(0).getLocality());
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("" + addresses.get(0).getLocality() + "")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //do things
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
            catch (Exception e){}




            /*Geocoder gcd = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = gcd.getFromLocation(lat, lng, 1);
            if (addresses.size() > 0)
                System.out.println(addresses.get(0).getLocality());*/

            /*List<Address> list = GeoCoder.getFromLocation(location
                    .getLatitude(), location.getLongitude(), 1);
            if (list != null & list.size() > 0) {
                Address address = list.get(0);
                result = address.getLocality();
                return result;*/






            /*HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI(url));
            HttpResponse response = client.execute(request);
            BufferedReader in = new BufferedReader(new InputStreamReader(response
                    .getEntity().getContent()));
            String line = "";

            while ((line = in.readLine()) != null) {

                JSONObject jObject = new JSONObject(line);

                if (jObject.has("name")) {

                    String temp = jObject.getString("name");
                    Log.e("name",temp);

                }

            }*/



        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
