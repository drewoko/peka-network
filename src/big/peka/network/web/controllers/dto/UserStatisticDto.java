package big.peka.network.web.controllers.dto;

import com.google.common.collect.Lists;

import java.util.List;

public class UserStatisticDto {

    private List<ViewerInfoIntervalDto> viewerInfoIntervalDtos;
    private List<ChannelDescriptor> usersFavouriteStreams = Lists.newArrayList();
    private List<ChannelDescriptor> streamsToRecomendate = Lists.newArrayList();
    private List<String> currentBrowsableStreams = Lists.newArrayList();

    public UserStatisticDto(List<ViewerInfoIntervalDto> viewerInfoIntervalDtos,
                            List<ChannelDescriptor> usersFavouriteStreams,
                            List<ChannelDescriptor> streamsToRecomendate,
                            List<String> currentBrowsableStreams)
    {
        this.viewerInfoIntervalDtos = viewerInfoIntervalDtos;
        this.usersFavouriteStreams = usersFavouriteStreams;
        this.streamsToRecomendate = streamsToRecomendate;
        this.currentBrowsableStreams = currentBrowsableStreams;
    }

    public List<ViewerInfoIntervalDto> getViewerInfoIntervalDtos() {
        return viewerInfoIntervalDtos;
    }

    public void setViewerInfoIntervalDtos(List<ViewerInfoIntervalDto> viewerInfoIntervalDtos) {
        this.viewerInfoIntervalDtos = viewerInfoIntervalDtos;
    }

    public List<ChannelDescriptor> getUsersFavouriteStreams() {
        return usersFavouriteStreams;
    }

    public void setUsersFavouriteStreams(List<ChannelDescriptor> usersFavouriteStreams) {
        this.usersFavouriteStreams = usersFavouriteStreams;
    }

    public List<ChannelDescriptor> getStreamsToRecomendate() {
        return streamsToRecomendate;
    }

    public void setStreamsToRecomendate(List<ChannelDescriptor> streamsToRecomendate) {
        this.streamsToRecomendate = streamsToRecomendate;
    }

    public List<String> getCurrentBrowsableStreams() {
        return currentBrowsableStreams;
    }

    public void setCurrentBrowsableStreams(List<String> currentBrowsableStreams) {
        this.currentBrowsableStreams = currentBrowsableStreams;
    }
}
