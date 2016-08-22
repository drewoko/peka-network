package big.peka.network.data.hibernate.model;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Proxy;
import org.springframework.data.repository.cdi.Eager;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "PERSON")
@NamedEntityGraph(name = "UserActivitiesAndChannels",
        attributeNodes = {
                @NamedAttributeNode(value = "activities", subgraph = "channelSubgraph")
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "channelSubgraph",
                        attributeNodes = {
                                @NamedAttributeNode("channel")
                        }
                )
        }
)
public class User extends PersistenceEntity {

    @OneToMany(mappedBy = "user")
    private Set<UserActivity> activities;

    @Column(name = "name", nullable = false)
    private String name;

    public Set<UserActivity> getActivities() {
        return activities;
    }

    public void setActivities(Set<UserActivity> activities) {
        this.activities = activities;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
