package com.flo.test;

import android.util.Log;

import org.teleal.cling.support.model.Res;
import org.teleal.cling.support.model.container.Container;
import org.teleal.cling.support.model.item.Item;

/**
 * Created by florent.noel on 5/23/13.
 */
public class UPnPContent {
    private static String TAG = UPnPContent.class.getName();

    //Content Properties
    private ApplicationUtils.Content_type mContentType;

    //Container and Item Properties
    private String mId = null;
    private String mParentID = null;
    private String mTitle = null;
    private String mCreator = null;
    private String mURL = null;

    //Container only properties
    private int mChildCount = 0;

    //Item only properties


    public UPnPContent(){
        mContentType = ApplicationUtils.Content_type.UNKNOWN;
    }

    public UPnPContent(Container c){
        mContentType = ApplicationUtils.Content_type.CONTAINER;

        mId = c.getId();
        mParentID = c.getParentID();
        mTitle = c.getTitle();
        mChildCount = c.getChildCount();
        mCreator = c.getCreator();
    }

    public UPnPContent(Item i){
        mContentType = ApplicationUtils.Content_type.ITEM;

        mId = i.getId();
        mParentID = i.getParentID();
        mTitle = i.getTitle();
        mCreator = i.getCreator();
        Res resource = i.getFirstResource();
        mURL = resource.getValue();
    }

    public boolean isContainer(){
        return (mContentType == ApplicationUtils.Content_type.CONTAINER);
    }

    public String getobjectID(){
        return  mId;
    }

    public String getTitle(){
        return  mTitle;
    }

    public String getURL(){
        Log.e(TAG, "getURL: " + mURL);
        return  mURL;
    }

}
