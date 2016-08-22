package big.peka.network.web.controllers;

import big.peka.network.services.statistic.StatisticService;
import big.peka.network.web.controllers.dto.StreamStatisticDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RecommendationsController {

    @Autowired
    StatisticService statisticService;

    @RequestMapping("/getRecommendations/{name}")
    String getRecommendations(@PathVariable String name){

        StringBuffer result = new StringBuffer();
        statisticService.getRecommendStreams(name, 5)
                .stream()
                .forEach(channel -> result.append(channel+" "));
        return result.toString();
    }

    @RequestMapping("/getUsersStreamTop/{name}")
    String getUsersStreamTop(@PathVariable String name){

        StringBuffer result = new StringBuffer();
        statisticService.getBestStreams(name).forEach(
                ((channel, value) -> result.append(channel+" "))
        );
        return result.toString();
    }

    @RequestMapping("/getStreamStatistic/{name}")
    String getStreamStatistic(@PathVariable String name){

        StringBuffer result = new StringBuffer();
        StreamStatisticDto statistic = statisticService.getStreamStatistic(name);
        statistic.getStreamsWithSameViewers()
                .stream()
                .forEach(estimate -> result.append(" " + estimate.getStream().getSlug() + " " + estimate.getEstimate()));

        result.append(" viewers:");
        statistic.getViewers()
                .stream()
                .forEach(viewer -> result.append(" " + viewer.getName()));

        return result.toString();
    }

    @RequestMapping("/getCommonStatistic")
    String getCommonStatistic(){
        StringBuffer result = new StringBuffer();
        StreamStatisticDto statistic = statisticService.getCommonStatistic();
        statistic.getStreamsWithSameViewers()
                .stream()
                .forEach(estimate -> result.append(" " + estimate.getStream().getSlug() + " " + estimate.getEstimate()));

        return result.toString();
    }
}
