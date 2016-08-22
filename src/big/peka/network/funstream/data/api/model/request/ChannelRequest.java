package big.peka.network.funstream.data.api.model.request;

import java.util.List;

public class ChannelRequest {

    private final List<String> channels;

    public ChannelRequest(final List<String> channels) {
        this.channels = channels;
    }

    public List<String> getChannels() {
        return channels;
    }

    public List<Object> getOptions(){
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChannelRequest that = (ChannelRequest) o;

        return !(channels != null ? !channels.equals(that.channels) : that.channels != null);

    }

    @Override
    public int hashCode() {
        return channels != null ? channels.hashCode() : 0;
    }
}
