package big.peka.network.funstream.data.api.model.request;

public class StreamRequest {
    private final Long id;
    private final String owner;
    private final Object options = null;


    public StreamRequest(final Long id, final String owner) {
        this.id = id;
        this.owner = owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StreamRequest that = (StreamRequest) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        return !(owner != null ? !owner.equals(that.owner) : that.owner != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (owner != null ? owner.hashCode() : 0);
        return result;
    }

    public Long getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public Object getOptions() {
        return options;
    }
}
