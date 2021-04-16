package com.example.coivd_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coivd_app.Connection.OkHttpSingleton;
import com.example.coivd_app.Connection.URL_OF_DB;
import com.example.coivd_app.HelperClasss.DatePickerFragment;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {


    EditText nicTxt;
    EditText nameTxt;
    EditText emailTxt;
    EditText dobTxt;
    EditText adStreetTxt;
    EditText adNoTxt;
    EditText adCityTxt;
    EditText numberTxt;
    EditText passwordTxt;
    TextView backLoginBtn;
    Button regBtn;

//session

    SharedPreferences sharedpreferences;



    String TAG="RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        //view initialization
        init();


        //session initialization
        //session
        sharedpreferences=getSharedPreferences( "user_details", Context.MODE_PRIVATE);


        //set to date picker for birthday
        dobTxt.setOnClickListener(v -> {
            showDatePicker();
        });

        regBtn.setOnClickListener(v -> {
            //register with database
            RegisterToDatabase();
        });

        backLoginBtn.setOnClickListener(v -> {

            startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            finish();

        });

    }

    private void RegisterToDatabase() {
        // ---------------------------------Validation   Part------------------------------------------
        if (TextUtils.isEmpty(nicTxt.getText().toString())) {
            nicTxt.setError("Nic is required !");
            return;
        }



                if (TextUtils.isEmpty(nameTxt.getText().toString())) {
            nameTxt.setError("Name is required !");
            return;
        }

        if (TextUtils.isEmpty(emailTxt.getText().toString())  && (!Patterns.EMAIL_ADDRESS.matcher( emailTxt.getText().toString() ).matches())) {
            emailTxt.setError("Please enter a valid email address !");
            return;
        }


        if (TextUtils.isEmpty(dobTxt.getText().toString())) {
            dobTxt.setError("DOB is required !");
            return;
        }

        if (TextUtils.isEmpty(adNoTxt.getText().toString())) {
            adNoTxt.setError("Address no required !");
            return;
        }
        if (TextUtils.isEmpty(adStreetTxt.getText().toString())) {
            adStreetTxt.setError("Nic is required !");
            return;
        }
        if (TextUtils.isEmpty(nicTxt.getText().toString())) {
            nicTxt.setError("Address Street required !");
            return;
        }


        if (TextUtils.isEmpty(numberTxt.getText().toString())) {
            numberTxt.setError("Phone Number is required !");
            return;
        }

        if(passwordTxt.length()<8 &&!isValidPassword(passwordTxt.getText().toString())){
            passwordTxt.setError( "Password must be 8 characters" );
            return;
        }


        RequestBody requestBody = new MultipartBody.Builder()
                .setType( MultipartBody.FORM )
                .addFormDataPart( "nic", nicTxt.getText().toString())
                .addFormDataPart( "name", nameTxt.getText().toString())
                .addFormDataPart( "email", emailTxt.getText().toString())
                .addFormDataPart( "dob", dobTxt.getText().toString())
                .addFormDataPart( "no", adNoTxt.getText().toString())
                .addFormDataPart( "street", adStreetTxt.getText().toString())
                .addFormDataPart( "city", adCityTxt.getText().toString())
                .addFormDataPart( "phone_num",numberTxt.getText().toString())
                .addFormDataPart( "status", "Negative")
                .addFormDataPart( "user_role", "Citizens")
                .addFormDataPart( "password", passwordTxt.getText().toString())

                .build();



        Request request = new Request.Builder()
                .url(URL_OF_DB.getInstance().userRegisterUrl)
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

                        RegisterActivity.this.runOnUiThread(() -> {
                            try {
                                String JSON_STRING = "["+myResponse+"]";
                                JSONArray mJsonArray = new JSONArray(JSON_STRING);
                                JSONObject mJsonObject = mJsonArray.getJSONObject(0);
                                String message = mJsonObject.getString( "message" );
                                dialogBox(message);


                                if(message.equals("Register successfully ...!")){



                                    startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                                    finish();
                                }

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
        AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this).create();
        alertDialog.setTitle("Alert Box");
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
               // Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
            }
        });

        alertDialog.show();


    }
    private void init() {
        nicTxt=findViewById(R.id.nicTxt);
        nameTxt=findViewById(R.id.nameTxt);
        emailTxt=findViewById(R.id.emailTxt);
        adNoTxt=findViewById(R.id.adNoTxt);
        dobTxt=findViewById(R.id.dobTxt);
        adStreetTxt=findViewById(R.id.adStreetTxt);
        adCityTxt=findViewById(R.id.adCityTxt);
        numberTxt=findViewById(R.id.numberTxt);
        passwordTxt=findViewById(R.id.passwordTxt);
        backLoginBtn=findViewById(R.id.backLoginBtn);
        regBtn=findViewById(R.id.regBtn);

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
        dateObj.show(getSupportFragmentManager(), "Date Picker");
    }
    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            dobTxt.setText(
                    String.valueOf(dayOfMonth) +"/"+ String.valueOf(monthOfYear+1)  + "/" +String.valueOf(year)  );
        }
    };

    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

}