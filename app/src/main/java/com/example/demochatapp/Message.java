package com.example.demochatapp;

public class Message {

    private String senderEmail;
    private String receiverEmail;
    private String message ;

    public Message(){
    }
    public Message(String senderEmail, String message,String receiverEmail) {
        this.senderEmail = senderEmail;
        this.message = message;
        this.receiverEmail=receiverEmail;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
