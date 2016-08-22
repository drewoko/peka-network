package big.peka.network.web.controllers.dto;

import big.peka.network.data.hibernate.model.User;
import big.peka.network.services.statistic.StatisticService.ChannelEntry;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Set;

public class StreamStatisticDto {

    Set<ChannelEntry<Long>> streamsWithSameViewers;
    List<User> viewers = Lists.newArrayList();

    public StreamStatisticDto(Set<ChannelEntry<Long>> streamsWithSameViewers, List<User> viewers) {
        this.streamsWithSameViewers = streamsWithSameViewers;
        this.viewers = viewers;
    }

    public Set<ChannelEntry<Long>> getStreamsWithSameViewers() {
        return streamsWithSameViewers;
    }

    public void setStreamsWithSameViewers(Set<ChannelEntry<Long>> streamsWithSameViewers) {
        this.streamsWithSameViewers = streamsWithSameViewers;
    }

    public List<User> getViewers() {
        return viewers;
    }

    public void setViewers(List<User> viewers) {
        this.viewers = viewers;
    }

}
