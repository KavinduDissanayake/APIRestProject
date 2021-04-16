package com.example.coivd_app.HelperClasss;

import java.io.Serializable;

public class ScannerModel  implements Serializable {

    private  String myResult;
    private  String current_lat;
    private  String current_lon;

    public ScannerModel() {
    }

    public String getMyResult() {
        return myResult;
    }

    public void setMyResult(String myResult) {
        this.myResult = myResult;
    }

    public String getCurrent_lat() {
        return current_lat;
    }

    public void setCurrent_lat(String current_lat) {
        this.current_lat = current_lat;
    }

    public String getCurrent_lon() {
        return current_lon;
    }

    public void setCurrent_lon(String current_lon) {
        this.current_lon = current_lon;
    }
}
