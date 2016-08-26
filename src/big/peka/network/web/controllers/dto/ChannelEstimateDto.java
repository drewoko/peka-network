package big.peka.network.web.controllers.dto;

public class ChannelEstimateDto<T> {

    private ChannelDescriptor channelDescriptor;
    private T estimate;

    public ChannelEstimateDto(ChannelDescriptor channelDescriptor, T estimate) {
        this.channelDescriptor = channelDescriptor;
        this.estimate = estimate;
    }

    public ChannelDescriptor getChannelDescriptor() {
        return channelDescriptor;
    }

    public void setChannelDescriptor(ChannelDescriptor channelDescriptor) {
        this.channelDescriptor = channelDescriptor;
    }

    public T getEstimate() {
        return estimate;
    }

    public void setEstimate(T estimate) {
        this.estimate = estimate;
    }
}
