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

    private ArrayList<Device> mDMSList = new ArrayList<Device>();
    private ArrayList<Device> mDMRList = new ArrayList<Device>();

    public ArrayList<Device> getDevicesList(){
        return mDevicesList;
    }

    public ArrayList<Device> getDMSList(){
        return mDevicesList;
    }

    public ArrayList<Device> getDMRList(){
        return mDMRList;
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

        mDevicesList.add(device);
        /*runOnUiThread(new Runnable() {
            public void run() {
                DeviceDisplay d = new DeviceDisplay(device);
                int position = listAdapter.getPosition(d);
                if (position >= 0) {
                    // Device already in the list, re-set new value at same position
                    listAdapter.remove(d);
                    listAdapter.insert(d, position);
                } else {
                    listAdapter.add(d);
                }
            }
        });*/
    }

    public void deviceRemoved(final Device device) {
        Log.i(TAG, "deviceRemoved: " + device.getDisplayString());

        mDevicesList.remove(device);
        /*runOnUiThread(new Runnable() {
            public void run() {
                listAdapter.remove(new DeviceDisplay(device));
            }
        });*/
    }
}
