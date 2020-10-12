package com.rosa.camp.ui.adapter;



import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.rosa.ContextCamp;
import com.rosa.camp.ui.adapter.PrefernceHelperCamp;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class Addresslatlng {


    private ArrayList<Double> latitude1 ;
    private ArrayList<Double> longtitude1;
    private PrefernceHelperCamp prefernceHelperCamp;
    private Context context;
    private static Addresslatlng addresslatlng;

    public Addresslatlng(){

    }

    public static Addresslatlng getAddressInstance(){
       if (addresslatlng==null)
       {
           addresslatlng=new Addresslatlng();
       }
       return  addresslatlng;
    }

    public ArrayList<Double> getLatitude1() {
        return latitude1;

    }

    public void setLatitude1(ArrayList<Double> latitude1) {
        this.latitude1 = latitude1;
    }

    public void setLongtitude1(ArrayList<Double> longtitude1) {
        this.longtitude1 = longtitude1;
    }



    public ArrayList<Double> getLongtitude1() {
        return longtitude1;


    }







}
