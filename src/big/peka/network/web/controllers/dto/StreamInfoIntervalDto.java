package big.peka.network.web.controllers.dto;

import big.peka.network.common.DateRange;

import java.util.Date;

public class StreamInfoIntervalDto extends DateRange {

    private String streamSlug;
    private Long messageCount = 0L;
    private Long usersCount = 0L;

    public StreamInfoIntervalDto(Date startDate, Date endDate, String streamSlug, long messageCount, long usersCount) {
        super(startDate, endDate);
        this.streamSlug = streamSlug;
        this.messageCount = messageCount;
        this.usersCount = usersCount;
    }

    public StreamInfoIntervalDto(Date startDate, Date endDate, String streamSlug){
        super(startDate, endDate);
        this.streamSlug = streamSlug;
    }

    public StreamInfoIntervalDto(){
    }

    public String getStreamSlug() {
        return streamSlug;
    }

    public void setStreamSlug(String streamSlug) {
        this.streamSlug = streamSlug;
    }

    public Long getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(Long messageCount) {
        this.messageCount = messageCount;
    }

    public Long getUsersCount() {
        return usersCount;
    }

    public void setUsersCount(Long usersCount) {
        this.usersCount = usersCount;
    }

    public void incrementUsersCount() {
        usersCount++;
    }

    public void incrementMessageCount(){
        messageCount++;
    }
}
