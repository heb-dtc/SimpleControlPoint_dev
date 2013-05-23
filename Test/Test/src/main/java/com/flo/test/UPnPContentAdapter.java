package com.flo.test;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by florent.noel on 5/23/13.
 */
public class UPnPContentAdapter extends BaseAdapter {

    private Activity mActivity;
    private ArrayList<UPnPContent> mData = new ArrayList<UPnPContent>();
    private static LayoutInflater mInflater = null;

    public UPnPContentAdapter(Activity a){
        mActivity = a;
        mInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void updateDeviceList(ArrayList<UPnPContent> items){
        mData.clear();
        mData.addAll(items);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return  mData.get(i);
   }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        //if needed, inflate the view
        if(view == null)
            view = mInflater.inflate(R.layout.dms_content_list_row, null);

        //find the views we need
        TextView contentTitle = (TextView) view.findViewById(R.id.content_name);
        ImageView deviceIcon = (ImageView) view.findViewById(R.id.device_icon);

        //fill the views with the values
        UPnPContent content = mData.get(i);

        contentTitle.setText(content.getTitle());
        //deviceIcon.setImageBitmap(dd.getDevice().getIcons().);

        return view;
    }
}
