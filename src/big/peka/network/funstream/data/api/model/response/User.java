package big.peka.network.funstream.data.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
    private final long id;

    private final String name;

    public User(@JsonProperty("id") long id, @JsonProperty("name") String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }

        final User user = (User) o;

        if (id != user.id) { return false; }
        return !(name != null ? !name.equals(user.name) : user.name != null);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
