package com.rosa.camp.ui.adapter;



import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.rosa.ContextCamp;
import com.rosa.camp.ui.adapter.PrefernceHelperCamp;

import java.util.List;

public class Addresslatlng {


    private List<Double> latitude1 ;
    private List<Double> longtitude1;
    private PrefernceHelperCamp prefernceHelperCamp;
    private Context context;
    private static Addresslatlng addresslatlng;



    public static Addresslatlng getAddressInstance(){
       if (addresslatlng==null)
       {
           addresslatlng=new Addresslatlng();
       }
       return  addresslatlng;
    }

    public List<Double> getLatitude1() {
        return latitude1;

    }


    public void setLatitude1(double latitude1) {
        this.latitude1.add(latitude1)  ;
    }

    public List<Double> getLongtitude1() {
        return longtitude1;


    }

    public void setLongtitude1(double longtitude1) {
        this.longtitude1.add(longtitude1) ;
    }






}
