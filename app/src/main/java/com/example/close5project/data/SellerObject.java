package com.example.close5project.data;

/**
 * Created by weiwu on 4/4/15.
 */
public class SellerObject {
    private String sellerId, sellerName, sellerPhoto, sellerListings;

    public SellerObject() {}

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public void setSellerPhoto(String sellerPhoto) {
        this.sellerPhoto = sellerPhoto;
    }

    public void setSellerListings(String sellerListings) {
        this.sellerListings = sellerListings;
    }

    public String getSellerId() {
        return sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public String getSellerPhoto() {
        return sellerPhoto;
    }

    public String getSellerListings() {
        return sellerListings;
    }
}
