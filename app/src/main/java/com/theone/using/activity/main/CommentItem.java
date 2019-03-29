package com.theone.using.activity.main;

/**
 * Created by liuyuan on 2016/7/29.
 */
public class CommentItem {

    private String username;
    private String commentContent;

    public CommentItem(String username, String commentContent){
        this.username = username;
        this.commentContent = commentContent;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

}
