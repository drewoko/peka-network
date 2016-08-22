package big.peka.network.data.hibernate.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Proxy;
import org.springframework.data.repository.cdi.Eager;

import javax.persistence.*;

@Entity
@Table(name = "USER_ACTIVITY")
@JsonIdentityInfo(property = "id", generator = ObjectIdGenerators.PropertyGenerator.class)
public class UserActivity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    User user;

    @OneToOne
    @JoinColumn(name = "CHANNEL_ID")
    Channel channel;

    @Column(name = "TIME")
    long time;

    @Column(name = "MESSAGE_COUNT")
    long messageCount;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(long messageCount) {
        this.messageCount = messageCount;
    }

    public void addTime(long time){
        setTime(this.time + time);
    }

    public void addMessageCount(long messageCount){
        setMessageCount(this.messageCount+messageCount);
    }
}
