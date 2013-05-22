package com.flo.test;

import android.app.Fragment;
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
public class BrowseDMSFragment extends ListFragment {

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

        ArrayList<String> mDMSList = UPnPController.getInstance().getDmsNamesList();

        setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mDMSList));

        String page = "Browse DMS";
        getActivity().setTitle(page);
    }
}
