package com.vullnetlimani.instacreative.Model;

public class Notification {

    public static final String USERID = "userid";
    public static final String TEXT = "text";
    public static final String POSTID = "postid";
    public static final String IS_POST = "is_post";

    private String userid;
    private String text;
    private String postid;
    private boolean is_post;

    public Notification() {
    }

    public Notification(String userid, String text, String postid, boolean is_post) {
        this.userid = userid;
        this.text = text;
        this.postid = postid;
        this.is_post = is_post;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public boolean isIs_post() {
        return is_post;
    }

    public void setIs_post(boolean is_post) {
        this.is_post = is_post;
    }
}
