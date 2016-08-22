package big.peka.network.data.mongo.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
public class MessageDocument{

    private String messageText;

    private UserDocument sender;

    private StreamDocument stream;

    private Date time;

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public UserDocument getSender() {
        return sender;
    }

    public void setSender(UserDocument sender) {
        this.sender = sender;
    }

    public StreamDocument getStream() {
        return stream;
    }

    public void setStream(StreamDocument stream) {
        this.stream = stream;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
