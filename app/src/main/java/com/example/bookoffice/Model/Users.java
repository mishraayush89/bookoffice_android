package com.example.bookoffice.Model;

public class Users {
    public String email,imageurl,name,uid,status;

    public Users()
    {

    }

    public Users(String email, String imageurl, String name, String uid,String status) {
        this.email = email;
        this.imageurl = imageurl;
        this.name = name;
        this.uid = uid;
        this.status=status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
