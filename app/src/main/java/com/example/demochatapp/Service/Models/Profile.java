package com.example.demochatapp.Service.Models;

public class Profile {
    private String status;
    private String message;
    private String name;
    private String email;
    private String phone;
    private String socketID;
    private String publickeyRSA;
    private String publickeyAES;
    private String lastSeen;

    public String getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(String lastSeen) {
        this.lastSeen = lastSeen;
    }

    public String getPublickeyRSA() {
        return publickeyRSA;
    }

    public void setPublickeyRSA(String publickeyRSA) {
        this.publickeyRSA = publickeyRSA;
    }

    public String getPublickeyAES() {
        return publickeyAES;
    }

    public void setPublickeyAES(String publickeyAES) {
        this.publickeyAES = publickeyAES;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSocketID() {
        return socketID;
    }

    public void setSocketID(String socketID) {
        this.socketID = socketID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}