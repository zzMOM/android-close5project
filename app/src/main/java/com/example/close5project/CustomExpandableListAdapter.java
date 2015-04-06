package com.example.close5project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.close5project.data.Close5SQLiteHelper;
import com.example.close5project.data.SellerObject;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weiwu on 4/5/15.
 */
public class CustomExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<SellerObject> sellerList;
    private Close5SQLiteHelper dbHelper;
    private LayoutInflater inflater;

    public CustomExpandableListAdapter(Context context, List<SellerObject> sellerList) {
        this.context = context;
        this.sellerList = sellerList;
        dbHelper = new Close5SQLiteHelper(context);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getGroupCount() {
        return sellerList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        // Expandable list always expand
        ExpandableListView expandableListView = (ExpandableListView) parent;
        expandableListView.expandGroup(groupPosition);

        View groupView = convertView;
        if(groupView == null) {
            groupView = inflater.inflate(R.layout.list_section_header, parent, false);
        }

        TextView sellerName = (TextView) groupView.findViewById(R.id.seller_name);
        TextView numberOfListing = (TextView) groupView.findViewById(R.id.number_of_listings);
        RoundedImageView profileImage = (RoundedImageView) groupView.findViewById(R.id.profile_round_image);

        SellerObject sellerObject = sellerList.get(groupPosition);
        sellerName.setText(sellerObject.getSellerName());
        numberOfListing.setText(sellerObject.getSellerListings() + " listings");

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(sellerObject.getSellerPhoto(), profileImage);
        return groupView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        SellerObject sellerObject = sellerList.get(groupPosition);
        View childView = null;

        int itemNumber = Integer.parseInt(sellerObject.getSellerListings());

        List<String> itemList = new ArrayList<String>();
        itemList = dbHelper.getItemListsBySeller(sellerObject.getSellerId());


        if(itemNumber == 1) {
            childView = itemNumber1(childView, sellerObject, itemList);
        } else if(itemNumber == 2) {
            childView = itemNumber2(childView, sellerObject, itemList);
        } else if(itemNumber == 3) {
            childView = itemNumber3(childView, sellerObject, itemList);
        } else if(itemNumber >= 4) {
            childView = itemNumber4(childView, sellerObject, itemList);
        }

        return childView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }


    private View itemNumber1(View childView, SellerObject sellerObject, List<String> itemList) {
        if(childView == null) {
            childView = inflater.inflate(R.layout.item_view_one, null);
        }
        ImageView image1 = (ImageView) childView.findViewById(R.id.one_image_1);
        ImageLoader imageLoader = ImageLoader.getInstance();
        String imageUrl = getItemPhotoUrl(itemList.get(0));
        imageLoader.displayImage(imageUrl, image1);
        return childView;
    }

    private View itemNumber2(View childView, SellerObject sellerObject, List<String> itemList) {
        if(childView == null) {
            childView = inflater.inflate(R.layout.item_view_two, null);
        }
        ImageView image1 = (ImageView) childView.findViewById(R.id.two_image_1);
        ImageView image2 = (ImageView) childView.findViewById(R.id.two_image_2);

        ImageLoader imageLoader = ImageLoader.getInstance();
        String imageUrl = getItemPhotoUrl(itemList.get(0));
        imageLoader.displayImage(imageUrl, image1);
        imageUrl = getItemPhotoUrl(itemList.get(1));
        imageLoader.displayImage(imageUrl, image2);
        return childView;
    }

    private View itemNumber3(View childView, SellerObject sellerObject, List<String> itemList) {
        if(childView == null) {
            childView = inflater.inflate(R.layout.item_view_three, null);
        }
        ImageView image1 = (ImageView) childView.findViewById(R.id.three_image_1);
        ImageView image2 = (ImageView) childView.findViewById(R.id.three_image_2);
        ImageView image3 = (ImageView) childView.findViewById(R.id.three_image_3);

        ImageLoader imageLoader = ImageLoader.getInstance();
        String imageUrl = getItemPhotoUrl(itemList.get(0));
        imageLoader.displayImage(imageUrl, image1);
        imageUrl = getItemPhotoUrl(itemList.get(1));
        imageLoader.displayImage(imageUrl, image2);
        imageUrl = getItemPhotoUrl(itemList.get(2));
        imageLoader.displayImage(imageUrl, image3);
        return childView;
    }
    private View itemNumber4(View childView, SellerObject sellerObject, List<String> itemList) {
        if(childView == null) {
            childView = inflater.inflate(R.layout.item_view_four, null);
        }
        ImageView image1 = (ImageView) childView.findViewById(R.id.four_image_1);
        ImageView image2 = (ImageView) childView.findViewById(R.id.four_image_2);
        ImageView image3 = (ImageView) childView.findViewById(R.id.four_image_3);
        ImageView image4 = (ImageView) childView.findViewById(R.id.four_image_4);
        ImageLoader imageLoader = ImageLoader.getInstance();
        String imageUrl = getItemPhotoUrl(itemList.get(0));
        imageLoader.displayImage(imageUrl, image1);
        imageUrl = getItemPhotoUrl(itemList.get(1));
        imageLoader.displayImage(imageUrl, image2);
        imageUrl = getItemPhotoUrl(itemList.get(2));
        imageLoader.displayImage(imageUrl, image3);
        imageUrl = getItemPhotoUrl(itemList.get(3));
        imageLoader.displayImage(imageUrl, image4);
        return childView;
    }

    private String getItemPhotoUrl(String itemId) {
        StringBuffer buffer = new StringBuffer().append("https://images.close5.com/v1/items/")
                .append(itemId)
                .append("/image?number=0&width=1080&height=1080");
        return buffer.toString();
    }

}
