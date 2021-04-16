package com.example.coivd_app;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.coivd_app.Connection.OkHttpSingleton;
import com.example.coivd_app.Connection.URL_OF_DB;
import com.example.coivd_app.Screens.User.HomeFragment;
import com.example.coivd_app.Screens.User.ProfileFragment;
import com.example.coivd_app.Screens.User.QRActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Nav_menu extends AppCompatActivity {


    String TAG="nav_menu";
    //side bar
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView menuIcon;
    LinearLayout contentView;
    ActionBarDrawerToggle toggle;



    ImageView navPro;
    TextView naUsername;


    FloatingActionButton floatingActionButton;


    //session for get values
    SharedPreferences sharedpreferences;

     private  String CURRENT_UID="34";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_main);
        init();

        navigationDrawer();

        openFragment(new HomeFragment());
    }

    private void init() {
        //declare a session
        sharedpreferences=getSharedPreferences("user_details",MODE_PRIVATE);
        CURRENT_UID=sharedpreferences.getString("uid","");


        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        menuIcon = findViewById(R.id.menu_icon);
        contentView = findViewById(R.id.content);
        floatingActionButton = findViewById(R.id.floatingActionButton);


        //inlize the side bar

        View header=navigationView.getHeaderView( 0 );
        naUsername=header.findViewById( R.id.navBar_name );
        navPro=header.findViewById( R.id.navPro );




        floatingActionButton.setOnClickListener(v -> {


            startActivity(new Intent(Nav_menu.this, QRActivity.class));

        });

    }


    private void navigationDrawer() {

        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.home:
                    navigationView.setCheckedItem(R.id.home);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    openFragment(new HomeFragment());
                    break;


                case R.id.profile:
                    navigationView.setCheckedItem(R.id.profile);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    openFragment(new ProfileFragment());

                    break;

                case R.id.nav_logout:
                    navigationView.setCheckedItem(R.id.nav_logout);
                    drawerLayout.closeDrawer(GravityCompat.START);


                    LogOut();

                    break;

            }
            return false;
        });

        menuIcon.setOnClickListener(v -> {
            if (drawerLayout.isDrawerVisible(GravityCompat.START))
                drawerLayout.closeDrawer(GravityCompat.START);
            else drawerLayout.openDrawer(GravityCompat.START);
        });

        // animateNavigationDrawer();
    }


    private void LogOut() {


        //sessions clear when user log out
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean("isLoggedIn",false);
        editor.putString("nic","");
        editor.putString("uid","");


        editor.clear();
        editor.commit();


        startActivity(new Intent(Nav_menu.this, SplashActivity.class));
        finish();


    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStart() {
        super.onStart();

        LoadDataFromDB();


    }


    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
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

                        Nav_menu.this.runOnUiThread(() -> {
                            try {
                                String JSON_STRING = "["+myResponse+"]";
                                JSONArray mJsonArray = new JSONArray(JSON_STRING);
                                JSONObject mJsonObject = mJsonArray.getJSONObject(0);
                                String nic = mJsonObject.getString( "nic" );

                                naUsername.setText(mJsonObject.getString( "name" )+"");


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

}