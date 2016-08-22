package big.peka.network.funstream.data.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ChannelUsers {

    private final Result result;

    public ChannelUsers(@JsonProperty("result")Result result) {
        this.result = result;
    }

    public Result getResult() {
        return result;
    }

    public class Result{
        private final int amount;
        private final List<Integer> users;

        public Result(
                @JsonProperty("amount") final int amount,
                @JsonProperty("users") final List<Integer> users
        ){
            this.amount = amount;
            this.users = users;
        }

        public int getAmount() {
            return amount;
        }

        public List<Integer> getUsers() {
            return users;
        }
    }
}
