package big.peka.network.web.controllers.dto;

import big.peka.network.common.DateRange;

import java.util.Date;

public class ViewerInfoIntervalDto extends DateRange {

    private String streamSlug;
    private String userName;

    public ViewerInfoIntervalDto(Date startDate, Date endDate, String streamSlug, String userName) {
        super(startDate, endDate);
        this.streamSlug = streamSlug;
        this.userName = userName;
    }

    public String getStreamSlug() {
        return streamSlug;
    }

    public void setStreamSlug(String streamSlug) {
        this.streamSlug = streamSlug;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
