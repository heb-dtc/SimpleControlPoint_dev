package com.flo.test;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by florent.noel on 5/17/13.
 */
public class ControlDMRFragment extends Fragment {

    public ControlDMRFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String page = "Control DMR";
        getActivity().setTitle(page);

        return inflater.inflate(R.layout.page_control_dmr_fragment, container, false);
    }
}
