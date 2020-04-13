package com.example.karunafighterspatients;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AnalyticsActivity extends AppCompatActivity  implements LocationListener {

    public static final String TAG="TAG";

    Double latitude, longitude;

    String newConifrmed, totalConfirmed, newDeaths, totalDeaths, newRecovered, totalRecovered;
    TextView textnewConfirmed, textTotalConfirmed, textNewDeaths, textTotalDeaths, textNewRecoverd, textTotalRecovered;
    Button buttonGrocerries, buttonChatbot;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);

            getLocation();
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        }
        textnewConfirmed = (TextView) findViewById(R.id.textConfirmed);
        textTotalConfirmed = (TextView) findViewById(R.id.textTotalConfirmed);
        textNewDeaths = (TextView) findViewById(R.id.textNewDeaths);
        textTotalDeaths = (TextView) findViewById(R.id.textTotalDeaths);
        textNewRecoverd = (TextView) findViewById(R.id.textNewRecoverd);
        textTotalRecovered = (TextView) findViewById(R.id.textTotalRecovered);

        buttonChatbot = (Button) findViewById(R.id.buttonAssessment);
        buttonGrocerries = (Button) findViewById(R.id.buttonGrocery);

        buttonChatbot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AnalyticsActivity.this, ChatBot.class);
                startActivity(i);
            }
        });

        buttonGrocerries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("geo:"+latitude+","+ longitude+"?q=grocery");

                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        new GetCaronaDetails().execute();

        textnewConfirmed.setText("New Confirmed: "+LoginActivity.newConifrmed);
        textTotalConfirmed.setText("Total Confirmed: "+LoginActivity.totalConfirmed);
        textNewDeaths.setText("New Deaths: "+LoginActivity.newDeaths);
        textTotalDeaths.setText("Total Deaths: "+LoginActivity.totalDeaths);
        textNewRecoverd.setText("New Recovered: "+LoginActivity.newRecovered);
        textTotalRecovered.setText("Total Recovered: "+LoginActivity.totalRecovered);


    }
    void getLocation() {
        try {
            Toast.makeText(getApplicationContext(),"starting uifhuih",Toast.LENGTH_SHORT).show();
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);

            Log.i(TAG,"Location Success");
        }
        catch(SecurityException e) {
            e.printStackTrace();
            Log.i(TAG,"Location failure");
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        Toast.makeText(getApplicationContext(),"latt",Toast.LENGTH_SHORT).show();
        latitude = Double.parseDouble(String.valueOf(location.getLatitude()));
        longitude = Double.parseDouble(String.valueOf(location.getLongitude()));

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private class GetCaronaDetails extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
//            pDialog = new ProgressDialog(CameraActivity.this);
//            pDialog.setMessage("Please wait...");
//            pDialog.setCancelable(false);
//            pDialog.show();

        }
        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            String url1 = "https://api.covid19api.com/summary";


            // Making a request to url and getting response
            String jsonStrAqi = sh.makeServiceCall(url1);


            Log.e(TAG,"Response from url: "+jsonStrAqi);


            if (jsonStrAqi !=null) {
                try {


                    JSONObject jsonObject2 = new JSONObject(jsonStrAqi);
                    JSONArray jsonObject1 = jsonObject2.getJSONArray("Countries");

                    for(int i=0;i<jsonObject1.length();i++) {

                        JSONObject c = jsonObject1.getJSONObject(i);
                        if(c.getString("Country").equals("India")) {
                            newConifrmed = c.getString("NewConfirmed");
                            totalConfirmed = c.getString("TotalConfirmed");
                            newDeaths = c.getString("NewDeaths");
                            totalDeaths = c.getString("TotalDeaths");
                            newRecovered = c.getString("NewRecovered");
                            totalRecovered = c.getString("TotalRecovered");
                        }



                        Log.e(TAG, "NewConfirmed: "+newConifrmed+"\ntotal confirmed: " + totalConfirmed + "\nnewDeaths: " + newDeaths + "\ntotalDeaths: " + totalDeaths
                        +"\nNew Recoverd: "+newRecovered+"\nTotal Recovered: "+totalRecovered);
                    }


//                    Log.e(TAG, "New Confirmed: "+jsonObject2.getString("NewConfirmed"));



                }
                catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
//            if (pDialog.isShowing())
//                pDialog.dismiss();

        }

    }
}
