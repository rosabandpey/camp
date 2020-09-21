package com.rosa.camp.ui.adapter;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefernceHelperCamp {

    private final String NAME="name";
    private final String DESCRIPTION="description";
    private final String TELL="tell";
    private final String ADDRESS="address";
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
    private  final String IMAGE="image";

    private SharedPreferences sharedPreferences;
    private Context context;

    public PrefernceHelperCamp(Context context){

        sharedPreferences=context.getSharedPreferences("shareCamp",Context.MODE_PRIVATE);

        this.context = context;
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


    public void putAddress(String address){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(ADDRESS,address);
        editor.commit();
    }

    public String getADDRESS(){
        return  sharedPreferences.getString(ADDRESS,"");
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
