package com.flo.test;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by florent.noel on 5/22/13.
 */
public class UPnPDeviceAdapter extends BaseAdapter {

    private Activity mActivity;
    private ArrayList<DeviceDisplay> mData = new ArrayList<DeviceDisplay>();
    private static LayoutInflater mInflater = null;

    public UPnPDeviceAdapter(Activity a){
        mActivity = a;
        mInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void updateDeviceList(ArrayList<DeviceDisplay> list){
        mData.clear();
        for(DeviceDisplay d : list){
            mData.add(d);
        }
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        //if needed, inflate the view
        if(view == null)
            view = mInflater.inflate(R.layout.device_list_row, null);

        //find the views we need
        TextView deviceNameView = (TextView) view.findViewById(R.id.device_friendly_name);
        ImageView deviceIcon = (ImageView) view.findViewById(R.id.device_icon);

        //fill the views with the values
        DeviceDisplay dd = mData.get(i);
        Bitmap img = new Bitmap();

        deviceNameView.setText(dd.toString());
        //deviceIcon.setImageBitmap(dd.getDevice().getIcons().);

        return view;
    }
}
