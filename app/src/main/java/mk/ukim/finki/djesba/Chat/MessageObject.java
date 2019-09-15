package mk.ukim.finki.djesba.Chat;

import java.util.ArrayList;

public class MessageObject {
    private String messageId;
    private String senderId;
    private String message;

    ArrayList<String> mediaUrlList;

    public MessageObject(String messageId, String senderId, String message, ArrayList<String> mediaUrlList) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.message = message;
        this.mediaUrlList = mediaUrlList;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<String> getMediaUrlList() { return mediaUrlList; }
}
