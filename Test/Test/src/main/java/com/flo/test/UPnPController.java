package com.flo.test;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import org.teleal.cling.android.AndroidUpnpService;
import org.teleal.cling.android.AndroidUpnpServiceImpl;
import org.teleal.cling.model.action.ActionInvocation;
import org.teleal.cling.model.message.UpnpResponse;
import org.teleal.cling.model.meta.Action;
import org.teleal.cling.model.meta.Device;
import org.teleal.cling.model.meta.Service;
import org.teleal.cling.model.types.UDAServiceId;
import org.teleal.cling.registry.RegistryListener;
import org.teleal.cling.support.avtransport.callback.*;
import org.teleal.cling.support.contentdirectory.callback.Browse;
import org.teleal.cling.support.model.BrowseFlag;
import org.teleal.cling.support.model.DIDLContent;
import org.teleal.cling.support.model.MediaInfo;
import org.teleal.cling.support.model.container.Container;
import org.teleal.cling.support.model.item.Item;
import org.teleal.cling.support.renderingcontrol.callback.GetMute;
import org.teleal.cling.support.renderingcontrol.callback.GetVolume;
import org.teleal.cling.support.renderingcontrol.callback.SetMute;
import org.teleal.cling.support.renderingcontrol.callback.SetVolume;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    private Device mCurrentDMS = null;
    private Device mCurrentDMR = null;

    private DIDLContent mLastBrowseResult;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.i(TAG, "service connected");
            mUPnPService = (AndroidUpnpService) iBinder;
            mIsCpStarted = true;

            for (Device device : mUPnPService.getRegistry().getDevices()) {
                mRegistryListener.deviceAdded(device);
            }

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

    /*
        DMS API calls
     */
    public void dms_browse(Device d, String id, BrowseFlag flag, final BrowseCallback cbInterface){
        if(mIsCpStarted){
            Log.i(TAG, "browse");

            mCurrentDMS = d;

            Service service = d.findService(new UDAServiceId("ContentDirectory"));

            mUPnPService.getControlPoint().execute(new Browse(service, id, flag) {
                @Override
                public void received(ActionInvocation actionInvocation, DIDLContent didlContent) {
                    Log.i(TAG, "browse, received");

                    updateLastBrowseResults(didlContent);
                    cbInterface.browseResultList(null);
                }

                @Override
                public void updateStatus(Status status) {
                    Log.i(TAG, "browse, update status");
                }

                @Override
                public void failure(ActionInvocation actionInvocation, UpnpResponse upnpResponse, String s) {
                    Log.i(TAG, "browse, failure");
                }
            });
        }
    }

    /*
        DMR API calls
     */

    public void dmr_play(Device d){
        if(mIsCpStarted){
            Log.i(TAG, "dmr_play");

            Service service = d.findService(new UDAServiceId("AVTransport"));

            mUPnPService.getControlPoint().execute(new Play(service) {
                @Override
                public void failure(ActionInvocation actionInvocation, UpnpResponse upnpResponse, String s) {

                }
            });
        }
    }

    public void dmr_stop(Device d){
        if(mIsCpStarted){
            Log.i(TAG, "dmr_stop");

            Service service = d.findService(new UDAServiceId("AVTransport"));

            mUPnPService.getControlPoint().execute(new Stop(service) {
                @Override
                public void failure(ActionInvocation actionInvocation, UpnpResponse upnpResponse, String s) {

                }
            });
        }
    }

    public void dmr_pause(Device d){
        if(mIsCpStarted){
            Log.i(TAG, "dmr_pause");

            Service service = d.findService(new UDAServiceId("AVTransport"));

            mUPnPService.getControlPoint().execute(new Pause(service) {
                @Override
                public void failure(ActionInvocation actionInvocation, UpnpResponse upnpResponse, String s) {

                }
            });
        }
    }

    public void dmr_getMediaInfo(Device d, final DMRCallbacks cbInterface){
        if(mIsCpStarted){
            Log.i(TAG, "dmr_getMediaInfo");

            Service service = d.findService(new UDAServiceId("AVTransport"));

            mUPnPService.getControlPoint().execute(new GetMediaInfo(service) {
                @Override
                public void received(ActionInvocation actionInvocation, MediaInfo mediaInfo) {
                    cbInterface.onReceiveMediaInfo(mediaInfo);
                }

                @Override
                public void failure(ActionInvocation actionInvocation, UpnpResponse upnpResponse, String s) {

                }
            });
        }
    }

    public void dmr_setURL(Device d, String URL){
        if(mIsCpStarted){
            Log.i(TAG, "dmr_setURL");

            Service service = d.findService(new UDAServiceId("AVTransport"));

            mUPnPService.getControlPoint().execute(new SetAVTransportURI(service, URL) {
                @Override
                public void failure(ActionInvocation actionInvocation, UpnpResponse upnpResponse, String s) {

                }
            });
        }
    }

    public void dmr_getVolume(Device d, final DMRCallbacks cbInterface){
        if(mIsCpStarted){
            Log.i(TAG, "dmr_getVolume");

            Service service = d.findService(new UDAServiceId("RenderingControl"));

            mUPnPService.getControlPoint().execute(new GetVolume(service) {
                @Override
                public void received(ActionInvocation actionInvocation, int i) {
                    cbInterface.onReceiveVolume(i);
                }

                @Override
                public void failure(ActionInvocation actionInvocation, UpnpResponse upnpResponse, String s) {

                }
            });
        }
    }

    public void dmr_setVolume(Device d, int volume){
        if(mIsCpStarted){
            Log.i(TAG, "dmr_setVolume");

            Service service = d.findService(new UDAServiceId("RenderingControl"));

            mUPnPService.getControlPoint().execute(new SetVolume(service, volume) {
                @Override
                public void failure(ActionInvocation actionInvocation, UpnpResponse upnpResponse, String s) {

                }
            });
        }
    }

    public void dmr_setMute(Device d, boolean muted){
        if(mIsCpStarted){
            Log.i(TAG, "dmr_setMute");

            Service service = d.findService(new UDAServiceId("RenderingControl"));

            mUPnPService.getControlPoint().execute(new SetMute(service, muted) {
                @Override
                public void failure(ActionInvocation actionInvocation, UpnpResponse upnpResponse, String s) {

                }
            });
        }
    }

    public void dmr_getMute(Device d, final DMRCallbacks cbInterface){
        if(mIsCpStarted){
            Log.i(TAG, "dmr_getMute");

            Service service = d.findService(new UDAServiceId("RenderingControl"));

            mUPnPService.getControlPoint().execute(new GetMute(service) {
                @Override
                public void received(ActionInvocation actionInvocation, boolean b) {
                    cbInterface.onReceiveMute(b);
                }

                @Override
                public void failure(ActionInvocation actionInvocation, UpnpResponse upnpResponse, String s) {

                }
            });
        }
    }

    private void updateLastBrowseResults(DIDLContent content){
        mLastBrowseResult = content;
    }

    public ArrayList<UPnPContent> getLastBrowseResult() {
        ArrayList<UPnPContent> result = new ArrayList<UPnPContent>();

        List<Container> containerList = mLastBrowseResult.getContainers();
        List<Item> itemList = mLastBrowseResult.getItems();

        if(containerList != null){
            for(Container c : containerList)
                result.add(new UPnPContent(c));
        }

        if(itemList != null){
            for(Item i : itemList)
                result.add(new UPnPContent(i));
        }

        return result;
    }


    public Device getCurrentDMR() {
        return mCurrentDMR;
    }

    public void setCurrentDMR(Device dmr) {
        this.mCurrentDMR = dmr;
    }

    public Device getCurrentDMS() {
        return mCurrentDMS;
    }

    public void setCurrentDMS(Device dms) {
        this.mCurrentDMS = dms;
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
