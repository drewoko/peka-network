package big.peka.network.funstream.data.model;

import big.peka.network.funstream.data.api.model.response.Stream;
import big.peka.network.funstream.data.api.model.response.User;

import java.util.Date;
import java.util.Set;

public class UserActivityInfo {

    private Date activityTime;
    private User user;
    private Set<Stream> streams;

    public UserActivityInfo(Date activityTime, User user, Set<Stream> streams) {
        this.activityTime = activityTime;
        this.user = user;
        this.streams = streams;
    }

    public Date getActivityTime() {
        return activityTime;
    }

    public void setActivityTime(Date activityTime) {
        this.activityTime = activityTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Stream> getStreams() {
        return streams;
    }

    public void setStream(Set<Stream> stream) {
        this.streams = streams;
    }

    @Override
    public String toString() {
        return "UserActivityInfo{" +
                "activityTime=" + activityTime +
                ", user=" + user +
                ", streams=" + streams +
                '}';
    }
}
