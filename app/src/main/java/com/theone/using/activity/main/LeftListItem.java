package com.theone.using.activity.main;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by liuyuan on 2016/7/16.
 */
public class LeftListItem {
    private int itemimgId;
    private String itemtx;

    public LeftListItem(int itemimgId, String itemtx) {
        this.itemimgId = itemimgId;
        this.itemtx = itemtx;
    }

    public LeftListItem() {
    }

    public int getItemimgId() {
        return itemimgId;
    }

    public void setItemimgId(int itemimgId) {
        this.itemimgId = itemimgId;
    }

    public String getItemtx() {
        return itemtx;
    }

    public void setItemtx(String itemtx) {
        this.itemtx = itemtx;
    }
}
