package com.flo.test;

import java.io.File;

/**
 * Created by Flo on 5/28/13.
 */
public class LocalContent {
    private static String TAG = LocalContent.class.getName();

    //Content Properties
    private ApplicationUtils.Content_type mContentType;

    private File mFile = null;

    //Container and Item Properties
    private String mTitle = null;

    //Container only properties
    private String mPath = null;

    public LocalContent(){
        mContentType = ApplicationUtils.Content_type.UNKNOWN;
    }

    public LocalContent(File file){
        mFile = file;

        if(file.isDirectory()){
            mContentType = ApplicationUtils.Content_type.CONTAINER;
        }
        else{
            mContentType = ApplicationUtils.Content_type.ITEM;
        }

        mTitle = file.getName();
        mPath = file.getPath();
    }

    public boolean isContainer(){
        return (mContentType == ApplicationUtils.Content_type.CONTAINER);
    }

    public String getTitle(){
        return  mTitle;
    }

    public String getPath(){
        return  mPath;
    }

    public File getFile(){
        return  mFile;
    }
}
