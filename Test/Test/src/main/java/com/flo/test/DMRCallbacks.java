package com.flo.test;

import org.teleal.cling.support.model.MediaInfo;

/**
 * Created by florent.noel on 5/23/13.
 */
public interface DMRCallbacks {
    public int onReceiveVolume(int volume);
    public int onReceiveMute(boolean muted);
    public int onReceiveMediaInfo(MediaInfo info);
}
