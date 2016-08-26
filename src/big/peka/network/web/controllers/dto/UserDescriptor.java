package big.peka.network.web.controllers.dto;

public class UserDescriptor {

    String name;

    public UserDescriptor(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
