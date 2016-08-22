package big.peka.network.funstream.data.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Streamer {

    private final long id;

    private final String name;

    public Streamer(@JsonProperty("id") long id, @JsonProperty("name") String name) {
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
        return "Streamer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }

        final Streamer streamer = (Streamer) o;

        if (id != streamer.id) { return false; }
        return !(name != null ? !name.equals(streamer.name) : streamer.name != null);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
