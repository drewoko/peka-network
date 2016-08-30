package big.peka.network.services.statistic;

import big.peka.network.funstream.client.FunstreamsClientContext;
import big.peka.network.funstream.data.api.model.response.Stream;
import big.peka.network.funstream.data.model.StreamActivityInfo;
import big.peka.network.funstream.data.model.UserActivityInfo;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Service
public class CurrentActivitiesService {

    @Autowired
    FunstreamsClientContext context;

    public List<String> findUser(String name){
        List<String> result = Lists.newArrayList();
        Set<UserActivityInfo> infos = context.getLastUserActivityInfos();
        boolean founded = false;
        Iterator<UserActivityInfo> usersIterator = infos.iterator();
        while (!founded && usersIterator.hasNext()){
            UserActivityInfo info = usersIterator.next();
            if (info.getUser().getName().toLowerCase().equals(name.toLowerCase())){
                founded = true;
                for (Stream stream : info.getStreams()){
                    result.add("sc2tv.ru/channel/" + stream.getSlug() + " ");
                }
            }
        }
        return result;
    }

    public boolean isStreamOnline(String name){

        List<StreamActivityInfo> streamInfos = context.getLastStreamActivityInfo();
        for (StreamActivityInfo info : streamInfos){
            if (info.getStream().getSlug().toLowerCase().equals(name.toLowerCase())){
                return true;
            }
        }
        return false;
    }
}
