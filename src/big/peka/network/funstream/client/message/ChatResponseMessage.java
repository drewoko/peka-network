package big.peka.network.funstream.client.message;

import big.peka.network.funstream.data.api.model.response.Stream;

public class ChatResponseMessage implements Message {

    Object[] chatResponse;
    Stream stream;

    public Object[] getChatResponse() {
        return chatResponse;
    }

    public void setChatResponse(Object[] chatResponse) {
        this.chatResponse = chatResponse;
    }

    public Stream getStream() {
        return stream;
    }

    public void setStream(Stream stream) {
        this.stream = stream;
    }

    public ChatResponseMessage(Object[] chatResponse, Stream stream) {
        this.chatResponse = chatResponse;
        this.stream = stream;
    }
}
