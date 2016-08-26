package big.peka.network.web.controllers.dto;

public class ChannelDescriptor {

    String channelSlug;

    public ChannelDescriptor(String channelSlug) {
        this.channelSlug = channelSlug;
    }

    public String getChannelSlug() {
        return channelSlug;
    }

    public void setChannelSlug(String channelSlug) {
        this.channelSlug = channelSlug;
    }
}
