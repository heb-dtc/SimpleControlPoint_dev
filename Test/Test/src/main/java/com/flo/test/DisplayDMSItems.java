package com.flo.test;

import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.teleal.cling.support.model.item.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by florent.noel on 5/23/13.
 */
public class DisplayDMSItems extends ListFragment implements BrowseCallback{
    private static String TAG = BrowseDMSFragment.class.getName();

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
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
        super.onDestroy();
    }

    private void updateListViewContent(){
        ArrayList<UPnPContent> list = UPnPController.getInstance().getLastBrowseResult();
        mListAdapter.updateContentList(list);
    }

    @Override
    public void browseResultList(List<Item> list) {

    }
}
