package big.peka.network.funstream.client;

import big.peka.network.funstream.data.model.UserActivityInfo;
import big.peka.network.funstream.data.api.model.response.Stream;
import big.peka.network.funstream.client.message.ChatResponseMessage;
import big.peka.network.funstream.client.message.IntermissionMessage;
import com.google.common.collect.*;
import big.peka.network.funstream.data.api.model.request.UsersRequest;
import big.peka.network.funstream.data.api.model.response.User;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

@Component
public class ChatResponseHandler {

    private final static long INTERMISSION = 1000;

    private static final Logger LOGGER = Logger.getLogger(ChatResponseHandler.class);

    private boolean intermission;
    private boolean intermissionCompromised;

    private Set<ChatResponseMessage> results = Sets.newHashSet();

    @Autowired
    FunstreamsClientContext context;

    public void onReceive(Object msg) {

        if (msg instanceof ChatResponseMessage){
            ChatResponseMessage response = (ChatResponseMessage) msg;
            results.add(response);

            intermissionCompromised = true;
            if (!intermission){
                intermission();
            }

        }
        else if (msg instanceof IntermissionMessage){
            if (intermissionCompromised) {
                intermission();
            } else {
                intermission = false;
                checkResponse();
            }
        }
    }

    private void intermission(){
        intermission = true;
        intermissionCompromised = false;
        Thread waitingThread = new Thread(() -> {
            try {
                Thread.sleep(INTERMISSION);
                onReceive(IntermissionMessage.newInstance());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        waitingThread.start();
    }

    private void checkResponse(){

        Multimap<Integer, Stream> usersStreams = HashMultimap.create();
        List<UserActivityInfo> userActivityInfos = Lists.newArrayList();
        List<Integer> ids = Lists.newArrayList();

        for (ChatResponseMessage response : getResults()){

            JSONObject result = (JSONObject) response.getChatResponse()[0];
            JSONObject channels = result.optJSONObject("result");
            if (channels != null) {
                JSONArray usersId = null;
                try {
                    usersId = channels.getJSONArray("users");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int j = 0; j < usersId.length(); j++) {
                    try {
                        Integer id = (Integer) usersId.get(j);
                        ids.add(id);
                        usersStreams.put(id, response.getStream());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        if (!ids.isEmpty()) {
            Integer[] idsArray = new Integer[ids.size()];
            LinkedHashMap<String, Object>[] users = FunstreamsApi.FunstreamsApiFactory.getInstance().getUsers(new UsersRequest(ids.toArray(idsArray)));

            for (LinkedHashMap map : users) {
                Integer id = (Integer)map.get("id");
                String name = map.get("name").toString();
                userActivityInfos.add(new UserActivityInfo(
                        Calendar.getInstance().getTime(),
                        new User(id, name),
                        ImmutableSet.copyOf(usersStreams.get(id))));
            }
        }

        context.addNewUserActivityInfos(userActivityInfos);

        LOGGER.info(context.getLastUserActivityInfos().size()+" users handled");
    }

    private Set<ChatResponseMessage> getResults(){
        Set<ChatResponseMessage> resultSet = ImmutableSet.copyOf(results);
        results.clear();
        return resultSet;
    }
}
