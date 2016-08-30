package big.peka.network.web.controllers.dto;

import big.peka.network.common.DateRange;

import java.util.Date;

public class StreamInfoIntervalDto extends DateRange implements Comparable<StreamInfoIntervalDto> {

    private Long messageCount = 0L;
    private Long usersCount = 0L;

    public StreamInfoIntervalDto(Date startDate, Date endDate, long messageCount, long usersCount) {
        super(startDate, endDate);
        this.messageCount = messageCount;
        this.usersCount = usersCount;
    }

    public StreamInfoIntervalDto(Date startDate, Date endDate){
        super(startDate, endDate);
    }

    public StreamInfoIntervalDto(){
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

    @Override
    public int compareTo(StreamInfoIntervalDto anotherInfo) {

        int result = this.startDate.compareTo(anotherInfo.getStartDate());
        if (result != 0){
            return result;
        }

        return this.endDate.compareTo(anotherInfo.getEndDate());
    }
}
