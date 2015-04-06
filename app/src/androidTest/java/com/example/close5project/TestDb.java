package com.example.close5project;

import android.test.AndroidTestCase;
import android.util.Log;

import com.example.close5project.data.Close5SQLiteHelper;
import com.example.close5project.data.ItemObject;
import com.example.close5project.data.SellerObject;

import java.util.List;

/**
 * Created by weiwu on 4/4/15.
 */
public class TestDb extends AndroidTestCase {
    private static String LOG_TAG = TestDb.class.getSimpleName();
    private FetchData.FetchDataListener listener = null;

    static SellerObject createSellerValue() {
        SellerObject sellerObject = new SellerObject();
        sellerObject.setSellerId("12345");
        sellerObject.setSellerName("Alice");
        sellerObject.setSellerListings("7");
        sellerObject.setSellerPhoto("photo1");

        return sellerObject;
    }

    static ItemObject createItemValue() {
        ItemObject itemObject = new ItemObject();
        itemObject.setSellerId("12345");
        itemObject.setItemId("item1");
        return itemObject;
    }


    public void testInsertGetDb() {
        Close5SQLiteHelper dbHelper = new Close5SQLiteHelper(mContext);
        dbHelper.deleteAllSeller();
        dbHelper.deleteALLItem();

        SellerObject s = createSellerValue();
        long locationRowId = dbHelper.addSeller(s);
        // Verify we got a row back
        assertTrue(locationRowId != -1);
        Log.e(LOG_TAG, "New row id: " + locationRowId);

        ItemObject i = createItemValue();
        locationRowId = dbHelper.addSellerItem(i);
        // Verify we got a row back
        assertTrue(locationRowId != -1);
        Log.e(LOG_TAG, "New row id: " + locationRowId);

        List<SellerObject> sellerList = dbHelper.getSellerList();
        assertTrue(sellerList.size() == 1);
        assertEquals(s.getSellerId(), sellerList.get(0).getSellerId());

        List<String> itemList = dbHelper.getItemListsBySeller(s.getSellerId());
        assertTrue(itemList.size() == 1);
        assertEquals(i.getItemId(), itemList.get(0));


    }

}
