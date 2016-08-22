package big.peka.network.funstream.data.model;

import big.peka.network.funstream.data.api.model.response.Stream;
import big.peka.network.funstream.data.api.model.response.User;

import java.util.Date;

public class MessageInfo {

    private Date time;
    private String text;
    private User sender;
    private Stream stream;

    public MessageInfo(Date time, String text, User sender, Stream stream) {
        this.time = time;
        this.text = text;
        this.sender = sender;
        this.stream = stream;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public Stream getStream() {
        return stream;
    }

    public void setStream(Stream stream) {
        this.stream = stream;
    }
}
