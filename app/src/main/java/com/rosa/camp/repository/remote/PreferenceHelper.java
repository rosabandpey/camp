package com.rosa.camp.repository.remote;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceHelper {

    private final String INTRO="intro";
    private final String NAME="name";
    private final String FAMILY="family";
    private final String PHONE="phone";
    private final String EMAIL="email";
    private final String PASSWORD="password";
    private final String BIRTHDATE="Birth";
    private final String SEX="sex";

    private SharedPreferences sharedPreferences;
    private Context context;

    public PreferenceHelper(Context context){

        sharedPreferences=context.getSharedPreferences("shared",Context.MODE_PRIVATE);
        this.context = context;
    }

    public void putIsLogin(Boolean loginornot)
    {
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean(INTRO,loginornot);
        editor.commit();
    }

    public Boolean getIsLogin(){

        return sharedPreferences.getBoolean(INTRO,false);
    }

    public void putNAME(String name){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(NAME,name);
        editor.commit();
    }

    public String getName(){
        return  sharedPreferences.getString(NAME,"");
    }

    public void putFamily(String family){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(FAMILY,family);
        editor.commit();
    }

    public String getFamily(){
        return  sharedPreferences.getString(FAMILY,"");
    }


    public void putEmail(String email){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(EMAIL,email);
        editor.commit();
    }

    public String getEmail(){
        return  sharedPreferences.getString(EMAIL,"");
    }

    public void putPhone(String phone){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(PHONE,phone);
        editor.commit();
    }

    public String getPhone(){
        return  sharedPreferences.getString(PHONE,"");
    }

    public void putSex(String sex){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(SEX,sex);
        editor.commit();
    }

    public String getSex(){
        return  sharedPreferences.getString(SEX,"");
    }

    public void putBirth(String birth){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(BIRTHDATE,birth);
        editor.commit();
    }

    public String getBIRTHDATE(){
        return  sharedPreferences.getString(BIRTHDATE,"");
    }


}
