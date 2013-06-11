package com.flo.test;

/**
 * Created by Flo on 5/28/13.
 */
public class ApplicationUtils {

    public static enum Content_type {ITEM, CONTAINER, UNKNOWN};

    public static String secondsToTimeString(int sec){
        String format = String.format("%%0%dd", 2);

        String seconds = String.format(format, sec % 60);
        String minutes = String.format(format, (sec % 3600) / 60);
        String hours = String.format(format, sec / 3600);

        String time =  hours + ":" + minutes + ":" + seconds;

        return time;
    }
}
