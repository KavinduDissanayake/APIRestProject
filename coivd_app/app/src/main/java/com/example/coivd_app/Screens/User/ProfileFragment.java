package com.example.coivd_app.Screens.User;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.coivd_app.Connection.OkHttpSingleton;
import com.example.coivd_app.Connection.URL_OF_DB;
import com.example.coivd_app.HelperClasss.DatePickerFragment;
import com.example.coivd_app.LoginActivity;
import com.example.coivd_app.R;
import com.example.coivd_app.RegisterActivity;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


public class ProfileFragment extends Fragment {


EditText nicEdit;
EditText nameEdit;
EditText emailEdit;
EditText dobEdit;
EditText adNoEdit;
EditText adStreetEdit;
EditText adCityEdit;
EditText phone_numEdit;


ImageView upload_icon;
ImageView navPro;

Bitmap imageBitmap;

TextView full_name;
TextView healthStatus;
TextView ageText;


Button updateBtn;
View view;



private  String CURRENT_UID="34";


    private String TAG="ProfileFragment";

    static final   int  PICK_FROM_GALLERY=123;



    ProgressDialog pd;

    //session for get values
    SharedPreferences sharedpreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.profile_fragment, container, false);


        init();



        dobEdit.setOnClickListener(v -> showDatePicker());

        updateBtn.setOnClickListener(v -> updateData());


        navPro.setOnClickListener(v -> {
            try {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
                } else {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, PICK_FROM_GALLERY);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return view;
    }

    private void updateData() {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType( MultipartBody.FORM )
                .addFormDataPart( "nic", nicEdit.getText().toString())
                .addFormDataPart( "name", nameEdit.getText().toString())
                .addFormDataPart( "email", emailEdit.getText().toString())
                .addFormDataPart( "dob", dobEdit.getText().toString())
                .addFormDataPart( "no", adNoEdit.getText().toString())
                .addFormDataPart( "street", adStreetEdit.getText().toString())
                .addFormDataPart( "city", adCityEdit.getText().toString())
                .addFormDataPart( "phone_num", phone_numEdit.getText().toString())

                .build();



        Request request = new Request.Builder()
                .url(URL_OF_DB.getInstance().user_update_info)
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

                        getActivity().runOnUiThread(() -> {
                            try {
                                String JSON_STRING = "["+myResponse+"]";
                                JSONArray mJsonArray = new JSONArray(JSON_STRING);
                                JSONObject mJsonObject = mJsonArray.getJSONObject(0);
                                String message = mJsonObject.getString( "message" );

                                dialogBox(message);
                                if(message.equals("Update successful...!")){
                                    LoadDataFromDB();

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

    private void init() {



        //declare a session
        sharedpreferences=getContext().getSharedPreferences("user_details",MODE_PRIVATE);
        CURRENT_UID=sharedpreferences.getString("uid","");




        pd = new ProgressDialog(getContext());

        healthStatus=view.findViewById(R.id.healthStatus);
        full_name=view.findViewById(R.id.full_name);
        updateBtn=view.findViewById(R.id.updateBtn);


//        upload_icon=view.findViewById(R.id.upload_icon);
        navPro=view.findViewById(R.id.navPro);

        nicEdit=view.findViewById(R.id.nicEdit);
        nameEdit=view.findViewById(R.id.nameEdit);
        emailEdit=view.findViewById(R.id.emailEdit);
        dobEdit=view.findViewById(R.id.dobEdit);
        adNoEdit=view.findViewById(R.id.adNoEdit);
        adCityEdit=view.findViewById(R.id.adCityEdit);
        adStreetEdit=view.findViewById(R.id.adStreetEdit);
        phone_numEdit=view.findViewById(R.id.phone_numEdit);
        ageText=view.findViewById(R.id.ageText);
    }


    @Override
    public void onStart() {
        super.onStart();

        LoadDataFromDB();
    }

    private void LoadDataFromDB() {

        RequestBody requestBody = new MultipartBody.Builder()
                .setType( MultipartBody.FORM )
                .addFormDataPart( "uid", CURRENT_UID)

                .build();



        Request request = new Request.Builder()
                .url(URL_OF_DB.getInstance().read_user_single_info)
                .post( requestBody )
                .build();

        OkHttpSingleton.getInstance().getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e( TAG, e.getMessage() );
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @SuppressLint("ResourceAsColor")
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                if (response.isSuccessful()) {

                    try {
                        final String myResponse = response.body().string();

                       getActivity().runOnUiThread(() -> {
                            try {
                                String JSON_STRING = "["+myResponse+"]";
                                JSONArray mJsonArray = new JSONArray(JSON_STRING);
                                JSONObject mJsonObject = mJsonArray.getJSONObject(0);
                                String nic = mJsonObject.getString( "nic" );


                                nicEdit.setText(mJsonObject.getString( "nic" )+"");
                                nameEdit.setText(mJsonObject.getString( "name" )+"");
                                emailEdit.setText(mJsonObject.getString( "email" )+"");
                                dobEdit.setText(mJsonObject.getString( "dob" )+"");
                                adNoEdit.setText(mJsonObject.getString( "no" )+"");
                                adStreetEdit.setText(mJsonObject.getString( "street" )+"");
                                adCityEdit.setText(mJsonObject.getString( "city" )+"");
                                phone_numEdit.setText(mJsonObject.getString( "phone_num" )+"");

//                                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
//
//                                Date d = df.parse(mJsonObject.getString( "dob" ));
//
//                                int month = d.getMonth();
//
//                                int day= d.getDay();
//
//                                int year = d.getYear();


                                String date=mJsonObject.getString( "dob")+"";
                                String[] items1 = date.split("/");
                                String date1=items1[0];
                                String month=items1[1];
                                String year=items1[2];

//                                //ageText.setText(getAgeFromLong(mJsonObject.getString( "dob" ))+"");
//                                Log.e( TAG+" Date", "Date = "+date1 );
//                                Log.e( TAG+" year", "year = "+year );
//                                Log.e( TAG+" month", "month = "+month );




                                ageText.setText("Age :"+getAge(Integer.parseInt(year),Integer.parseInt(month),Integer.parseInt(date1)));

                                if(mJsonObject.getString( "status" ).equals("Positive")){
                                    healthStatus.setText("Health Status :"+mJsonObject.getString( "status" )+"");
                                    healthStatus.setTextColor(Color.RED);
                                }else{
                                    healthStatus.setText("Health Status :"+mJsonObject.getString( "status" )+"");
//                                    healthStatus.setTextColor(Color.RED);
                                }





                                try {
                                    Picasso.get().load( ""+mJsonObject.getString( "img_path" )+"")
                                            .networkPolicy( NetworkPolicy.NO_CACHE)
                                            .memoryPolicy( MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE)
                                            .into( navPro );

                                }catch (Exception e){


                                }


                               // Log.e( TAG+" nic", nic );

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

    private void showDatePicker() {
        DatePickerFragment dateObj = new DatePickerFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        dateObj.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        dateObj.setCallBack(ondate);
        dateObj.show(getFragmentManager(), "Date Picker");
    }
    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            dobEdit.setText(
                    String.valueOf(dayOfMonth) +"/"+ String.valueOf(monthOfYear+1)  + "/" +String.valueOf(year)  );
        }
    };

    // Dialog box

    public void dialogBox(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle("Alert Box");
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog, which) -> {
            // Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
        });

        alertDialog.show();


    }



    //age calculator
    private String getAge(int year, int month, int day){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PICK_FROM_GALLERY){
            if (resultCode == RESULT_OK){

                Uri path = data.getData();
                try {
                    imageBitmap= MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),path);
                    navPro.setImageBitmap(imageBitmap);
                    Log.e("Image path",imageBitmap+"");

                    UpLoadingImage();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void UpLoadingImage() {


        pd.setMessage("Uploading loading");
        pd.show();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG,75, byteArrayOutputStream);
        byte[] imageInByte = byteArrayOutputStream.toByteArray();
        String encodedImage =  Base64.encodeToString(imageInByte,Base64.DEFAULT);


        RequestBody requestBody = new MultipartBody.Builder()
                .setType( MultipartBody.FORM )
                .addFormDataPart( "uid", CURRENT_UID)
                .addFormDataPart( "image", encodedImage)

                .build();



        Request request = new Request.Builder()
                .url(URL_OF_DB.getInstance().upload_image)
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

                        getActivity().runOnUiThread(() -> {
                            try {
                                String JSON_STRING = "["+myResponse+"]";
                                JSONArray mJsonArray = new JSONArray(JSON_STRING);
                                JSONObject mJsonObject = mJsonArray.getJSONObject(0);
                                String message = mJsonObject.getString( "message" );
                                pd.dismiss();
                                dialogBox(message);
                                if(message.equals("Update successful...!")){
                                    LoadDataFromDB();

                                }



                                Log.e( TAG+" message", message );

                            }catch (Exception e){
                                pd.dismiss();
                                Log.e( TAG, e.getMessage() );

                                // dialogBox(e.getMessage());

                            }
                        });



                    } catch (IOException e) {
                        e.printStackTrace();
                        pd.dismiss();
                    }
                }

            }
        });

    }

}