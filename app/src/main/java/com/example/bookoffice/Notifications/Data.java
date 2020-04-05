package com.example.bookoffice.Notifications;

public class Data {
    private  String body,sented,title,user;
    private int icon;

    public Data(){

    }


    public Data(String body, String sented, String title, String user, int icon) {
        this.body = body;
        this.sented = sented;
        this.title = title;
        this.user = user;
        this.icon = icon;
    }


    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSented() {
        return sented;
    }

    public void setSented(String sented) {
        this.sented = sented;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
