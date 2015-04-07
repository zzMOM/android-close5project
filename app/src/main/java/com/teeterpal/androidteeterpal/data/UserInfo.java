package com.teeterpal.androidteeterpal.data;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("UserInfo")
public class UserInfo extends ParseObject {

    public UserInfo(){}

    public ParseUser getUser() {
        return getParseUser("user");
    }

    public void setParseUser(ParseUser user) {
        put("user", user);
    }

    public String getFullName() {
        return getString("fullName");
    }

    public void setFullName(String fullName) {
        put("fullName", fullName);
    }

    public String getZipcode() {
        return getString("zipcode");
    }

    public void setZipcode(String zipcode) {
        put("zipcode", zipcode);
    }

    public ParseFile getProfilePhoto() {
        return getParseFile("profilePhoto");
    }

    public void setProfilePhoto(ParseFile profile) {
        put("profilePhoto", profile);
    }

    public ParseFile getCoverPhoto() {
        return getParseFile("profilePhoto");
    }

    public void setCoverPhoto(ParseFile cover) {
        put("profilePhoto", cover);
    }

}
