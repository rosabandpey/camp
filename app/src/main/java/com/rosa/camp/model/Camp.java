package com.rosa.camp.model;

public class Camp {


    private String name;
    private String description;
    private String address;
    private double addressLatitude;
    private double addressLongtitude;
    private String city;
    private String tell;
    private String image;
    private boolean parking;
    private String cost;
    private boolean showers;
    private boolean gas;
    private boolean resturant;
    private boolean wc;
    private boolean wifi;
    private boolean wheelchairs;
    private boolean drinkingWater;
    private boolean allowpets;

    public Camp(String name
            , String description
            , String address
            , double addressLatitude
            , double addressLongtitude
            , String city
            , String tell
            , String image
            , String cost
            , boolean parking
            , boolean showers
            , boolean gas
            , boolean resturant
            , boolean wc
            , boolean wifi
            , boolean wheelchairs
            , boolean drinkingWater
            , boolean allowpets){
        this.name=name;
        this.description=description;
        this.address=address;
        this.addressLatitude=addressLatitude;
        this.addressLongtitude=addressLongtitude;
        this.city=city;
        this.cost=cost;
        this.tell=tell;
        this.image=image;
        this.parking=parking;
        this.showers=showers;
        this.gas=gas;
        this.resturant=resturant;
        this.wifi=wifi;
        this.wc=wc;
        this.allowpets=allowpets;
        this.wheelchairs=wheelchairs;
        this.drinkingWater=drinkingWater;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getAddressLatitude() {
        return addressLatitude;
    }

    public void setAddressLatitude(double addressLatitude) {
        this.addressLatitude = addressLatitude;
    }

    public double getAddressLongtitude() {
        return addressLongtitude;
    }

    public void setAddressLongtitude(double addressLongtitude) {
        this.addressLongtitude = addressLongtitude;
    }



    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTell() {
        return tell;
    }

    public void setTell(String tell) {
        this.tell = tell;
    }

    public boolean isParking() {
        return parking;
    }

    public void setParking(boolean parking) {
        this.parking = parking;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public boolean isShowers() {
        return showers;
    }

    public void setShowers(boolean showers) {
        this.showers = showers;
    }

    public boolean isGas() {
        return gas;
    }

    public void setGas(boolean gas) {
        this.gas = gas;
    }

    public boolean isResturant() {
        return resturant;
    }

    public void setResturant(boolean resturant) {
        this.resturant = resturant;
    }

    public boolean isWc() {
        return wc;
    }

    public void setWc(boolean wc) {
        this.wc = wc;
    }

    public boolean isWifi() {
        return wifi;
    }

    public void setWifi(boolean wifi) {
        this.wifi = wifi;
    }

    public boolean isWheelchairs() {
        return wheelchairs;
    }

    public void setWheelchairs(boolean wheelchairs) {
        this.wheelchairs = wheelchairs;
    }

    public boolean isDrinkingWater() {
        return drinkingWater;
    }

    public void setDrinkingWater(boolean drinkingWater) {
        this.drinkingWater = drinkingWater;
    }

    public boolean isAllowpets() {
        return allowpets;
    }

    public void setAllowpets(boolean allowpets) {
        this.allowpets = allowpets;
    }




}
