package big.peka.network.web.controllers.dto;

import com.google.common.collect.Lists;

import java.util.List;

public class StreamStatisticDto {

    List<ChannelEstimateDto> anotherStreamsViewersCounts = Lists.newArrayList();
    List<UserDescriptor> viewers = Lists.newArrayList();

    public StreamStatisticDto(List<ChannelEstimateDto> anotherStreamsViewersCounts, List<UserDescriptor> viewers) {
        this.anotherStreamsViewersCounts = anotherStreamsViewersCounts;
        this.viewers = viewers;
    }

    public List<ChannelEstimateDto> getAnotherStreamsViewersCounts() {
        return anotherStreamsViewersCounts;
    }

    public void setAnotherStreamsViewersCounts(List<ChannelEstimateDto> anotherStreamsViewersCounts) {
        this.anotherStreamsViewersCounts = anotherStreamsViewersCounts;
    }

    public List<UserDescriptor> getViewers() {
        return viewers;
    }

    public void setViewers(List<UserDescriptor> viewers) {
        this.viewers = viewers;
    }

}
