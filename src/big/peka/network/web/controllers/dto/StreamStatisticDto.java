package big.peka.network.web.controllers.dto;

import com.google.common.collect.Lists;

import java.util.List;

public class StreamStatisticDto {

    boolean online;

    List<ChannelEstimateDto> viewersFavouriteStreams = Lists.newArrayList();
    List<UserDescriptor> viewers = Lists.newArrayList();
    List<StreamInfoIntervalDto> infoIntervals = Lists.newArrayList();

    public StreamStatisticDto(List<ChannelEstimateDto> viewersFavouriteStreams, List<UserDescriptor> viewers) {
        this.viewersFavouriteStreams = viewersFavouriteStreams;
        this.viewers = viewers;
    }

    public List<ChannelEstimateDto> getViewersFavouriteStreams() {
        return viewersFavouriteStreams;
    }

    public void setViewersFavouriteStreams(List<ChannelEstimateDto> viewersFavouriteStreams) {
        this.viewersFavouriteStreams = viewersFavouriteStreams;
    }

    public List<UserDescriptor> getViewers() {
        return viewers;
    }

    public void setViewers(List<UserDescriptor> viewers) {
        this.viewers = viewers;
    }

    public List<StreamInfoIntervalDto> getInfoIntervals() {
        return infoIntervals;
    }

    public void setInfoIntervals(List<StreamInfoIntervalDto> infoIntervals) {
        this.infoIntervals = infoIntervals;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }
}
