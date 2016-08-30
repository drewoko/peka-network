package big.peka.network.web.controllers.dto;

import big.peka.network.common.DateRange;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class ViewerInfoIntervalDto extends DateRange implements Comparable<ViewerInfoIntervalDto> {

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

    @Override
    public int compareTo(ViewerInfoIntervalDto anotherInfo) {

        int result = this.startDate.compareTo(anotherInfo.getStartDate());
        if (result != 0){
            return result;
        }

        result = this.endDate.compareTo(anotherInfo.getEndDate());
        if (result != 0){
            return result;
        }

        return this.streamSlug.compareTo(anotherInfo.getStreamSlug());
    }
}
