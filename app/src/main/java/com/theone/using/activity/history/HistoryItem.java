package com.theone.using.activity.history;

/**
 * Created by liuyuan on 2016/7/21.
 */
public class HistoryItem {
    private String historyType;
    private String historyContent;

    public HistoryItem( String historyType,String historyContent){
        this.historyType=historyType;
        this.historyContent=historyContent;
    }

    public String getHistoryType() {
        return historyType;
    }

    public void setHistoryType(String historyType) {
        this.historyType = historyType;
    }

    public String getHistoryContent() {
        return historyContent;
    }

    public void setHistoryContent(String historyContent) {
        this.historyContent = historyContent;
    }
}
