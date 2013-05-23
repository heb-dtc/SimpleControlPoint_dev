package com.flo.test;

import org.teleal.cling.support.model.container.Container;
import org.teleal.cling.support.model.item.Item;

/**
 * Created by florent.noel on 5/23/13.
 */
public class UPnPContent {
    private static String TAG = UPnPContent.class.getName();

    public enum Content_type {ITEM, CONTAINER, UNKNOWN};

    //Content Properties
    private Content_type mContentType;

    //Container and Item Properties
    private String mId = null;
    private String mParentID = null;
    private String mTitle = null;
    private String mCreator = null;

    //Container only properties
    private int mChildCount = 0;

    //Item only properties


    public UPnPContent(){
        mContentType = Content_type.UNKNOWN;
    }

    public UPnPContent(Container c){
        mContentType = Content_type.CONTAINER;

        mId = c.getId();
        mParentID = c.getParentID();
        mTitle = c.getTitle();
        mChildCount = c.getChildCount();
        mCreator = c.getCreator();
    }

    public UPnPContent(Item i){
        mContentType = Content_type.ITEM;

        mId = i.getId();
        mParentID = i.getParentID();
        mTitle = i.getTitle();
        mCreator = i.getCreator();
    }

    public String getTitle(){
        return  mTitle;
    }

}
