package com.example.coivd_app.Connection;

public class URL_OF_DB {

    private static URL_OF_DB singletonInstance;

    public static URL_OF_DB getInstance() {
        if (singletonInstance == null) {
            singletonInstance = new URL_OF_DB();
        }
        return singletonInstance;
    }


    //public  String BaseURL="http://192.168.1.10/CovidApp/api/";
    public  String BaseURL="https://apicovidapp.000webhostapp.com/CovidApp/api/";


    public String userRegisterUrl=BaseURL+"user_register.php";
    public String userLoginUrl=BaseURL+"user_auth.php";
    public String read_user_single_info=BaseURL+"read_user_single_info.php";
    public String user_update_info=BaseURL+"user_update_info.php";
    public String upload_image=BaseURL+"upload_image.php";


}
