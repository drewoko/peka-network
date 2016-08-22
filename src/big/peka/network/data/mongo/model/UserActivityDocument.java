package big.peka.network.data.mongo.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
public class UserActivityDocument extends DocumentEntity {

    private UserDocument user;

    private Date time;

    private StreamDocument stream;

    public UserDocument getUser() {
        return user;
    }

    public void setUser(UserDocument user) {
        this.user = user;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public StreamDocument getStream() {
        return stream;
    }

    public void setStream(StreamDocument stream) {
        this.stream = stream;
    }
}
