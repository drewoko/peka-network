package big.peka.network.data.mongo.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class StreamDocument extends DocumentEntity {

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StreamDocument that = (StreamDocument) o;

        if (owner.getName() != null ? !owner.getName().equals(that.owner.getName()) : that.owner.getName() != null) return false;
        return !(slug != null ? !slug.equals(that.slug) : that.slug != null);

    }

    @Override
    public int hashCode() {
        int result = owner.getName() != null ? owner.getName().hashCode() : 0;
        result = 31 * result + (slug != null ? slug.hashCode() : 0);
        return result;
    }
}
