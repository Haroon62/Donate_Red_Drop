package com.example.donatereddropp.Models;

import java.io.Serializable;

public class ChatModel implements Serializable {
    String messagecontent;
    String senderids;
    long messagetime;

    String receiverID;

    String messageId;
    String image;
    String purlofprofilepic;

    public ChatModel() {
    }


    public ChatModel(String messagecontent, String senderids, long messagetime, String receiverID, String messageId, String purlofprofilepic,String image) {
        this.messagecontent = messagecontent;
        this.senderids = senderids;
        this.messagetime = messagetime;
        this.receiverID = receiverID;
        this.messageId = messageId;
        this.purlofprofilepic = purlofprofilepic;
        this.image=image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMessagecontent() {
        return messagecontent;
    }

    public void setMessagecontent(String messagecontent) {
        this.messagecontent = messagecontent;
    }

    public String getSenderids() {
        return senderids;
    }

    public void setSenderids(String senderids) {
        this.senderids = senderids;
    }

    public long getMessagetime() {
        return messagetime;
    }

    public void setMessagetime(long messagetime) {
        this.messagetime = messagetime;
    }

    public String getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(String receiverID) {
        this.receiverID = receiverID;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getPurlofprofilepic() {
        return purlofprofilepic;
    }

    public void setPurlofprofilepic(String purlofprofilepic) {
        this.purlofprofilepic = purlofprofilepic;
    }

    @Override
    public String toString() {
        return "ChatRoomModel{" +
                "messagecontent='" + messagecontent + '\'' +
                ", senderids='" + senderids + '\'' +
                ", messagetime=" + messagetime +
                ", receiverID='" + receiverID + '\'' +
                ", messageId='" + messageId + '\'' +
                ", purlofprofilepic='" + purlofprofilepic + '\'' +
                '}';
    }
}
