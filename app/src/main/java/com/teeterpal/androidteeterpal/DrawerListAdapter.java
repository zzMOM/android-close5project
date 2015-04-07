package com.teeterpal.androidteeterpal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by weiwu on 3/16/15.
 */
public class DrawerListAdapter extends BaseAdapter{
    private Context context;
    private int[] icons;
    private String[] values;
    private int resource;


    private static class ViewHolder{
        ImageView image;
        TextView text;
    }

    public DrawerListAdapter(Context context, int resource, int[] icons, String[] values) {
        this.context = context;
        this.resource = resource;
        this.icons = icons;
        this.values = values;
    }

    @Override
    public int getCount() {
        return values.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if(rowView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.drawer_list_item, parent, false);
            ViewHolder holder = new ViewHolder();
            holder.image = (ImageView) rowView.findViewById(R.id.drawer_row_image);
            holder.text = (TextView) rowView.findViewById(R.id.drawer_row_text);
            rowView.setTag(holder);
        }

        ViewHolder holder = (ViewHolder)rowView.getTag();
        holder.image.setImageResource(icons[position]);
        holder.text.setText(values[position]);

        return rowView;
    }
}
