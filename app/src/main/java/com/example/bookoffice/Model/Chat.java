package com.example.bookoffice.Model;

public class Chat {
    private String message,sender,reciever;
    private boolean  isseen;

    public Chat(){

    }

    public Chat(String messae, String sender, String reciever,boolean isseen) {
        this.message = messae;
        this.sender = sender;
        this.reciever = reciever;
        this.isseen=isseen;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReciever() {
        return reciever;
    }

    public void setReciever(String reciever) {
        this.reciever = reciever;
    }

    public boolean isIsseen() {
        return isseen;
    }

    public void setIsseen(boolean isseen) {
        this.isseen = isseen;
    }
}
