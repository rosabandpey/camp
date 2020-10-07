package com.rosa.camp.ui.adapter;



import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.rosa.ContextCamp;
import com.rosa.camp.ui.adapter.PrefernceHelperCamp;

public class Addresslatlng {


    private double latitude1 ;
    private double longtitude1;
    private PrefernceHelperCamp prefernceHelperCamp;
    private Context context;

    public Addresslatlng(double latitude1,double longtitude1){


        this.latitude1=latitude1;
        this.longtitude1=longtitude1;
    }

    public double getLatitude1() {
        return latitude1;

    }

    public void setLatitude1(double latitude1) {
        this.latitude1 = latitude1;
    }

    public double getLongtitude1() {
        return longtitude1;


    }

    public void setLongtitude1(double longtitude1) {
        this.longtitude1 = longtitude1;
    }






}
