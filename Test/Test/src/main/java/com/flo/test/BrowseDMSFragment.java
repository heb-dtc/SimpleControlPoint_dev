package com.flo.test;

import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import org.teleal.cling.model.meta.Device;
import org.teleal.cling.support.model.BrowseFlag;
import org.teleal.cling.support.model.item.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by florent.noel on 5/17/13.
 */
public class BrowseDMSFragment extends ListFragment implements BrowseCallback{
    private static String TAG = BrowseDMSFragment.class.getName();

    private UPnPDeviceAdapter mListAdapter;
    //old one --> private ArrayAdapter<DeviceDisplay> mListAdapter;

    public BrowseDMSFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.page_browse_dms_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mListAdapter = new UPnPDeviceAdapter(getActivity());
        //mListAdapter = new ArrayAdapter<DeviceDisplay>(getActivity(), android.R.layout.simple_list_item_1);
        setListAdapter(mListAdapter);

        String page = "Browse DMS";
        getActivity().setTitle(page);

        updateListViewContent();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }

    private void updateListViewContent(){
        ArrayList<DeviceDisplay> list = UPnPController.getInstance().getDmsList();
        mListAdapter.updateDeviceList(list);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        DeviceDisplay dd = (DeviceDisplay) getListView().getItemAtPosition(position);
        Device d = dd.getDevice();

        Log.i(TAG, "onListItemClick --> " + dd.toString());

        UPnPController.getInstance().browse(d, "0", BrowseFlag.DIRECT_CHILDREN, this);
    }

    @Override
    public void browseResultList(List<Item> list){
        Log.i(TAG, "browse results received");

        MainActivity a = (MainActivity)getActivity();
        a.loadDisplayDMSItems();
    }
}
