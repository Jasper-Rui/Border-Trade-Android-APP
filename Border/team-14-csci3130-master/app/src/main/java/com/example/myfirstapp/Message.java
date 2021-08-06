package com.example.myfirstapp;

public class Message {

    //variables
    private String msg;
    private String dateSent;
    private boolean read_unread;
    private String to;
    private String from;

    public Message(){}

    //    USE SETTER METHODS TO CONSTRUCT


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDateSent() {
        return dateSent;
    }

    public void setDateSent(String dateSent) {
        this.dateSent = dateSent;
    }

    public boolean getRead_unread() {
        return read_unread;
    }

    public void setRead_unread(boolean read_unread) {
        this.read_unread = read_unread;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String toString(){
        return "Message sent: '"+ msg +"\'\n" +
                "Date sent: "+ dateSent + "\n" +
                "Read/Unread: "+ read_unread + "\n" +
                "From: "+ from + "\n" +
                "To: "+ to;
    }
}
