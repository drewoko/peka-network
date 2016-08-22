package big.peka.network.funstream.data.model;

import big.peka.network.funstream.data.api.model.response.Stream;

import java.util.Date;

public class StreamActivityInfo {

    private Stream stream;
    private Date activityTime;

    public StreamActivityInfo(Stream stream, Date activityTime) {
        this.stream = stream;
        this.activityTime = activityTime;
    }

    public Stream getStream() {
        return stream;
    }

    public void setStream(Stream stream) {
        this.stream = stream;
    }

    public Date getActivityTime() {
        return activityTime;
    }

    public void setActivityTime(Date activityTime) {
        this.activityTime = activityTime;
    }
}
