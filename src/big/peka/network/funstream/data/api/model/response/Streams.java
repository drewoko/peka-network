package big.peka.network.funstream.data.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Streams {
  /* not complete implementation, should also have category field containing parent and current
  categories */

    private final List<Stream> streams;

    public Streams(@JsonProperty("content") List<Stream> streams) {
        this.streams = streams;
    }

    public List<Stream> getStreams() {
        return streams;
    }

    @Override
    public String toString() {
        return "StreamsResponseEntity{" +
                "streams=" + streams +
                '}';
    }
}
