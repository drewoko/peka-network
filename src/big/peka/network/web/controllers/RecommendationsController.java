package big.peka.network.web.controllers;

import big.peka.network.data.hibernate.model.Channel;
import big.peka.network.services.statistic.CurrentActivitiesService;
import big.peka.network.services.statistic.StatisticService;
import big.peka.network.services.statistic.StatisticService.StreamStatistic;
import big.peka.network.services.statistic.StatisticService.ChannelEntry;
import big.peka.network.web.controllers.dto.*;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RecommendationsController {

    @Autowired
    StatisticService statisticService;

    @Autowired
    CurrentActivitiesService currentActivitiesService;

    @RequestMapping("/getUserStatistic/{name}")
    @ResponseBody
    UserStatisticDto getUserStatistic(@PathVariable String name)
    {
        List<ChannelDescriptor> recommendations = getRecommendations(name);
        List<ChannelDescriptor> favouriteStreams = getUsersFavouriteStreams(name);
        List<ViewerInfoIntervalDto> infoIntervalDtos = getLastDayUserStatistic(name);
        List<String> currentBrowsableStreams = currentActivitiesService.findUser(name);

        return new UserStatisticDto(infoIntervalDtos, favouriteStreams, recommendations, currentBrowsableStreams);
    }

    @RequestMapping("/getRecommendations/{name}")
    @ResponseBody
    List<ChannelDescriptor> getRecommendations(@PathVariable String name){

        List<ChannelDescriptor> result = Lists.newArrayList();
        statisticService.getRecommendStreams(name, 5).forEach(channel -> {
                    result.add(convertToDto(channel));
                }
        );

        return result;
    }

    @RequestMapping("/getUsersFavouriteStreams/{name}")
    @ResponseBody
    List<ChannelDescriptor> getUsersFavouriteStreams(@PathVariable String name){

        List<ChannelDescriptor> result = Lists.newArrayList();
        statisticService.getBestStreamsSorted(name).forEach( channelEntry -> {
                    result.add(convertToDto(channelEntry.getStream()));
                }
        );

        return result;
    }

    @RequestMapping("/getStreamStatistic/{name}")
    @ResponseBody
    StreamStatisticDto getStreamStatistic(@PathVariable String name){

        StreamStatisticDto streamStatisticDto = convertToDto(statisticService.getStreamStatistic(name));
        streamStatisticDto.setInfoIntervals(getStreamsViewersActivities(name));
        streamStatisticDto.setOnline(currentActivitiesService.isStreamOnline(name));
        return streamStatisticDto;
    }

    @RequestMapping(value = "/getStreamRating")
    @ResponseBody
    List<ChannelEstimateDto> getStreamRating(){

        List<ChannelEstimateDto> streamsWithCount = Lists.newArrayList();
        statisticService.getMostPopularStreams().forEach( entry -> {
                    streamsWithCount.add(convertToDto(entry));
                }
        );

        return streamsWithCount;
    }

    @RequestMapping("/lastDayUserStatistic/{name}")
    @ResponseBody
    List<ViewerInfoIntervalDto> getLastDayUserStatistic(@PathVariable String name)
    {
        return statisticService.getLastDayUsersStatistic(name);
    }

    @RequestMapping("/lastDayStreamActivities/{name}")
    @ResponseBody
    List<StreamInfoIntervalDto> getStreamsViewersActivities(@PathVariable String name)
    {
        return statisticService.getStreamsViewersActivitiesByDefaultPeriod(name);
    }

    private StreamStatisticDto convertToDto(StreamStatistic streamStatistic){

        List<ChannelEstimateDto> streamsWithCount = Lists.newArrayList();
        streamStatistic.getStreamsWithSameViewers().forEach( entry -> {
                    ChannelEstimateDto channelEntry = convertToDto(entry);
                    streamsWithCount.add(channelEntry);
                }
        );

        List<UserDescriptor> users = Lists.newArrayList();
        streamStatistic.getViewers().forEach( viewer ->{
                    UserDescriptor user = new UserDescriptor(viewer.getName());
                    users.add(user);
                }
        );

        return new StreamStatisticDto(streamsWithCount, users);
    }

    private ChannelDescriptor convertToDto(Channel channel){
        return new ChannelDescriptor(channel.getSlug());
    }

    private ChannelEstimateDto convertToDto(ChannelEntry entry){
        return new ChannelEstimateDto<>(new ChannelDescriptor(entry.getStream().getSlug()), entry.getEstimate());
    }
}
