package big.peka.network.data.mongo.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
public class StreamActivityDocument extends DocumentEntity {

    private StreamDocument stream;
    private Date activityTime;

    public StreamDocument getStream() {
        return stream;
    }

    public void setStream(StreamDocument stream) {
        this.stream = stream;
    }

    public Date getActivityTime() {
        return activityTime;
    }

    public void setActivityTime(Date activityTime) {
        this.activityTime = activityTime;
    }
}
