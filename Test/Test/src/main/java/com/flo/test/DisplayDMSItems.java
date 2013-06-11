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
 * Created by florent.noel on 5/23/13.
 */
public class DisplayDMSItems extends ListFragment implements BrowseCallback{
    private static String TAG = DisplayDMSItems.class.getName();

    private UPnPContentAdapter mListAdapter;

    public DisplayDMSItems() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.page_display_dms_items_fragment, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mListAdapter = new UPnPContentAdapter(getActivity());
        setListAdapter(mListAdapter);

        String page = "DMS Items";
        getActivity().setTitle(page);

        updateListViewContent();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        UPnPContent item = (UPnPContent) mListAdapter.getItem(position);

        if(item.isContainer()){
            browseDown(item);
        }
        else{
            MainActivity a = (MainActivity)getActivity();
            a.loadChooseDMRFragment(item);
        }
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
        super.onDestroy();
    }

    private void browseMeta(UPnPContent item){
        Log.i(TAG, "browseMeta");

        Device dms = UPnPController.getInstance().getCurrentDMS();

        if(item != null && dms != null){
            UPnPController.getInstance().dms_browse(dms, item.getobjectID(), BrowseFlag.METADATA, this);
        }
    }

    private void browseDown(UPnPContent item){
        Log.i(TAG, "browseDown");

        Device dms = UPnPController.getInstance().getCurrentDMS();

        if(item != null && dms != null){
            UPnPController.getInstance().dms_browse(dms, item.getobjectID(), BrowseFlag.DIRECT_CHILDREN, this);
        }
    }

    private void updateListViewContent(){
        ArrayList<UPnPContent> list = UPnPController.getInstance().getLastBrowseResult();
        mListAdapter.updateContentList(list);
    }

    @Override
    public void browseResultList(List<Item> list) {
        Log.e(TAG, "browseResultList received");

        updateListViewContent();

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mListAdapter.notifyDataSetChanged();
            }
        });
    }
}
