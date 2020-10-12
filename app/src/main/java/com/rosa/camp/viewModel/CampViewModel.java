package com.rosa.camp.viewModel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ToggleButton;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.fragment.app.FragmentTransaction;
//import androidx.databinding.library.baseAdapters.BR;
import com.rosa.ContextCamp;
import com.rosa.camp.BR;
import com.rosa.camp.R;
import com.rosa.camp.model.Camp;
import com.rosa.camp.ui.adapter.Addresslatlng;
import com.rosa.camp.ui.adapter.PrefernceHelperCamp;
import com.rosa.camp.view.AddressActivity;
import com.rosa.camp.view.DirectionFragment;
import com.rosa.camp.view.HomeActivity;
import com.rosa.camp.view.MapActivity;
import com.rosa.camp.view.MapFragment;
import com.rosa.camp.view.RegisterCampActivity;

import java.util.ArrayList;
import java.util.List;

public class CampViewModel extends BaseObservable  {

    private String campName;
    private String campDescription;
    private double campAddressLatitude;
    private double campAddressLongtitude;
    private String campAddress;
    private String campCity;
    private String campTell;
    private String campimg;
    private String campCost;
    private boolean campParking;
    private boolean campShowers;
    private boolean campGas;
    private boolean campResturant;
    private boolean campWc;
    private boolean campWifi;
    private boolean campWheelchairs;
    private boolean campDrinkingWater;
    private boolean campAllowpets;
    public Camp camp;
    PrefernceHelperCamp preferenceHelper;
    Context context;
    private ImageView cImage;
    private Uri cImageUri;
    private static final int PICK_IMAGE_REQUEST = 0;
    private static final int LAUNCH_ADDRESS_ACTIVITY=1;
    private static final int LAUNCH_REGISTERCAMP_ACTIVITY=1;
    protected RegisterCampActivity activity;
    Activity host;
    public int locationCount;
    Addresslatlng addresslatlng;
    ArrayList<Double> listLatitude =new ArrayList<>();
    ArrayList<Double> listLongtitude =new ArrayList<>();


   /* public void permissionsCheck() {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            return;
        }
    } */


    @SuppressLint("RestrictedApi")
    public void imageSelect(View view) {
       // permissionsCheck();
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
        } else {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
        }
        intent.setType("image/*");


//
        host = (Activity) view.getContext();
        Log.i("getActivity", host.toString());
        host.startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }



    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == PICK_IMAGE_REQUEST) {

                // The user picked a image.
                // The Intent's data Uri identifies which item was selected.
                if (data != null) {

                    // This is the key line item, URI specifies the name of the data
                   // cImageUri = data.getData();
                    setCampimg(data.getData().toString());
                    // Saves image URI as string to Default Shared Preferences

                }
       //     }
        }
        Log.d("Longtitude", String.valueOf(requestCode));
        if (requestCode == LAUNCH_ADDRESS_ACTIVITY) {
            if (data != null) {
                Bundle extras = data.getExtras();
                if (extras == null) {
                    return;
                }
// get data via the key
                Double latitude = extras.getDouble("latitude", 0);
                Double longtitude = extras.getDouble("Longtitude", 0);
                String address=extras.getString("addressDescription","");

                if (latitude != null) {
                    // do something with the data
                    Log.d("latitude", latitude.toString());
                    setCampAddressLatitude(latitude);
                    addToListLatitude(latitude);
                }
                if (longtitude != null) {
                    // do something with the data
                    Log.d("Longtitude", longtitude.toString());
                    setCampAddressLongtitude(longtitude);
                    addToListLatitude(longtitude);
                }
                if (address != null) {
                    // do something with the data
                    Log.d("address", address);
                    setCampAddress(address);
                }
            }
        }
    }



    public CampViewModel (){
        camp=new Camp("","","",0,0,"","","false","",false,false,false,false,false,false,false,false,false);
        context= ContextCamp.getAppContext();
        preferenceHelper = PrefernceHelperCamp.instanceCamp(context);
        addresslatlng=Addresslatlng.getAddressInstance();
    }




    public void saveButton(View view){

        registerCamp(view);

    }

    public void clickToggle(View view){

        boolean on = ((ToggleButton) view).isChecked();

        if (on) {
            view.setBackgroundColor(Color.GREEN);
            Log.d("toggle","toggle");
            // Enable vibrate
        } else {
            view.setBackgroundColor(Color.RED);
            // Disable vibrate
        }
    }

    public void addToListLatitude(double item){

            listLatitude.add(item);

    }

    public void addToLisLongtitude(double item){

        listLongtitude.add(item);

    }


    public void imageSelectButton(View view){

    imageSelect(view);

    }

    public void selectAddress(View view){

        host = (Activity) view.getContext();
        Intent i = new Intent(host, AddressActivity.class);
        host.startActivityForResult(i,LAUNCH_ADDRESS_ACTIVITY);

    }



    public void registerCamp(View view) {

        locationCount++;

        preferenceHelper.putLocationCount(locationCount);
        preferenceHelper.putNAME(getCampName());
        preferenceHelper.putDescription(getCampDescription());
        preferenceHelper.putADDRESS(getCampAddress());
        preferenceHelper.putAddressLatitude(getCampAddressLatitude());
        addresslatlng.setLatitude1(listLatitude);
        preferenceHelper.putAddressLongtitude(getCampAddressLongtitude());
        addresslatlng.setLongtitude1(listLongtitude);
        preferenceHelper.putCITY(getCampCity());
        preferenceHelper.putCOST(getCampCost());
        preferenceHelper.putPARKING(isCampParking());
        preferenceHelper.putRESTURAUNT(isCampResturant());
        preferenceHelper.putSHOWERS(isCampShowers());
        preferenceHelper.putGas(isCampGas());
        preferenceHelper.putWC(isCampWc());
        preferenceHelper.putWIFI(isCampWifi());
        preferenceHelper.putWHEELCHAIRS(isCampWheelchairs());
        preferenceHelper.putDRINKINGWATER(isCampDrinkingWater());
        preferenceHelper.putALLOWPETS(isCampAllowpets());
        preferenceHelper.putIMAGE(getCampimg());

        Log.i("PreferenceHelper", getCampimg());
        Log.i("locationCount",String.valueOf(addresslatlng.getLatitude1().size()) );

        Intent i = new Intent(context, HomeActivity.class);
        i.putExtra("Longtitude",listLongtitude);
        i.putExtra("latitude",listLongtitude);
        host = (Activity) view.getContext();
        host.setResult(LAUNCH_REGISTERCAMP_ACTIVITY,i);
        host.finish();

    }

    @Bindable
    public String getCampimg() {
        return camp.getImage();
    }

    public void setCampimg(String campimg) {
        camp.setImage(campimg);
        notifyPropertyChanged(BR.campimg);
    }


    @Bindable
    public String getCampName() {
        return camp.getName();
    }

    public void setCampName(String campName) {
        camp.setName(campName);
        notifyPropertyChanged(BR.campName);
    }

    @Bindable
    public String getCampDescription() {
        return camp.getDescription();
    }

    public void setCampDescription(String campDescription) {
        camp.setDescription(campDescription);
        notifyPropertyChanged(BR.campDescription);
    }

    @Bindable
    public String getCampAddress() {
        return camp.getAddress();
    }

    public void setCampAddress(String campAddress) {
        camp.setAddress(campAddress);
        notifyPropertyChanged(BR.campAddress);
    }

    @Bindable
    public double getCampAddressLatitude() {
        return camp.getAddressLatitude();
    }

    public void setCampAddressLatitude(double campAddressLatitude) {
        camp.setAddressLatitude(campAddressLatitude);
    }

    @Bindable
    public double getCampAddressLongtitude() {
        return camp.getAddressLongtitude();
    }

    public void setCampAddressLongtitude(double campAddressLongtitude) {
        camp.setAddressLongtitude(campAddressLongtitude);
    }


    @Bindable
    public String getCampCity() {
        return camp.getCity();
    }

    public void setCampCity(String campCity) {
       camp.setCity(campCity);
        notifyPropertyChanged(BR.campCity);
    }

    @Bindable
    public String getCampTell() {
        return camp.getTell();
    }

    public void setCampTell(String campTell) {
        camp.setTell(campTell);
        notifyPropertyChanged(BR.campTell);
    }


    @Bindable
    public boolean isCampParking() {
        return camp.isParking();
    }

    public void setCampParking(boolean campParking) {
        camp.setParking(campParking);
        notifyPropertyChanged(BR.campParking);
    }

    @Bindable
    public String getCampCost() {
        return camp.getCost();
    }

    public void setCampCost(String campCost) {
        camp.setCost(campCost);
        notifyPropertyChanged(BR.campCost);
    }

    @Bindable
    public boolean isCampShowers() {
        return camp.isShowers();
    }

    public void setCampShowers(boolean campShowers) {
        camp.setShowers(campShowers);
        notifyPropertyChanged(BR.campShowers);
    }

    @Bindable
    public boolean isCampGas() {
        return camp.isGas();
    }

    public void setCampGas(boolean campGas) {
        camp.setGas(campGas);
        notifyPropertyChanged(BR.campGas);
    }

    @Bindable
    public boolean isCampResturant() {
        return camp.isResturant();
    }

    public void setCampResturant(boolean campResturant) {
        camp.setResturant(campResturant);
        notifyPropertyChanged(BR.campResturant);
    }

    @Bindable
    public boolean isCampWc() {
        return camp.isWc();
    }

    public void setCampWc(boolean campWc) {
        camp.setWc(campWc);
        notifyPropertyChanged(BR.campWc);
    }

    @Bindable
    public boolean isCampWifi() {
        return camp.isWifi();
    }

    public void setCampWifi(boolean campWifi) {
        camp.setWifi(campWifi);
        notifyPropertyChanged(BR.campWifi);
    }

    @Bindable
    public boolean isCampWheelchairs() {
        return camp.isWheelchairs();
    }

    public void setCampWheelchairs(boolean campWheelchairs) {
        camp.setWheelchairs(campWheelchairs);
        notifyPropertyChanged(BR.campWheelchairs);
    }

    @Bindable
    public boolean isCampDrinkingWater() {
        return camp.isDrinkingWater();
    }

    public void setCampDrinkingWater(boolean campDrinkingWater) {
        camp.setDrinkingWater(campDrinkingWater);
        notifyPropertyChanged(BR.campDrinkingWater);
    }

    @Bindable
    public boolean isCampAllowpets() {
        return camp.isAllowpets();
    }

    public void setCampAllowpets(boolean campAllowpets) {
        camp.setAllowpets(campAllowpets);
        notifyPropertyChanged(BR.campAllowpets);
    }
}
