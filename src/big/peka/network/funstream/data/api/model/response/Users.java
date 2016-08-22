package big.peka.network.funstream.data.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Users {

    private final User[] users;

    public Users(@JsonProperty User... users) {
        this.users = users;
    }

    public User[] getUsers() {
        return users;
    }

    @Override
    public String toString() {
        return "UsersResponseEntity{" +
                "users=" + users +
                '}';
    }
}
