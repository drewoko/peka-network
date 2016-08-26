package big.peka.network.common;

import javax.annotation.Nonnull;
import java.util.Date;

public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    public static long getDifference(@Nonnull Date firstDate, @Nonnull Date secondDate){
        return firstDate.getTime() - secondDate.getTime();
    }

    public static Date getDateAfterDuration(@Nonnull Date date, long duration){
        return new Date(date.getTime() + duration);
    }

    public static Date getDateBeforeDuration(@Nonnull Date date, long duration){
        return new Date(date.getTime() - duration);
    }

    public static DateRange getDataRangeByPeriod(long period, @Nonnull Date point){
        long periodCount = point.getTime() / period;
        return new DateRange(new Date(periodCount * period), new Date((periodCount+1) * period));
    }
}
