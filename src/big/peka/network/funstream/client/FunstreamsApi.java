package big.peka.network.funstream.client;

import big.peka.network.funstream.data.api.model.request.*;
import big.peka.network.funstream.data.api.model.response.Channel;
import big.peka.network.funstream.data.api.model.response.Stream;
import big.peka.network.funstream.data.api.model.response.Streams;
import big.peka.network.funstream.data.api.model.response.User;
import feign.Feign;
import feign.Logger;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.jaxrs.JAXRSModule;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.LinkedHashMap;
import java.util.List;


public interface FunstreamsApi {

    @POST
    @Path("api/content")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Streams getStreams(ContentRequest data);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("api/stream")
    public Stream getStream(StreamRequest data);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("api/user")
    public User getUser(UserRequest data);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/api/channel/data")
    public List<Channel> getChannel(ChannelRequest data);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("api/user/list")
    public LinkedHashMap[] getUsers(UsersRequest data);

    class FunstreamsApiFactory {
        public static FunstreamsApi getInstance(){
            return Feign.builder()
                    .contract(new JAXRSModule.JAXRSContract())
                    .decoder(new JacksonDecoder())
                    .encoder(new JacksonEncoder())
                    .logLevel(Logger.Level.BASIC)
                    .target(FunstreamsApi.class, "http://funstream.tv/");
        }
    }

}
