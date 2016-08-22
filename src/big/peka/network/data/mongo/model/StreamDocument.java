package big.peka.network.data.mongo.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
public class StreamDocument {

    private UserDocument owner;

    private String slug;

    public UserDocument getOwner() {
        return owner;
    }

    public void setOwner(UserDocument owner) {
        this.owner = owner;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }
}
