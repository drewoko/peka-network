package big.peka.network.data.mongo.model;

import org.assertj.core.util.Sets;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Document
public class UserDocument {

    private Set userActivities = Sets.newHashSet();

    private String name;

    public Set<UserActivityDocument> getUserActivities() {
        return userActivities;
    }

    public void setUserActivities(Set<UserActivityDocument> userActivities) {
        this.userActivities = userActivities;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
