package com.theone.using.activity.main;

/**
 * Created by liuyuan on 2016/7/23.
 */
public class SearchItem {
    private int searchItemImgId;
    private String searchPlaceName;
    private String searchAddress;

    public SearchItem(int searchItemImgId, String searchAddress, String searchPlaceName) {
        this.searchItemImgId = searchItemImgId;
        this.searchAddress = searchAddress;
        this.searchPlaceName = searchPlaceName;
    }

    public int getSearchItemImgId() {
        return searchItemImgId;
    }

    public void setSearchItemImgId(int searchItemImgId) {
        this.searchItemImgId = searchItemImgId;
    }

    public String getSearchPlaceName() {
        return searchPlaceName;
    }

    public void setSearchPlaceName(String searchPlaceName) {
        this.searchPlaceName = searchPlaceName;
    }

    public String getSearchAddress() {
        return searchAddress;
    }

    public void setSearchAddress(String searchAddress) {
        this.searchAddress = searchAddress;
    }
}
