package com.flo.test;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import org.teleal.cling.android.AndroidUpnpService;
import org.teleal.cling.android.AndroidUpnpServiceImpl;
import org.teleal.cling.model.meta.Device;
import org.teleal.cling.registry.RegistryListener;

import java.util.ArrayList;

/**
 * Created by Flo on 5/21/13.
 */
public class UPnPController {
    private static String TAG = UPnPController.class.getName();

    private static UPnPController mInstance = null;
    private Context mContext = null;

    //UPNP
    private AndroidUpnpService mUPnPService;
    private BrowseRegistryListener mRegistryListener = new BrowseRegistryListener();

    private boolean mIsCpStarted = false;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.i(TAG, "service connected");
            mUPnPService = (AndroidUpnpService) iBinder;
            mIsCpStarted = true;

            /*for (Device device : mUPnPService.getRegistry().getDevices()) {
                //registryListener.
            }*/

            mUPnPService.getRegistry().addListener(mRegistryListener);
            mUPnPService.getControlPoint().search();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.i(TAG, "service disconnected");
            mUPnPService = null;
            mIsCpStarted = false;
        }
    };

    protected UPnPController(){
    }

    public synchronized  static UPnPController getInstance(){
        if(mInstance == null){
            mInstance = new UPnPController();
        }

        return  mInstance;
    }

    public void startCP(Context c){
        if(!mIsCpStarted){
            if(mContext == null){
                mContext = c;
            }

            mContext.bindService(
                    new Intent(mContext, AndroidUpnpServiceImpl.class),
                    serviceConnection,
                    Context.BIND_AUTO_CREATE
            );
        }
    }

    public void stopCP(){
        if(!mIsCpStarted){
            if (mUPnPService != null) {
                mUPnPService.getRegistry().removeListener(mRegistryListener);
            }
            mContext.unbindService(serviceConnection);
        }
    }

    public ArrayList<DeviceDisplay> getDmsList(){
        Log.i(TAG, "getDmsList");

        ArrayList<DeviceDisplay> dmsNames = new ArrayList<DeviceDisplay>();

        for(int i=0 ; i < mRegistryListener.getDMSList().size() ; i++){
            Log.i(TAG, "--> " + mRegistryListener.getDMSList().get(i).getDisplayString());
            dmsNames.add(new DeviceDisplay(mRegistryListener.getDMSList().get(i)));
        }

        return dmsNames;
    }

    public ArrayList<DeviceDisplay> getDmrList(){
        Log.i(TAG, "getDmrList");

        ArrayList<DeviceDisplay> dmrNames = new ArrayList<DeviceDisplay>();

        for(int i=0 ; i < mRegistryListener.getDMRList().size() ; i++){
            Log.i(TAG, "--> " + mRegistryListener.getDMRList().get(i).getDisplayString());
            dmrNames.add(new DeviceDisplay(mRegistryListener.getDMRList().get(i)));
        }

        return dmrNames;
    }
}
