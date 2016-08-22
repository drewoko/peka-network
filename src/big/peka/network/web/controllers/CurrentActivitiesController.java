package big.peka.network.web.controllers;

import big.peka.network.funstream.client.FunstreamsClientContext;
import big.peka.network.funstream.data.model.StreamActivityInfo;
import big.peka.network.funstream.data.model.UserActivityInfo;
import big.peka.network.funstream.data.api.model.response.Stream;
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
    FunstreamsClientContext funstreamsClientContext;

    @RequestMapping("/getUsers")
    String getUsers() {
        StringBuffer result = new StringBuffer();
        Set<UserActivityInfo> infos = funstreamsClientContext.getLastUserActivityInfos();
        result.append("users count = "+ infos.size()+" ");
        for (UserActivityInfo info : infos){
            result.append(info.getUser().getName() + " ");
        }
        return result.toString();
    }

    @RequestMapping("/getUser/{name}")
    String getUser(@PathVariable String name){
        StringBuffer result = new StringBuffer();
        Set<UserActivityInfo> infos = funstreamsClientContext.getLastUserActivityInfos();
        boolean founded = false;
        Iterator<UserActivityInfo> usersIterator = infos.iterator();
        while (!founded && usersIterator.hasNext()){
            UserActivityInfo info = usersIterator.next();
            if (info.getUser().getName().toLowerCase().equals(name.toLowerCase())){
                founded = true;
                result.append(info.getActivityTime()+" ");
                result.append(info.getUser().getName() + "\n");
                for (Stream stream : info.getStreams()){
                    result.append("sc2tv.ru/channel/"+stream.getSlug()+" ");
                }
            }
        }
        if (!founded){
            result.append("user not found");
        }
        return result.toString();
    }

    @RequestMapping("/getStreams")
    String getStreams(){
        StringBuffer result = new StringBuffer();
        List<StreamActivityInfo> streamInfos = funstreamsClientContext.getLastStreamActivityInfo();
        for (StreamActivityInfo info : streamInfos){
            result.append(info.getStream().getName()+" ");
        }
        return result.toString();
    }
}
