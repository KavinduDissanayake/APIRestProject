package com.example.coivd_app.Screens.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coivd_app.Connection.OkHttpSingleton;
import com.example.coivd_app.Connection.URL_OF_DB;
import com.example.coivd_app.HelperClasss.ScannerModel;
import com.example.coivd_app.LoginActivity;
import com.example.coivd_app.Nav_menu;
import com.example.coivd_app.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.Manifest.permission.CAMERA;

public class LocationActivity extends AppCompatActivity  implements LocationListener {


    //live locations get
    LocationManager locationManager;
    private double latitude;
    private double longitude;
    private static final int REQUEST_LOCATION = 212;


    //progress dialog init
    ProgressDialog dialogBox;


    TextView latText;
    TextView lonText;

    Button Confirm;



    String TAG="LocationActivity";
    ScannerModel scannerModel;


    String nic_current_user;


    //session for get values
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_activity);

        init();




        Intent intent = getIntent();
         scannerModel = (ScannerModel) intent.getExtras().getSerializable("scannerModel");

        Log.e("scannerModel",scannerModel.getMyResult()+"");;


        //auto calling

        dialogBox.setMessage("Getting location .......");

        //getLiveLocation for send to database
        if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LocationActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

            dialogBox.dismiss();

        } else {
            detectCurrentLocation();
            dialogBox.show();

            //progress running ..........


        }


        Confirm.setOnClickListener(v -> {
            AddToDatabase();

        });


    }

    private void init() {
        dialogBox = new ProgressDialog(this);

        latText=findViewById(R.id.latText);
        lonText=findViewById(R.id.lonText);
        Confirm=findViewById(R.id.Confirm);


        //declare a session
                sharedpreferences=LocationActivity.this.getSharedPreferences("user_details",MODE_PRIVATE);
        nic_current_user=sharedpreferences.getString("nic","");
    }



    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case  REQUEST_LOCATION:{

                if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    detectCurrentLocation();
                }
                else
                {
                    Toast.makeText(this, "Permission Denied.", Toast.LENGTH_SHORT).show();


                }
            }
            break;




        }
    }


    //live location defect
    private void detectCurrentLocation() {
        Toast.makeText(this, "Please getting your current Location", Toast.LENGTH_SHORT).show();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        latitude=location.getLatitude();
        longitude=location.getLongitude();


        latText.setText(latitude+"");
        lonText.setText(longitude+"");

        Log.e("latitude",latitude+"");;
        Log.e("longitude",longitude+"");;




        //alert Box
        dialogBox.dismiss();

    }

    private void AddToDatabase() {

        RequestBody requestBody = new MultipartBody.Builder()
                .setType( MultipartBody.FORM )
                .addFormDataPart( "nic", nic_current_user)
                .addFormDataPart( "lat", latText.getText().toString())
                .addFormDataPart( "lon", lonText.getText().toString())


                .build();



        Request request = new Request.Builder()
                .url(scannerModel.getMyResult()+"")
                .post( requestBody )
                .build();

        OkHttpSingleton.getInstance().getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e( TAG, e.getMessage() );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                if (response.isSuccessful()) {

                    try {
                        final String myResponse = response.body().string();

                       LocationActivity.this.runOnUiThread(() -> {
                            try {
                                String JSON_STRING = "["+myResponse+"]";
                                JSONArray mJsonArray = new JSONArray(JSON_STRING);
                                JSONObject mJsonObject = mJsonArray.getJSONObject(0);
                                String message = mJsonObject.getString( "message" );

                                dialogBox(message);
                                if(message.equals("Update successful...!")){




                                    startActivity(new Intent(LocationActivity.this, Nav_menu.class));
                                    finish();


                                }



                                Log.e( TAG+" message", message );

                            }catch (Exception e){

                                Log.e( TAG, e.getMessage() );

                                // dialogBox(e.getMessage());

                            }
                        });



                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

    }


    // Dialog box

    public void dialogBox(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(LocationActivity.this).create();
        alertDialog.setTitle("Alert Box");
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog, which) -> {
            // Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
        });

        alertDialog.show();


    }


    @Override
    public void onStatusChanged (String provider,int status, Bundle extras){

    }

    @Override
    public void onProviderEnabled (@NonNull String provider){

    }

    @Override
    public void onProviderDisabled (@NonNull String provider){
        Toast.makeText(this, "Turn on gps", Toast.LENGTH_SHORT).show();

    }

}