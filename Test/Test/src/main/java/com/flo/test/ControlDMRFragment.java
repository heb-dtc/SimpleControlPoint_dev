package com.flo.test;

import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import org.teleal.cling.model.meta.Device;
import org.teleal.cling.support.model.MediaInfo;

import java.util.ArrayList;

/**
 * Created by florent.noel on 5/17/13.
 */
public class ControlDMRFragment extends Fragment implements DMRCallbacks{
    private static String TAG = ControlDMRFragment.class.getName();

    private Button mPlayButton;
    private Button mPauseButton;
    private Button mStopButton;
    private Button mGetVolumeButton;
    private Button mGetMediaInfoButton;

    private Device mDMR;

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

        mPlayButton = (Button)getActivity().findViewById(R.id.btn_play);
        mPauseButton = (Button)getActivity().findViewById(R.id.btn_pause);
        mStopButton = (Button)getActivity().findViewById(R.id.btn_stop);
        mGetVolumeButton = (Button)getActivity().findViewById(R.id.btn_get_volume);
        mGetMediaInfoButton = (Button)getActivity().findViewById(R.id.btn_get_media_info);

        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play();
            }
        });

        mPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pause();
            }
        });

        mStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stop();
            }
        });

        mGetMediaInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getVolume();
            }
        });

        mGetVolumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMediaInfo();
            }
        });

        mDMR = UPnPController.getInstance().getCurrentDMR();

        String page = "Control " + mDMR.getDetails().getFriendlyName();
        getActivity().setTitle(page);
    }

    private void play(){
        UPnPController.getInstance().dmr_play(mDMR);
    }

    private void pause(){
        UPnPController.getInstance().dmr_pause(mDMR);
    }

    private void stop(){
        UPnPController.getInstance().dmr_stop(mDMR);
    }

    private void getMediaInfo(){
        UPnPController.getInstance().dmr_getMediaInfo(mDMR, this);
    }

    private void getVolume(){
        UPnPController.getInstance().dmr_getVolume(mDMR, this);
    }

    @Override
    public int onReceiveVolume(int volume) {
        Log.e(TAG, "onReceiveVolume " + volume);
        return 0;
    }

    @Override
    public int onReceiveMute(boolean muted) {
        Log.e(TAG, "onReceiveMute " + muted);

        return 0;
    }

    @Override
    public int onReceiveMediaInfo(MediaInfo info) {
        Log.e(TAG, "onReceiveMediaInfo " + info.getCurrentURI());
        Log.e(TAG, "onReceiveMediaInfo " + info.getMediaDuration());

        return 0;
    }
}
