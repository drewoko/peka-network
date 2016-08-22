package big.peka.network.funstream.client;

import big.peka.network.funstream.data.model.MessageInfo;
import big.peka.network.funstream.data.api.model.request.ChannelRequest;
import big.peka.network.funstream.data.api.model.request.StreamRequest;
import big.peka.network.funstream.data.api.model.response.Stream;
import big.peka.network.funstream.data.api.model.response.User;
import com.google.common.collect.Lists;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Date;

@Component
public class ChatEventListener {

    @Autowired
    FunstreamsClientContext funstreamsClientContext;

    public void onMessage(Object... msg){
        JSONObject messageProperties = (JSONObject)msg[0];

        JSONObject sender = messageProperties.optJSONObject("from");
        String name = sender.optString("name");
        int id = sender.optInt("id");
        User user = new User(id, name);
        String messageText = messageProperties.optString("text");
        String channel = messageProperties.optString("channel");
        Date time = new Date((Integer)messageProperties.opt("time"));

        User channelOwner = FunstreamsApi.FunstreamsApiFactory.getInstance().
                getChannel(new ChannelRequest(Lists.newArrayList(channel))).iterator().next().getUser();
        if (channelOwner != null){
            StreamRequest streamRequest = new StreamRequest(null, channelOwner.getName());
            Stream stream = FunstreamsApi.FunstreamsApiFactory.getInstance().getStream(streamRequest);

            MessageInfo messageInfo = new MessageInfo(time, messageText, user, stream);

            funstreamsClientContext.addMessageInfo(messageInfo);
        }
    }

    //This kind of message is disabled
    public void onJoin(Object msg){
        throw new NotImplementedException();
    }

    public void onLeft(Object msg){
        throw new NotImplementedException();
    }
}
