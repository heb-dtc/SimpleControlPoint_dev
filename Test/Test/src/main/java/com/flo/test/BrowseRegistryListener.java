package com.flo.test;

import android.util.Log;
import org.teleal.cling.model.meta.Device;
import org.teleal.cling.model.meta.LocalDevice;
import org.teleal.cling.model.meta.RemoteDevice;
import org.teleal.cling.model.types.DeviceType;
import org.teleal.cling.registry.DefaultRegistryListener;
import org.teleal.cling.registry.Registry;

import java.util.ArrayList;

/**
 * Created by Flo on 5/21/13.
 */
public class BrowseRegistryListener extends DefaultRegistryListener {
    private static String TAG = BrowseRegistryListener.class.getName();

    private ArrayList<Device> mDevicesList = new ArrayList<Device>();

    public ArrayList<Device> getDevicesList(){
        return mDevicesList;
    }

    public ArrayList<Device> getDMSList(){
        ArrayList<Device> list = new ArrayList<Device>();

        for(Device d : mDevicesList){
            DeviceType type = d.getType();
            if(type.getType().contains("MediaServer"))
                list.add(d);
        }

        return list;
    }

    public ArrayList<Device> getDMRList(){
        ArrayList<Device> list = new ArrayList<Device>();

        for(Device d : mDevicesList){
            DeviceType type = d.getType();
            if(type.getType().contains("MediaRenderer"))
                list.add(d);
        }

        return list;
    }

    @Override
    public void remoteDeviceDiscoveryStarted(Registry registry, RemoteDevice device) {
        deviceAdded(device);
    }

    @Override
    public void remoteDeviceDiscoveryFailed(Registry registry, final RemoteDevice device, final Exception ex) {
        /*runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(
                        BrowseActivity.this,
                        "Discovery failed of '" + device.getDisplayString() + "': " +
                                (ex != null ? ex.toString() : "Couldn't retrieve device/service descriptors"),
                        Toast.LENGTH_LONG
                ).show();
            }
        });*/
        deviceRemoved(device);
    }

    @Override
    public void remoteDeviceAdded(Registry registry, RemoteDevice device) {
        deviceAdded(device);
    }

    @Override
    public void remoteDeviceRemoved(Registry registry, RemoteDevice device) {
        deviceRemoved(device);
    }

    @Override
    public void localDeviceAdded(Registry registry, LocalDevice device) {
        deviceAdded(device);
    }

    @Override
    public void localDeviceRemoved(Registry registry, LocalDevice device) {
        deviceRemoved(device);
    }

    public void deviceAdded(final Device device) {
        Log.i(TAG, "deviceAdded: " + device.getDisplayString());

        int position = mDevicesList.indexOf(device);
        if(position >= 0){
            mDevicesList.remove(position);
            mDevicesList.add(position, device);
        }
        else{
            mDevicesList.add(device);
        }
    }

    public void deviceRemoved(final Device device) {
        Log.i(TAG, "deviceRemoved: " + device.getDisplayString());

        mDevicesList.remove(device);
    }
}
