package big.peka.network.funstream.client;

import big.peka.network.funstream.data.model.MessageInfo;
import big.peka.network.funstream.data.model.StreamActivityInfo;
import big.peka.network.funstream.data.model.UserActivityInfo;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class FunstreamsClientContext {

    Set<UserActivityInfo> userActivityInfos = Sets.newHashSet();

    Set<UserActivityInfo> lastUserActivityInfos = Sets.newHashSet();
    List<StreamActivityInfo> streamActivityInfo = Lists.newArrayList();
    List<StreamActivityInfo> lastStreamActivityInfo = Lists.newArrayList();
    List<MessageInfo> messageInfos = Lists.newArrayList();

    public void addNewUserActivityInfos(List<UserActivityInfo> newUserActivityInfos){
        lastUserActivityInfos.clear();
        lastUserActivityInfos.addAll(newUserActivityInfos);
        userActivityInfos.addAll(lastUserActivityInfos);
    }

    public void addNewStreamActivityInfos(List<StreamActivityInfo> newStreamActivityInfo){
        lastStreamActivityInfo = newStreamActivityInfo;
        streamActivityInfo.addAll(lastStreamActivityInfo);
    }

    public List<UserActivityInfo> pollUserActivityInfos() {
        List<UserActivityInfo> infos = ImmutableList.copyOf(userActivityInfos);
        userActivityInfos.clear();
        return infos;
    }

    public Set<UserActivityInfo> getLastUserActivityInfos() {
        return lastUserActivityInfos;
    }

    public List<StreamActivityInfo> pollStreamActivityInfo() {
        List<StreamActivityInfo> infos = ImmutableList.copyOf(streamActivityInfo);
        streamActivityInfo.clear();
        return infos;
    }

    public void setStreamActivityInfo(List<StreamActivityInfo> streamActivityInfo) {
        this.streamActivityInfo = streamActivityInfo;
    }

    public List<StreamActivityInfo> getLastStreamActivityInfo() {
        return lastStreamActivityInfo;
    }

    public void setLastStreamActivityInfo(List<StreamActivityInfo> lastStreamActivityInfo) {
        this.lastStreamActivityInfo = lastStreamActivityInfo;
    }

    public void addMessageInfo(MessageInfo messageInfo){
        messageInfos.add(messageInfo);
    }

    public List<MessageInfo> pollMessageInfos() {
        List<MessageInfo> infos = ImmutableList.copyOf(messageInfos);
        messageInfos.clear();
        return infos;
    }

    public void setMessageInfos(List<MessageInfo> messageInfos) {
        this.messageInfos = messageInfos;
    }
}
