package com.teeterpal.androidteeterpal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.teeterpal.androidteeterpal.data.EventObject;

import java.util.List;

/**
 * Created by weiwu on 2/21/15.
 */
public class DiscoverEventAdapter extends BaseAdapter {
    private Context context;
    private List<EventObject> eventObjectList;

    static class ViewHolder{
        public ImageView image;
        public TextView title, dateTime, keywords;
        public TextView mile, weekDay, popularity;
    }


    public DiscoverEventAdapter(Context context, List<EventObject> eventObjectList){
        this.context = context;
        this.eventObjectList = eventObjectList;
    }

    @Override
    public int getCount() {
        return eventObjectList.size();
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
            rowView = inflater.inflate(R.layout.event_list_item, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.image = (ImageView) rowView.findViewById(R.id.discover_event_image);
            viewHolder.title = (TextView) rowView.findViewById(R.id.discover_event_title);
            viewHolder.dateTime = (TextView) rowView.findViewById(R.id.discover_event_date_time);
            viewHolder.keywords = (TextView) rowView.findViewById(R.id.discover_event_keywords);
            viewHolder.mile = (TextView) rowView.findViewById(R.id.mile);
            viewHolder.weekDay = (TextView) rowView.findViewById(R.id.weekday);
            viewHolder.popularity = (TextView) rowView.findViewById(R.id.popularity);
            rowView.setTag(viewHolder);
        }

        DateTimeFormatHelper dtHelper = new DateTimeFormatHelper();
        ViewHolder viewHolder = (ViewHolder) rowView.getTag();
        EventObject event = eventObjectList.get(position);
        viewHolder.image.setImageResource(R.drawable.teeter);
        viewHolder.title.setText(event.getTitle());
        //viewHolder.dateTime.setText(event.getStartAt());
        viewHolder.keywords.setText(event.getKeywords());

        return rowView;
    }
}
