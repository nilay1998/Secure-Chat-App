package com.example.demochatapp.Service.Models;

public class Message {

    private String id;
    private String sender;
    private String receiver;
    private String message;
    private String created_at;
    private String isRead;

    public String getCreated_at() {
        return created_at;
    }

    public Message(String senderEmail, String message,String receiverEmail,String isRead) {
        this.sender = senderEmail;
        this.message = message;
        this.receiver=receiverEmail;
        this.isRead=isRead;
    }
    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    public String getSenderEmail() {
        return sender;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
