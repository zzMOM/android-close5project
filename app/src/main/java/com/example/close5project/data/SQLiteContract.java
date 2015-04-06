package com.example.close5project.data;

import android.provider.BaseColumns;

/**
 * Created by weiwu on 4/4/15.
 */
public class SQLiteContract {

    public static final class SellerEntry implements BaseColumns{
        // Table name
        public static final String TABLE_NAME = "seller";

        // Table fields
        public static final String SELLER_ID = "seller_id";
        public static final String SELLER_NAME = "name";
        public static final String SELLER_NUMBER_LISTINGS = "listings";
        public static final String SELLER_PHOTO = "photo";
    }

    public static final class ItemEntry implements BaseColumns{
        // Table name
        public static final String TABLE_NAME = "item";

        // Table fields
        public static final String ITEM_ID = "item_id";
        public static final String ITEM_SELLER_ID = "item_user";
    }
}
