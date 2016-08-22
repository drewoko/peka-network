package big.peka.network.funstream.data.api.model.request;

import java.util.HashMap;

public class ContentRequest {

    private String content = "stream";
    private HashMap<String, Object> category;
    private String type = "all";

    public String getContent() {
        return content;
    }

    public HashMap<String, Object> getCategory() {
        return category;
    }

    public String getType() {
        return type;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCategory(HashMap<String, Object> category) {
        this.category = category;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ContentRequest(String content, HashMap<String, Object> category, String type) {
        this.content = content;
        this.category = category;
        this.type = type;
    }
}
