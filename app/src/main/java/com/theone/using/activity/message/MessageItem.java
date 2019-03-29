package com.theone.using.activity.message;

/**
 * Created by liuyuan on 2016/7/21.
 */
public class MessageItem {
    private String messageHead;
    private String messageContent;

    public MessageItem(String historyType, String historyContent){
        this.messageHead=historyType;
        this.messageContent=historyContent;
    }

    public String getHistoryType() {
        return messageHead;
    }

    public void setHistoryType(String historyType) {
        this.messageHead = historyType;
    }

    public String getHistoryContent() {
        return messageContent;
    }

    public void setHistoryContent(String historyContent) {
        this.messageContent = historyContent;
    }
}
