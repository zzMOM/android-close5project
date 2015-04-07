package com.example.close5project.data;

/**
 * Created by weiwu on 4/4/15.
 */
public class ItemObject {
    private String itemId, sellerId;

    public ItemObject() {}

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }


    public String getSellerId() {
        return sellerId;
    }

    public String getItemId() {
        return itemId;
    }
}
