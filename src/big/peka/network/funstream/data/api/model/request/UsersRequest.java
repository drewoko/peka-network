package big.peka.network.funstream.data.api.model.request;

import java.util.Arrays;

public class UsersRequest {

    private final Integer[] ids;

    public Integer[] getIds() {
        return ids;
    }

    public UsersRequest(Integer[] ids) {
        this.ids = ids;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UsersRequest that = (UsersRequest) o;

        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(ids, that.ids);

    }

    @Override
    public int hashCode() {
        return ids != null ? Arrays.hashCode(ids) : 0;
    }
}
