package com.teeterpal.androidteeterpal.data;

import java.util.Date;

/**
 * Created by weiwu on 3/17/15.
 */
public class EventObject {
    private boolean hasProfile = true;
    private double latitude = 0.0, longitude = 0.0;
    private Date startAt = null, endAt = null;
    private String address = "", title = "", visibility = "", category = "";
    private String age = "", keywords = "", description = "";
    private String userId = "";
    private byte[] image = null;

    public EventObject(){}

    public EventObject(boolean hasProfile,  String userId, String title, String category,
                       String visibility, String address, Date startAt, Date endAt, String age,
                       String keywords, String description, double latitude, double longitude,
                       byte[] image){
        this.hasProfile = hasProfile;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.startAt = startAt;
        this.endAt = endAt;
        this.title = title;
        this.visibility = visibility;
        this.category = category;
        this.age = age;
        this.keywords = keywords;
        this.description = description;
        this.userId = userId;
        this.image = image;
    }

    public void setHasProfile(boolean hasProfile) {
        this.hasProfile = hasProfile;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setStartAt(Date startAt) {
        this.startAt = startAt;
    }

    public void setEndAt(Date endAt) {
        this.endAt = endAt;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setVisibility(String visibility){
        this.visibility = visibility;
    }

    public void setCategory(String category){
        this.category = category;
    }

    public void setAge(String age){
        this.age = age;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public boolean getHasProfile() {
        return hasProfile;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getAddress() {
        return address;
    }

    public Date getStartAt() {
        return startAt;
    }

    public Date getEndAt() {
        return endAt;
    }

    public String getTitle() {
        return title;
    }

    public String getVisibility() {
        return visibility;
    }

    public String getCategory() {
        return category;
    }

    public String getAge() {
        return age;
    }

    public String getKeywords() {
        return keywords;
    }

    public String getDescription() {
        return description;
    }

    public String getUserId() {
        return userId;
    }

    public byte[] getImage() {
        return image;
    }


    @Override
    public String toString(){
        return new StringBuilder().append("latitude: ").append(latitude).append(", ")
                .append("longitude: ").append(longitude).append(", ")
                .append("address: ").append(address).append(", ")
                .append("start date & time: ").append(startAt).append(", ")
                .append("end date & time: ").append(endAt).append(", ")
                .append("title: ").append(title).append(", ")
                .append("visibility: ").append(visibility).append(", ")
                .append("category: ").append(category).append(", ")
                .append("age: ").append(age).append(", ")
                .append("keywords: ").append(keywords).append(", ")
                .append("description: ").append(description).append(", ")
                .append("ownerId: ").append(userId).append(";").toString();
    }
}
