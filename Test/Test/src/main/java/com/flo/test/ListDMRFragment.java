package com.flo.test;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by florent.noel on 5/23/13.
 */
public class ListDMRFragment extends ListFragment {

    private UPnPDeviceAdapter mListAdapter;

    public ListDMRFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.page_list_dmr_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mListAdapter = new UPnPDeviceAdapter(getActivity());
        setListAdapter(mListAdapter);

        String page = "DMR List";
        getActivity().setTitle(page);

        updateListViewContent();
    }

    private void updateListViewContent(){
        ArrayList<DeviceDisplay> list = UPnPController.getInstance().getDmrList();
        mListAdapter.updateDeviceList(list);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        DeviceDisplay dd = (DeviceDisplay) mListAdapter.getItem(position);
        UPnPController.getInstance().setCurrentDMR(dd.getDevice());

        Fragment fragment = new ControlDMRFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.addToBackStack(null);
        transaction.replace(R.id.content_frame, fragment).commit();
    }
}
