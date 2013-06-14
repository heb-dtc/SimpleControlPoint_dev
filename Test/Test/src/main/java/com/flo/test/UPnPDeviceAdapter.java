package com.flo.test;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.teleal.cling.model.meta.Icon;
import org.teleal.cling.model.meta.RemoteDeviceIdentity;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by florent.noel on 5/22/13.
 */
public class UPnPDeviceAdapter extends BaseAdapter {
    private static String TAG = UPnPDeviceAdapter.class.getName();

    private Activity mActivity;
    private ArrayList<DeviceDisplay> mData = new ArrayList<DeviceDisplay>();
    private HashMap<String, Bitmap> mDeviceIconsList = new  HashMap<String, Bitmap>();
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
        Icon[] listIcon = dd.getDevice().getIcons();

        RemoteDeviceIdentity id = (RemoteDeviceIdentity)dd.getDevice().getIdentity();
        String iconURL = "http://" + id.getDescriptorURL().getAuthority().toString();

        for(Icon ic : listIcon){
            if(ic.getHeight() == 120){
                iconURL += ic.getUri();
                break;
            }
        }

        Log.e(TAG, iconURL);

        String devName = dd.toString();

        if(!mDeviceIconsList.containsKey(devName)){
            new DownloadIDeviceIconTask(devName, deviceIcon).execute(iconURL);
        }

        deviceNameView.setText(devName);

        return view;
    }

    private class DownloadIDeviceIconTask extends AsyncTask<String, Void, Bitmap>{

        ImageView mView;
        String mDeviceName;

        DownloadIDeviceIconTask(String deviceName, ImageView view){
            mView = view;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            return ApplicationUtils.decodeSampledBitmapFromNetwork(urls[0], false, 0, 0);
        }

        protected void onPostExecute(Bitmap result) {
            mView.setImageBitmap(result);
            mDeviceIconsList.put(mDeviceName, result);
        }
    }
}
