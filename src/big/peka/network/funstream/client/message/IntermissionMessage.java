package big.peka.network.funstream.client.message;

public class IntermissionMessage implements Message {

    private IntermissionMessage(){}

    public static IntermissionMessage newInstance() {
        return new IntermissionMessage();
    }
}
