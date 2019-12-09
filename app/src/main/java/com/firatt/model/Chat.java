package com.firatt.model;

import java.util.Date;

public class Chat {
    public String messageText;
    public long messageTime;
    public String rec;
    public String send;
    public int type;

    public int getType() {
        return type;
    }

    public long getMessageTime() {
        return messageTime;
    }


    public String getMessageText() {
        return messageText;
    }


    public Chat(){}

    public Chat(String messageText, String send,int type) {
        this.messageText = messageText;
        this.send=send;
        this.type=type;
        messageTime=new Date().getTime();
    }

}
