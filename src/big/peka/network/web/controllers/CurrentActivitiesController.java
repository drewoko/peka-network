package big.peka.network.web.controllers;

import big.peka.network.funstream.client.FunstreamsClientContext;
import big.peka.network.funstream.data.model.StreamActivityInfo;
import big.peka.network.funstream.data.model.UserActivityInfo;
import big.peka.network.funstream.data.api.model.response.Stream;
import big.peka.network.services.statistic.CurrentActivitiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

@RestController
public class CurrentActivitiesController {

    @Autowired
    CurrentActivitiesService currentActivitiesService;

    @RequestMapping("/findUser/{name}")
    List<String> findUser(@PathVariable String name){
        return currentActivitiesService.findUser(name);
    }

    @RequestMapping("/getStreamStatus/{name}")
    boolean isStreamOnline(@PathVariable String name){
        return currentActivitiesService.isStreamOnline(name);
    }
}
