package com.rosa.camp.ui.adapter;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefernceHelperCamp {

    private final String NAME="name";
    private final String DESCRIPTION="description";
    private final String TELL="tell";
    private final String ADDRESS="address";
    private final String ADDRESSLATITUDE="addressLatitude";
    private final String ADDRESSLONGTITUDE="addressLongtitude";
    private final String CITY="city";
    private final String PARKING="parking";
    private final String COST="cost";
    private final String SHOWERS="showers";
    private final String GAS="gas";
    private final String RESTURANT="resturant";
    private final String WC="wc";
    private final String WIFI="wifi";
    private final String WHEELCHAIRS="wheelchairs";
    private final String DRINKINGWATER="drinkingWater";
    private final String ALLOWPETS="allowpets";
    private final String IMAGE="image";
    private final String LOCATIONCOUNT="locationCount";

    private SharedPreferences sharedPreferences;
    private Context context;

    public PrefernceHelperCamp(Context context){

        sharedPreferences=context.getSharedPreferences("shareCamp",Context.MODE_PRIVATE);

        this.context = context;
    }


    public void putLocationCount(int locationCount){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putInt(LOCATIONCOUNT,locationCount);
        editor.commit();

    }

    public int getLocationCount(){

        return sharedPreferences.getInt(LOCATIONCOUNT,0);
    }

    public void putIMAGE(String image){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("image", image);
        editor.commit();

    }

    public String getIMAGE(){

        return sharedPreferences.getString(IMAGE,"");
    }

    public void putNAME(String name){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(NAME,name);
        editor.commit();
    }

    public String getName(){
        return  sharedPreferences.getString(NAME,"");
    }

    public void putDescription(String description){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(DESCRIPTION,description);
        editor.commit();
    }

    public String getDESCRIPTION(){
        return  sharedPreferences.getString(DESCRIPTION,"");
    }


    public String getADDRESS(String address) {
        return  sharedPreferences.getString(ADDRESS,"");
    }

    public void putADDRESS(String adress) {
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(ADDRESS,adress);
        editor.commit();
    }

    public void putAddressLatitude(double addressLatitude){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putLong(ADDRESSLATITUDE,Double.doubleToRawLongBits(addressLatitude));
        editor.commit();
    }

    public Long getADDRESSLatitude(){
        return  sharedPreferences.getLong(ADDRESSLATITUDE,Double.doubleToLongBits(0));
    }

    public void putAddressLongtitude(double addressLongtitude){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putLong(ADDRESSLONGTITUDE,Double.doubleToRawLongBits(addressLongtitude));
        editor.commit();
    }

    public long getADDRESSLongtitude(){
        return  sharedPreferences.getLong(ADDRESSLONGTITUDE,Double.doubleToLongBits(0));
    }


    public void putTELL(String tell){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(TELL,tell);
        editor.commit();
    }

    public String getTELL(){
        return  sharedPreferences.getString(TELL,"");
    }

    public void putCOST(String cost){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(COST,cost);
        editor.commit();
    }

    public String getCOST(){
        return  sharedPreferences.getString(COST,"");
    }

    public void putCITY(String birth){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(CITY,birth);
        editor.commit();
    }

    public String getCITY(){
        return  sharedPreferences.getString(CITY,"");
    }


    public void putPARKING(Boolean parking)
    {
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean(PARKING,parking);
        editor.commit();
    }

    public Boolean IsPARKING(){

        return sharedPreferences.getBoolean(PARKING,false);
    }


    public void putSHOWERS(Boolean showers)
    {
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean(SHOWERS,showers);
        editor.commit();
    }

    public Boolean IsSHOWERS(){

        return sharedPreferences.getBoolean(SHOWERS,false);
    }

    public void putGas(Boolean gas)
    {
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean(GAS,gas);
        editor.commit();
    }

    public Boolean IsGas(){

        return sharedPreferences.getBoolean(GAS,false);
    }

    public void putRESTURAUNT(Boolean restaurant)
    {
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean(RESTURANT,restaurant);
        editor.commit();
    }

    public Boolean IsRESTURAUNT(){

        return sharedPreferences.getBoolean(RESTURANT,false);
    }

    public void putWC(Boolean wc)
    {
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean(WC,wc);
        editor.commit();
    }

    public Boolean IsWC(){

        return sharedPreferences.getBoolean(WC,false);
    }

    public void putWIFI(Boolean wifi)
    {
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean(WIFI,wifi);
        editor.commit();
    }

    public Boolean IsWIFI(){

        return sharedPreferences.getBoolean(WIFI,false);
    }


    public void putWHEELCHAIRS(Boolean wheelchairs)
    {
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean(WHEELCHAIRS,wheelchairs);
        editor.commit();
    }

    public Boolean IsWHEELCHAIRS(){

        return sharedPreferences.getBoolean(WHEELCHAIRS,false);
    }

    public void putDRINKINGWATER(Boolean drinkingWater)
    {
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean(DRINKINGWATER,drinkingWater);
        editor.commit();
    }

    public Boolean IsDRINKINGWATER(){

        return sharedPreferences.getBoolean(DRINKINGWATER,false);
    }

    public void putALLOWPETS(Boolean allowpets)
    {
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean(DRINKINGWATER,allowpets);
        editor.commit();
    }

    public Boolean IsALLOWPETS(){

        return sharedPreferences.getBoolean(DRINKINGWATER,false);
    }

}
