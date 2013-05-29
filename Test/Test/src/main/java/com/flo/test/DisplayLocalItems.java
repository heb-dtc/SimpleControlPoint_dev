package com.flo.test;

import android.os.Bundle;
import android.os.Environment;
import android.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Flo on 5/28/13.
 */
public class DisplayLocalItems extends ListFragment {
    private static String TAG = DisplayLocalItems.class.getName();

    private LocalContentAdapter mListAdapter;

    public DisplayLocalItems() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.page_display_local_items_fragment, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mListAdapter = new LocalContentAdapter(getActivity());
        setListAdapter(mListAdapter);

        String page = "Local Items";
        getActivity().setTitle(page);

        File storage = Environment.getExternalStorageDirectory();
        getFiles(storage);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        LocalContent item = (LocalContent) mListAdapter.getItem(position);

        if(item.isContainer()){
            getFiles(item.getFile());
        }
    }

    public void getFiles(File f) {
        Log.i(TAG, "getFiles");

        ArrayList<LocalContent> fileList = new ArrayList<LocalContent>();

        File[] files = f.listFiles();
        if (files.length > 0){
            for (int i=0; i<files.length; i++)
                fileList.add(new LocalContent(files[i]));
        }

        updateListViewContent(fileList);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
        super.onDestroy();
    }

    private void updateListViewContent(ArrayList<LocalContent> fileList){
        mListAdapter.updateContentList(fileList);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mListAdapter.notifyDataSetChanged();
            }
        });
    }
}
