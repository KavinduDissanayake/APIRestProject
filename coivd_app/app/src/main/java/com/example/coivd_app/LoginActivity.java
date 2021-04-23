package com.example.coivd_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.coivd_app.Connection.OkHttpSingleton;
import com.example.coivd_app.Connection.URL_OF_DB;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {


    EditText nicText;
    EditText passwordTxt;
    Button loginBtn;
    TextView goToRegisterBtn;



    String TAG="LoginActivity";

    //session preparing for Set the values
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        init();

        loginBtn.setOnClickListener(v -> {


            LoginWithDatabase();
        });

        goToRegisterBtn.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            finish();
        });
    }

    private void LoginWithDatabase() {

        // ---------------------------------Validation Part------------------------------------------
        if (TextUtils.isEmpty(nicText.getText().toString())) {
            nicText.setError("Nic is required !");
            return;
        }
        if(passwordTxt.length()<8 &&!isValidPassword(passwordTxt.getText().toString())){
            passwordTxt.setError( "Password must be 8 characters" );
            return;
        }

        RequestBody requestBody = new MultipartBody.Builder()
                .setType( MultipartBody.FORM )
                .addFormDataPart( "nic", nicText.getText().toString())
                .addFormDataPart( "password", passwordTxt.getText().toString())
                .build();

        Request request = new Request.Builder()
                .url(URL_OF_DB.getInstance().userLoginUrl)
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

                        LoginActivity.this.runOnUiThread(() -> {
                            try {
                                String JSON_STRING = "["+myResponse+"]";
                                JSONArray mJsonArray = new JSONArray(JSON_STRING);
                                JSONObject mJsonObject = mJsonArray.getJSONObject(0);
                                String message = mJsonObject.getString( "message" );




                                if(message.equals("Authentication Success")){
                                    String uid = mJsonObject.getString( "uid" );
                                    String nic = mJsonObject.getString( "nic" );
//                                    Log.e( TAG+"uid", uid );
//                                    Log.e( TAG+"nic", nic );
                                    sharedpreferences =getSharedPreferences("user_details", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedpreferences.edit();
                                    editor.putBoolean("isLoggedIn",true);
                                    editor.putString("uid",uid);
                                    editor.putString("nic",nic);
                                    editor.commit();




                                    startActivity(new Intent(LoginActivity.this, Nav_menu.class));
                                    finish();

                                }
                                dialogBox(message);




                            }catch (Exception e){

                                Log.e( TAG, e.getMessage() );

                                // dialogBox(e.getMessage());

                            }
                        });






                    }catch ( Exception e){

                        Log.e( TAG, e.getMessage() );

                    }
                }


            }
        });
    }

    private void init() {
        nicText=findViewById(R.id.nicText);
        passwordTxt=findViewById(R.id.passwordTxt);
        loginBtn=findViewById(R.id.loginBtn);
        goToRegisterBtn=findViewById(R.id.goToRegisterBtn);
    }


    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }




    public void dialogBox(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
        alertDialog.setTitle("Alert Box");
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
            }
        });

        alertDialog.show();


    }
}