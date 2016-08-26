package big.peka.network.web.controllers;

import big.peka.network.data.hibernate.model.Channel;
import big.peka.network.services.statistic.StatisticService;
import big.peka.network.services.statistic.StatisticService.StreamStatistic;
import big.peka.network.services.statistic.StatisticService.ChannelEntry;
import big.peka.network.web.controllers.dto.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
public class RecommendationsController {

    @Autowired
    StatisticService statisticService;

    @RequestMapping("/getRecommendations/{name}")
    @ResponseBody
    Set<Channel> getRecommendations(@PathVariable String name){
        return statisticService.getRecommendStreams(name, 5);
    }

    @RequestMapping("/getUsersStreamTop/{name}")
    @ResponseBody
    List<ChannelDescriptor> getUsersStreamTop(@PathVariable String name){

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

        return convertToDto(statisticService.getStreamStatistic(name));
    }

    @RequestMapping(value = "/getCommonStatistic")
    @ResponseBody
    List<ChannelEstimateDto> getCommonStatistic(){

        List<ChannelEstimateDto> streamsWithCount = Lists.newArrayList();
        statisticService.getMostPopularStreams().forEach( entry -> {
                    streamsWithCount.add(convertToDto(entry));
                }
        );

        return streamsWithCount;
    }

    @RequestMapping("/lastDayUserStatistic/{name}")
    @ResponseBody
    ResponseEntity<List<ViewerInfoIntervalDto>> getLastDayUserStatistic(@PathVariable String name)
    {
        return new ResponseEntity<>(statisticService.getLastDayUsersStatistic(name), HttpStatus.OK);
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
