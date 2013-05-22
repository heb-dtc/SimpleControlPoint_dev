package com.flo.test;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by florent.noel on 5/17/13.
 */
public class ControlDMRFragment extends ListFragment {

    private ArrayAdapter<DeviceDisplay> mListAdapter;

    public ControlDMRFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.page_control_dmr_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mListAdapter = new ArrayAdapter<DeviceDisplay>(getActivity(), android.R.layout.simple_list_item_1);
        setListAdapter(mListAdapter);

        String page = "Control DMR";
        getActivity().setTitle(page);

        updateListViewContent();
    }

    private void updateListViewContent(){
        ArrayList<DeviceDisplay> list = UPnPController.getInstance().getDmrList();

        mListAdapter.clear();
        for(DeviceDisplay d : list){
            mListAdapter.add(d);
        }
    }
}
