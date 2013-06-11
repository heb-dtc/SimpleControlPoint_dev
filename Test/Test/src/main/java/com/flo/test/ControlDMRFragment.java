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
import android.widget.SeekBar;
import android.widget.TextView;

import org.teleal.cling.model.meta.Device;
import org.teleal.cling.support.avtransport.lastchange.AVTransportVariable;
import org.teleal.cling.support.model.MediaInfo;
import org.teleal.cling.support.model.PositionInfo;
import org.teleal.cling.support.model.TransportInfo;
import org.teleal.cling.support.model.TransportState;

import java.text.SimpleDateFormat;
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
    private Button mMuteButton;
    private Button mGetMediaInfoButton;

    private SeekBar mVolumeBar;
    private SeekBar mPositionBar;

    private TextView mDurationView;
    private TextView mURIView;

    private Device mDMR;
    private MediaInfo mCurrentMediaInfo;
    private TransportState mCurrentState;
    private boolean mIsMuted;
    private boolean mIsPositionBarInitialized;

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
        mMuteButton = (Button)getActivity().findViewById(R.id.btn_mute);
        mGetMediaInfoButton = (Button)getActivity().findViewById(R.id.btn_get_media_info);
        mVolumeBar = (SeekBar)getActivity().findViewById(R.id.seek_bar_volume);
        mPositionBar = (SeekBar)getActivity().findViewById(R.id.seek_bar_position);
        mDurationView = (TextView)getActivity().findViewById(R.id.tv_duration);
        mURIView = (TextView)getActivity().findViewById(R.id.tv_uri);

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

        mMuteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setMute(!mIsMuted);
                mIsMuted = !mIsMuted;
            }
        });

        mDMR = UPnPController.getInstance().getCurrentDMR();

        mVolumeBar.setOnSeekBarChangeListener(mVolumeBarListener);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mVolumeBar.setMax(100);
            }
        });
        mPositionBar.setOnSeekBarChangeListener(mPositionBarListener);

        String page = "Control " + mDMR.getDetails().getFriendlyName();
        getActivity().setTitle(page);

        getMute();
        getVolume();
        getMediaInfo();
        getTransportInfo();
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

    private void getTransportInfo(){
        UPnPController.getInstance().dmr_getTransportInfo(mDMR, this);
    }

    private void getPositionInfo(){
        UPnPController.getInstance().dmr_getPositionInfo(mDMR, this);
    }

    private void getVolume(){
        UPnPController.getInstance().dmr_getVolume(mDMR, this);
    }

    private void getMute(){
        UPnPController.getInstance().dmr_getMute(mDMR, this);
    }

    private void setMute(boolean mute){
        UPnPController.getInstance().dmr_setMute(mDMR, mute);
    }

    private void setVolume(int volume){
        UPnPController.getInstance().dmr_setVolume(mDMR, volume);
    }

    private void startPositionListenerThread(){
        getPositionInfo(); //for now....
    }

    private SeekBar.OnSeekBarChangeListener mPositionBarListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            String seekToString = ApplicationUtils.secondsToTimeString(i);
            Log.e(TAG, "onProgressChanged --> " + seekToString);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    private SeekBar.OnSeekBarChangeListener mVolumeBarListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                setVolume(i);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    };

    /*
        DMR CALLBACK Interface
     */

    @Override
    public int onReceiveVolume(final int volume) {
        Log.e(TAG, "onReceiveVolume " + volume);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mVolumeBar.setProgress(volume);
            }
        });

        return 0;
    }

    @Override
    public int onReceiveMute(boolean muted) {
        Log.e(TAG, "onReceiveMute " + muted);

        mIsMuted = muted;

        return 0;
    }

    @Override
    public int onReceiveMediaInfo(final MediaInfo info) {
        Log.e(TAG, "onReceiveMediaInfo " + info.getCurrentURI());
        Log.e(TAG, "onReceiveMediaInfo " + info.getMediaDuration());

        mCurrentMediaInfo = info;

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mURIView.setText(info.getCurrentURI());
                mDurationView.setText(info.getMediaDuration());
            }
        });

        return 0;
    }

    @Override
    public int onReceivePositionInfo(final PositionInfo info) {
        Log.e(TAG, "onReceivePositionInfo, duration is: " + info.getTrackDurationSeconds());
        Log.e(TAG, "onReceivePositionInfo, position is: " + info.getTrackElapsedSeconds());

        if(!mIsPositionBarInitialized){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int durInSec = (int) info.getTrackDurationSeconds();
                    mPositionBar.setMax(durInSec);
                    mIsPositionBarInitialized = true;

                    int posInSec = (int) info.getTrackElapsedSeconds();
                    mPositionBar.setProgress(posInSec);
                }
            });
        }

        return 0;
    }

    @Override
    public int onReceiveTransportInfo(TransportInfo info) {
        Log.e(TAG, "onReceiveTransportInfo " + info.getCurrentTransportState().getValue());

        mCurrentState = info.getCurrentTransportState();

        //call getPositionInfo once
        getPositionInfo();

        if(mCurrentState == TransportState.PLAYING){
            startPositionListenerThread();
        }

        return 0;
    }
}
