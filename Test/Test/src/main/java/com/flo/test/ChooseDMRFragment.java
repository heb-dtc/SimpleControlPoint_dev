package com.flo.test;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

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

            //fill the views with the values
            //Bitmap img = new Bitmap();

            deviceNameView.setText(dd.toString());
            //deviceIcon.setImageBitmap(dd.getDevice().getIcons().);

            return view;
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
    }
}
