package com.flo.test;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.teleal.cling.model.meta.Icon;
import org.teleal.cling.model.meta.RemoteDeviceIdentity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by florent.noel on 5/29/13.
 */
public class ChooseDMRFragment extends ListFragment {
    private static String TAG = ChooseDMRFragment.class.getName();

    private DMRAdapter mListAdapter;
    private UPnPContent mItem = null;

    private class DMRAdapter extends BaseAdapter {

        private Activity mActivity;
        private ArrayList<DeviceDisplay> mData = new ArrayList<DeviceDisplay>();
        private HashMap<String, Bitmap> mDeviceIconsList = new  HashMap<String, Bitmap>();
        private LayoutInflater mInflater = null;

        public DMRAdapter(Activity a){
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
                view = mInflater.inflate(R.layout.dmr_item_list_row, null);

            final DeviceDisplay dd = mData.get(i);

            //find the views we need
            TextView deviceNameView = (TextView) view.findViewById(R.id.device_name);
            ImageView deviceIcon = (ImageView) view.findViewById(R.id.device_icon);
            Button pushBtn = (Button) view.findViewById(R.id.pushTo_btn);

            pushBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pushTo(dd);
                }
            });

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

            deviceNameView.setText(dd.toString());

            return view;
        }

        private class DownloadIDeviceIconTask extends AsyncTask<String, Void, Bitmap> {

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

    public ChooseDMRFragment(){

    }

    public ChooseDMRFragment(UPnPContent item){
        mItem = item;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.page_choose_dmr_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mListAdapter = new DMRAdapter(getActivity());
        setListAdapter(mListAdapter);

        String page = "Choose DMR to push to";
        getActivity().setTitle(page);

        updateListViewContent();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
        super.onDestroy();
    }

    private void updateListViewContent(){
        ArrayList<DeviceDisplay> list = UPnPController.getInstance().getDmrList();
        mListAdapter.updateDeviceList(list);
    }

    private void pushTo(DeviceDisplay dev){
        Log.i(TAG, "pushTo");

        if(mItem != null && dev != null){
            UPnPController.getInstance().dmr_setURL(dev.getDevice(), mItem.getURL());
            UPnPController.getInstance().dmr_play(dev.getDevice());
            UPnPController.getInstance().dmr_play(dev.getDevice());
            UPnPController.getInstance().dmr_play(dev.getDevice());
            UPnPController.getInstance().dmr_play(dev.getDevice());
        }

        loadControlDMRFragment(dev);
    }

    private void loadControlDMRFragment(DeviceDisplay dev){
        UPnPController.getInstance().setCurrentDMR(dev.getDevice());

        Fragment fragment = new ControlDMRFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.addToBackStack(null);
        transaction.replace(R.id.content_frame, fragment).commit();
    }
}
