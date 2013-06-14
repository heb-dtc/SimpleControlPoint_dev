package com.flo.test;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Flo on 5/28/13.
 */
public class ApplicationUtils {
    private static String TAG = ApplicationUtils.class.getName();

    public static enum Content_type {ITEM, CONTAINER, UNKNOWN};

    public static String secondsToTimeString(int sec){
        String format = String.format("%%0%dd", 2);

        String seconds = String.format(format, sec % 60);
        String minutes = String.format(format, (sec % 3600) / 60);
        String hours = String.format(format, sec / 3600);

        String time =  hours + ":" + minutes + ":" + seconds;

        return time;
    }

    public static Bitmap decodeSampledBitmapFromNetwork(String path, boolean compress, int width, int height) {
        Log.v(TAG, "decodeSampledBitmapFromNetwork");

        URL url = null;
        InputStream input = null;
        HttpURLConnection connection = null;

        try {
            url = new URL(path);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();

            input = connection.getInputStream();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();

        if(compress){
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(input, null, options);

            try {
                input.close();
                connection.disconnect();

                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();

                input = connection.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Calculate inSampleSize
            int sampleSize = calculateInSampleSize(options, width, height);
            options.inSampleSize = sampleSize;
            //Log.v(TAG, "Calculate inSampleSize: " + sampleSize);
        }

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        Bitmap img = BitmapFactory.decodeStream(input, null, options);

        try {
            input.close();
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return img;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float)height / (float)reqHeight);
            } else {
                inSampleSize = Math.round((float)width / (float)reqWidth);
            }
        }

        return inSampleSize;
    }
}
