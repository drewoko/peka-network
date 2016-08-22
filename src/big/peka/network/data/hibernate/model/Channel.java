package big.peka.network.data.hibernate.model;

import org.hibernate.annotations.Proxy;
import org.springframework.data.repository.cdi.Eager;

import javax.persistence.*;

@Entity
@Table(name = "Stream")
public class Channel extends PersistenceEntity {

    @Column(name = "slug")
    private String slug;

    @OneToOne
    @JoinColumn(name = "USER_ID")
    public User owner;

    @Column(name = "time")
    private long time;

    public String getSlug() {
        return slug;
    }

    public void setSlug(String name) {
        this.slug = name;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }


}
