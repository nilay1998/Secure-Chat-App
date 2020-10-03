package com.example.demochatapp.Retrofit;

public class Message {

    private String sender;
    private String receiver;
    private String message;
    private String created_at;

    public String getCreated_at() {
        return created_at;
    }

    public Message(){
    }
    public Message(String senderEmail, String message,String receiverEmail) {
        this.sender = senderEmail;
        this.message = message;
        this.receiver=receiverEmail;
    }

    public String getSenderEmail() {
        return sender;
    }

    public void setSenderEmail(String senderEmail) {
        this.sender = senderEmail;
    }

    public String getReceiverEmail() {
        return receiver;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiver = receiverEmail;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
