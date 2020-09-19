package com.rosa.camp.viewModel;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
//import androidx.databinding.library.baseAdapters.BR;
import com.rosa.ContextCamp;
import com.rosa.camp.BR;
import com.rosa.camp.model.Camp;
import com.rosa.camp.repository.remote.PreferenceHelper;
import com.rosa.camp.ui.adapter.PrefernceHelperCamp;

public class CampViewModel extends BaseObservable {

    private String campName;
    private String campDescription;
    private String campAddress;
    private String campCity;
    private String campTell;
    private String campImage;
    private boolean campParking;
    private String campCost;
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

    public CampViewModel (){
        camp=new Camp("","","","","",false,"",false,false,false,false,false,false,false,false);
        context= ContextCamp.getAppContext();
    }

    public void onClick(View view){

        registerCamp(context);
    }

    public void registerCamp(Context context) {
        preferenceHelper = new PrefernceHelperCamp(context);
        preferenceHelper.putNAME(getCampName());
        preferenceHelper.putDescription(getCampDescription());
        preferenceHelper.putAddress(getCampAddress());
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

        Log.i("PreferenceHelper", getCampName());
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
