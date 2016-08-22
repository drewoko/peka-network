package big.peka.network.funstream.client;

import big.peka.network.funstream.data.model.StreamActivityInfo;
import big.peka.network.funstream.data.api.model.request.ContentRequest;
import big.peka.network.funstream.data.api.model.response.Stream;
import big.peka.network.funstream.data.api.model.response.Streams;
import big.peka.network.funstream.client.message.ChatResponseMessage;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@EnableScheduling
public class FunstreamsClient {

    private Socket socket;
    private boolean connected;

    @Autowired
    private ChatResponseHandler chatResponseHandler;
    @Autowired
    FunstreamsClientContext context;
    @Autowired
    ChatEventListener chatEventListener;

    private static final Logger LOGGER = Logger.getLogger(FunstreamsClient.class);

    @PostConstruct
    public void init() throws URISyntaxException {

        IO.Options options = new IO.Options();
        options.reconnection = true;
        options.reconnectionDelay = 10;
        options.reconnectionAttempts = 200;

        options.transports = new String[] {"websocket"};

        socket = IO.socket("https://funstream.tv", options);

        socket.on(Socket.EVENT_CONNECT, this::connected);
        socket.on(Socket.EVENT_CONNECT_ERROR, this::connectionFailed);
        socket.on(Socket.EVENT_DISCONNECT, this::disconnected);
        socket.on(Socket.EVENT_ERROR, this::connectionFailed);
        socket.on("/chat/message", this::readChat);

        socket.connect();
    }

    private void disconnected(Object... objects) {
        connected = false;
        LOGGER.info("Connection closed");
    }

    public void connectionFailed(Object... msg){
        LOGGER.info("Connection failed with "+msg);
    }

    private void connected(Object... msg) {
        LOGGER.info("Connected to Funstream");
        connected = true;
        join("all");
        getUserInfo();
    }

    @Scheduled(fixedRate = 20000)
    private void getUserInfo(){
        if (connected){
            LOGGER.info("Getting stream and user infos");
            getAllUsersOnActiveStreams();
        }
    }

    private void join(String channel) {
        try {
            socket.emit("/chat/join", new JSONObject().put("channel", channel));
            LOGGER.info("Chat joined");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void readChat(Object... msg){
        chatEventListener.onMessage(msg);
    }

    public void getAllUsersOnActiveStreams() {

        HashMap<String, Object> category = Maps.newHashMap();
        category.put("id", null);
        category.put("slug", "top");
        Streams response = FunstreamsApi.FunstreamsApiFactory
                .getInstance()
                .getStreams(new ContentRequest("stream", category, "all"));
        final Set<Stream> streams = response.getStreams().stream().collect(Collectors.toSet());
        List<StreamActivityInfo> streamInfos = Lists.newArrayList();

        for (Stream stream : streams){
            try {
                streamInfos.add(new StreamActivityInfo(stream, Calendar.getInstance().getTime()));
                getUsers(stream);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        context.addNewStreamActivityInfos(streamInfos);
        LOGGER.info(streamInfos.size()+" streams handled");
    }

    public void getUsers(Stream stream) throws JSONException {
        socket.emit("/chat/channel/list", new JSONObject().put("channel", "stream/" + stream.getOwner().getId()), new Ack() {
            @Override
            public void call(Object... args) {
                chatResponseHandler.onReceive(new ChatResponseMessage(args, stream));
            }
        });
    }

}
