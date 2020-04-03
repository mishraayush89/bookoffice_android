package com.example.bookoffice.Model;

public class Chat {
    private String message,sender,reciever;

    public Chat(){

    }

    public Chat(String messae, String sender, String reciever) {
        this.message = messae;
        this.sender = sender;
        this.reciever = reciever;
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
}
