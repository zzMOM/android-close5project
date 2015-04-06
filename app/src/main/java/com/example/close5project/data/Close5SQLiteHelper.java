package com.example.close5project.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.close5project.data.SQLiteContract.ItemEntry;
import com.example.close5project.data.SQLiteContract.SellerEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weiwu on 4/4/15.
 */
public class Close5SQLiteHelper extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "close5.db";

    public Close5SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_SELLER_TABLE = "CREATE TABLE " + SellerEntry.TABLE_NAME + " (" +
                SellerEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                SellerEntry.SELLER_ID + " TEXT NOT NULL, " +
                SellerEntry.SELLER_NAME + " TEXT NOT NULL, " +
                SellerEntry.SELLER_NUMBER_LISTINGS + " TEXT NOT NULL, " +
                SellerEntry.SELLER_PHOTO + " TEXT NOT NULL" + ");";

        final String SQL_CREATE_ITEM_TABLE = "CREATE TABLE " + ItemEntry.TABLE_NAME + " (" +
                ItemEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ItemEntry.ITEM_ID + " TEXT NOT NULL, " +
                ItemEntry.ITEM_SELLER_ID + " TEXT NOT NULL" + ");";

        sqLiteDatabase.execSQL(SQL_CREATE_SELLER_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_ITEM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SellerEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ItemEntry.TABLE_NAME);
        onCreate(db);
    }


    public long addSeller(SellerObject sellerObject) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SellerEntry.SELLER_ID, sellerObject.getSellerId());
        values.put(SellerEntry.SELLER_NAME, sellerObject.getSellerName());
        values.put(SellerEntry.SELLER_NUMBER_LISTINGS, sellerObject.getSellerListings());
        values.put(SellerEntry.SELLER_PHOTO, sellerObject.getSellerPhoto());

        long index = db.insert(SellerEntry.TABLE_NAME, null, values);
        db.close();
        return index;
    }

    public long addSellerItem(ItemObject itemObject) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ItemEntry.ITEM_ID, itemObject.getItemId());
        values.put(ItemEntry.ITEM_SELLER_ID, itemObject.getSellerId());
        long index = db.insert(ItemEntry.TABLE_NAME, null, values);
        db.close();
        return index;
    }

    public void deleteAllSeller() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(SellerEntry.TABLE_NAME, null, null);
        db.close();
    }

    public void deleteALLItem() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ItemEntry.TABLE_NAME, null, null);
        db.close();
    }

    public List<SellerObject> getSellerList() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<SellerObject> sellerObjectList = new ArrayList<SellerObject>();
        Cursor cursor  = db.query(SellerEntry.TABLE_NAME,
                null, null, null, null, null, null);
        if(cursor.moveToFirst()) {
            do {
                SellerObject sellerObject = new SellerObject();
                sellerObject.setSellerId(cursor.getString(1));
                sellerObject.setSellerName(cursor.getString(2));
                sellerObject.setSellerListings(cursor.getString(3));
                sellerObject.setSellerPhoto(cursor.getString(4));
                sellerObjectList.add(sellerObject);
            } while (cursor.moveToNext());
        }

        return sellerObjectList;
    }

    public List<String> getItemListsBySeller(String sellerId) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<String> itemList = new ArrayList<String>();
        Cursor cursor  = db.query(ItemEntry.TABLE_NAME,
                                  null,
                                  ItemEntry.ITEM_SELLER_ID + "=?",
                                  new String[]{ sellerId },
                                  null,
                                  null,
                                  null);
        if(cursor.moveToFirst()) {
            do {
                itemList.add(cursor.getString(1));

            } while (cursor.moveToNext());
        }

        return itemList;
    }
}
