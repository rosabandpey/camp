package com.rosa.camp.viewModel;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import com.rosa.camp.model.Camp;

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

    public CampViewModel (){
        camp=new Camp("","","","","",false,"",false,false,false,false,false,false,false,false);
    }

    @Bindable
    public String getCampName() {
        return camp.getName();
    }

    public void setCampName(String campName) {
        camp.setName(campName);
        notifyPropertyChanged(BR.);
    }

    @Bindable
    public String getCampDescription() {
        return camp.getDescription();
    }

    public void setCampDescription(String campDescription) {
        camp.setDescription(campDescription);
    }

    @Bindable
    public String getCampAddress() {
        return camp.getAddress();
    }

    public void setCampAddress(String campAddress) {
       camp.setAddress(campAddress);
    }

    @Bindable
    public String getCampCity() {
        return camp.getCity();
    }

    public void setCampCity(String campCity) {
       camp.setCity(campCity);
    }

    @Bindable
    public String getCampTell() {
        return camp.getTell();
    }

    public void setCampTell(String campTell) {
        camp.setTell(campTell);
    }


    @Bindable
    public boolean isCampParking() {
        return camp.isParking();
    }

    public void setCampParking(boolean campParking) {
        camp.setParking(campParking);
    }

    @Bindable
    public String getCampCost() {
        return camp.getCost();
    }

    public void setCampCost(String campCost) {
        camp.setCost(campCost);
    }

    @Bindable
    public boolean isCampShowers() {
        return camp.isShowers();
    }

    public void setCampShowers(boolean campShowers) {
        camp.setShowers(campShowers);
    }

    @Bindable
    public boolean isCampGas() {
        return camp.isGas();
    }

    public void setCampGas(boolean campGas) {
        camp.setGas(campGas);
    }

    @Bindable
    public boolean isCampResturant() {
        return camp.isResturant();
    }

    public void setCampResturant(boolean campResturant) {
        camp.setResturant(campResturant);
    }

    @Bindable
    public boolean isCampWc() {
        return camp.isWc();
    }

    public void setCampWc(boolean campWc) {
        camp.setWc(campWc);
    }

    @Bindable
    public boolean isCampWifi() {
        return camp.isWifi();
    }

    public void setCampWifi(boolean campWifi) {
        camp.setWifi(campWifi);
    }

    @Bindable
    public boolean isCampWheelchairs() {
        return camp.isWheelchairs();
    }

    public void setCampWheelchairs(boolean campWheelchairs) {
        camp.setWheelchairs(campWheelchairs);
    }

    @Bindable
    public boolean isCampDrinkingWater() {
        return camp.isDrinkingWater();
    }

    public void setCampDrinkingWater(boolean campDrinkingWater) {
        camp.setDrinkingWater(campDrinkingWater);
    }

    @Bindable
    public boolean isCampAllowpets() {
        return camp.isAllowpets();
    }

    public void setCampAllowpets(boolean campAllowpets) {
        camp.setAllowpets(campAllowpets);
    }
}
