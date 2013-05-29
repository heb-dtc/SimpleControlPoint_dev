package com.flo.test;

import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import org.teleal.cling.model.meta.Device;
import org.teleal.cling.support.model.BrowseFlag;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by florent.noel on 5/17/13.
 */
public class BrowseLocalFragment extends ListFragment {
    private static  String TAG = BrowseLocalFragment.class.getName();
    private ArrayList<String> mSdCardFiles = null;

    public BrowseLocalFragment() {
        Log.i(TAG, "create BrowseLocalFragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.page_browse_local_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mSdCardFiles = getFiles();

        setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mSdCardFiles));

        String page = "Browse Local Files";
        getActivity().setTitle(page);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    }

    public ArrayList<String> getFiles() {
        Log.i(TAG, "getFiles from ext. storage");

        File storage = Environment.getExternalStorageDirectory();
        ArrayList<String> fileList = new ArrayList<String>();

        File[] files = storage.listFiles();
        if (files.length == 0)
            return null;
        else {
            for (int i=0; i<files.length; i++)
                fileList.add(files[i].getName());
        }

        return fileList;
    }
}
