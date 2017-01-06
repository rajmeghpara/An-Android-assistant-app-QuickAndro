package com.rarity.apps.quickandro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Premang on 07-Aug-16.
 */
public class DrawerListAdapter extends BaseAdapter{

    private Context context;
    private String titles[];
    private int imageRes[] = {R.mipmap.drawer_call, R.mipmap.drawer_calc, R.mipmap.drawer_openapp, R.mipmap.drawer_msg, R.mipmap.drawer_profile, R.mipmap.drawer_search, R.mipmap.drawer_onoff, R.mipmap.drawer_alarm};
    private String description[];

    public DrawerListAdapter(Context context){
        this.context = context;
        titles = context.getResources().getStringArray(R.array.drawer_items);
        description = context.getResources().getStringArray(R.array.drawer_description);
    }

    @Override
    public int getCount(){
        return titles.length;
    }

    @Override
    public View getView(int position, View convert, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View root = inflater.inflate(R.layout.drawer_list_item, parent, false);

        TextView title = (TextView) root.findViewById(R.id.title);
        ImageView image = (ImageView) root.findViewById(R.id.image);

        title.setText(titles[position]);
        image.setImageResource(imageRes[position]);

        return root;
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public Object getItem(int position){
        return new String[]{titles[position], description[position]};
    }

}
