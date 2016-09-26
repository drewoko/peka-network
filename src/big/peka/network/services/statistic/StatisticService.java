package big.peka.network.services.statistic;

import big.peka.network.common.DateRange;
import big.peka.network.common.DateUtils;
import big.peka.network.data.hibernate.model.Channel;
import big.peka.network.data.hibernate.model.User;
import big.peka.network.data.hibernate.model.UserActivity;
import big.peka.network.data.hibernate.repositories.interfaces.ChannelRepository;
import big.peka.network.data.hibernate.repositories.interfaces.UserRepository;
import big.peka.network.data.mongo.model.MessageDocument;
import big.peka.network.data.mongo.model.UserActivityDocument;
import big.peka.network.data.mongo.repositories.interfaces.MessageDocumentRepository;
import big.peka.network.data.mongo.repositories.interfaces.UserActivityDocumentRepository;
import big.peka.network.web.controllers.dto.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.assertj.core.util.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static big.peka.network.common.DateUtils.getDataRangeByPeriod;
import static big.peka.network.common.DateUtils.getDateAfterDuration;
import static big.peka.network.common.DateUtils.getDifference;

@Service
public class StatisticService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ChannelRepository channelRepository;
    @Autowired
    UserActivityDocumentRepository userActivityDocumentRepository;
    @Autowired
    MessageDocumentRepository messageDocumentRepository;

    private static final int BEST_ACTIVITIES_COUNT = 10;
    private static final int TOP_STREAMS_COUNT = 15;
    private static final int TOP_VIEWERS_ANOTHER_STREAMS = 15;
    private static final long MAX_NOT_FAMILIAR_STREAM_TIME = DateUtils.MILLIS_PER_MINUTE;
    private static final long MIN_FAVOURITE_STREAM_TIME = DateUtils.MILLIS_PER_HOUR;
    private static final long MIN_FAVOURITE_STREAM_ACTIVITY_TIME = DateUtils.MILLIS_PER_HOUR / 2;
    private static final long TIME_PER_PERIOD_FOR_STREAM_ACTIVITIES = DateUtils.MILLIS_PER_MINUTE * 15;

    public Set<ChannelEntry<Long>> getMostPopularStreams(){

        Set<User> users = userRepository.findAll();

        TreeSet<ChannelEntry<Long>> sortedStreamsByCount = new TreeSet<>();
        Map<Channel, Long> channelsCount = Maps.newHashMap();

        for (User user : users){
            Set<Channel> usersTop = getBestStreams(user, Sets.newHashSet()).keySet();
            usersTop
                    .stream()
                    .filter(channel -> !user.equals(channel.getOwner()))
                    .forEach(channel -> {
                        Long value = channelsCount.get(channel);
                        value = value == null ? 1 : value + 1;
                        channelsCount.put(channel, value);
                    });
        }

        channelsCount.forEach(
                (channel, count) -> {
                    if (sortedStreamsByCount.size() < TOP_STREAMS_COUNT) {
                        sortedStreamsByCount.add(new ChannelEntry<>(channel, count));
                    } else {
                        if (count > sortedStreamsByCount.first().getEstimate()) {
                            sortedStreamsByCount.remove(sortedStreamsByCount.first());
                            sortedStreamsByCount.add(new ChannelEntry<>(channel, count));
                        }
                    }
                });

        return sortedStreamsByCount;

    }

    public StreamStatistic getStreamStatistic(String streamName){

        Channel stream = channelRepository.findBySlug(streamName);
        Set<User> users = userRepository.findAll();

        TreeSet<ChannelEntry<Long>> sortedStreamsByCount = new TreeSet<>();

        Map<Channel, Long> channelsCount = Maps.newHashMap();
        List<User> viewers = Lists.newArrayList();

        users.stream()
                .filter(user -> !user.equals(stream.getOwner()))
                .forEach(user -> {
                    Set<Channel> usersTop = getBestStreams(user, stream).keySet();
                    if (usersTop.contains(stream)) {
                        viewers.add(user);
                        usersTop
                                .stream()
                                .filter(channel -> !channel.equals(stream))
                                .forEach(channel -> {
                                    Long value = channelsCount.get(channel);
                                    value = value == null ? 1 : value + 1;
                                    channelsCount.put(channel, value);
                                });
                    }
                });


        channelsCount.forEach(
                (channel, count) -> {
                    if (sortedStreamsByCount.size() < TOP_VIEWERS_ANOTHER_STREAMS) {
                        sortedStreamsByCount.add(new ChannelEntry<>(channel, count));
                    } else {
                        if (count > sortedStreamsByCount.first().getEstimate()) {
                            sortedStreamsByCount.remove(sortedStreamsByCount.first());
                            sortedStreamsByCount.add(new ChannelEntry<>(channel, count));
                        }
                    }
                }
        );

        return new StreamStatistic(sortedStreamsByCount, viewers);
    }

    public Set<Channel> getRecommendStreams(String userName, int streamsCount){

        TreeSet<AggregatedChannelEntries<Double>> sortedChannelEstimates = new TreeSet<>();

        User processingUser = userRepository.findByName(userName);
        Map<Channel, Double> topUsersStreams = getBestStreams(processingUser, Sets.newHashSet());

        Set<User> users = userRepository.findAll();

        users.forEach(
                user -> {
                    Map<Channel, Double> topStreams = getBestStreams(user, topUsersStreams.keySet());
                    double aggregateEstimate = getAggregateScore(topUsersStreams, topStreams);

                    if (aggregateEstimate > 0){
                        if (sortedChannelEstimates.size() < BEST_ACTIVITIES_COUNT){
                            sortedChannelEstimates.add(new AggregatedChannelEntries<>(topStreams.keySet(), aggregateEstimate));
                        }
                        else if (sortedChannelEstimates.first().getAggregatedValue() < aggregateEstimate){
                            sortedChannelEstimates.remove(sortedChannelEstimates.first());
                            sortedChannelEstimates.add(new AggregatedChannelEntries<>(topStreams.keySet(), aggregateEstimate));
                        }
                    }
                }
        );

        TreeSet<ChannelEntry<Long>> sortedStreamsByCount = new TreeSet<>();

        Map<Channel, Long> streamToRecommendationCounts =
                sortedChannelEstimates.stream()
                        .map(AggregatedChannelEntries::getChannelEstimates)
                        .flatMap(Set::stream)
                        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        Map<Channel, UserActivity> usersChannels = processingUser.getActivities().stream().
                collect(Collectors.toMap(UserActivity::getChannel, Function.identity()));


        streamToRecommendationCounts.forEach(
                (channel, count) -> {
                    UserActivity userActivity = usersChannels.get(channel);
                    if (!topUsersStreams.containsKey(channel)
                            && !channel.getOwner().equals(processingUser)
                            && (userActivity == null || userActivity.getTime() < MAX_NOT_FAMILIAR_STREAM_TIME)) {
                        if (sortedStreamsByCount.size() < streamsCount) {
                            sortedStreamsByCount.add(new ChannelEntry<>(channel, count));
                        } else {
                            if (count > sortedStreamsByCount.last().getEstimate()) {
                                sortedStreamsByCount.remove(sortedStreamsByCount.last());
                                sortedStreamsByCount.add(new ChannelEntry<>(channel, count));
                            }
                        }
                    }
                }
        );

        return sortedStreamsByCount.stream().map(ChannelEntry::getStream).collect(Collectors.toSet());

    }

    public List<StreamInfoIntervalDto> getStreamsViewersActivitiesByDefaultPeriod(String name){
        return getStreamsViewersActivitiesPerPeriod(name, TIME_PER_PERIOD_FOR_STREAM_ACTIVITIES);
    }

    public List<StreamInfoIntervalDto> getStreamsViewersActivitiesPerPeriod(String name, long period){

        List<UserActivityDocument> viewersActivities = userActivityDocumentRepository.findByStream_Slug(name);
        List<MessageDocument> messages = messageDocumentRepository.findByStream_Slug(name);

        List<StreamInfoIntervalDto> ranges = Lists.newArrayList();
        Map<DateRange, StreamInfoIntervalDto> previousDateRanges = Maps.newHashMap();
        Map<DateRange, Set<String>> viewersOnRange = Maps.newHashMap();

        viewersActivities.forEach( activityDocument -> {

            Date activityDate = activityDocument.getTime();
            DateRange range = getDataRangeByPeriod(period, activityDate);
            StreamInfoIntervalDto streamInfoIntervalDto = previousDateRanges.get(range);
            if (streamInfoIntervalDto == null){
                streamInfoIntervalDto = new StreamInfoIntervalDto();
                streamInfoIntervalDto.setStartDate(range.getStartDate());
                streamInfoIntervalDto.setEndDate(range.getEndDate());
            }
            Set<String> users = viewersOnRange.get(range);
            if (users == null){
                users = Sets.newHashSet();
            }
            if (!users.contains(activityDocument.getUser().getName())){
                streamInfoIntervalDto.incrementUsersCount();
                users.add(activityDocument.getUser().getName());
            }
            viewersOnRange.put(range, users);
            previousDateRanges.put(range, streamInfoIntervalDto);
        });

        messages.forEach(messageDocument -> {

            Date activityDate = messageDocument.getTime();
            DateRange range = getDataRangeByPeriod(TIME_PER_PERIOD_FOR_STREAM_ACTIVITIES, activityDate);
            StreamInfoIntervalDto streamInfoIntervalDto = previousDateRanges.get(range);
            if (streamInfoIntervalDto == null) {
                streamInfoIntervalDto = new StreamInfoIntervalDto();
                streamInfoIntervalDto.setStartDate(range.getStartDate());
                streamInfoIntervalDto.setEndDate(range.getEndDate());
            }
            streamInfoIntervalDto.incrementMessageCount();
            previousDateRanges.put(range, streamInfoIntervalDto);
        });

        ranges.addAll(previousDateRanges.values());
        Collections.sort(ranges);

        return ranges;
    }

    public List<ViewerInfoIntervalDto> getLastDayUsersStatistic(String name){

        List<UserActivityDocument> userActivityDocuments = userActivityDocumentRepository.findByUser_Name(name);

        userActivityDocuments = userActivityDocuments
                .stream()
                .sorted((a1, a2) -> {
                    int result = a1.getTime().compareTo(a2.getTime());
                    return result == 0 ? a1.toString().compareTo(a2.toString()) : result;
                })
                .collect(Collectors.toList());

        List<ViewerInfoIntervalDto> ranges = Lists.newArrayList();
        Map<String, ViewerInfoIntervalDto> previousDateRanges = Maps.newHashMap();

        userActivityDocuments.forEach(activityDocument -> {

            Date activityDate = activityDocument.getTime();
            String activityStream = activityDocument.getStream().getSlug();
            ViewerInfoIntervalDto previousRange = previousDateRanges.get(activityStream);

            if (previousRange != null) {
                if (getDifference(activityDate, previousRange.getEndDate()) <= DateUtils.MILLIS_PER_MINUTE) {
                    previousRange = new ViewerInfoIntervalDto(previousRange.getStartDate(), getDateAfterDuration(activityDate, 20000), activityStream, name);
                } else {
                    ranges.add(previousRange);
                    previousRange = new ViewerInfoIntervalDto(activityDate, getDateAfterDuration(activityDate, 20000), activityStream, name);
                }
            } else {
                previousRange = new ViewerInfoIntervalDto(activityDate, getDateAfterDuration(activityDate, 20000), activityStream, name);
            }
            previousDateRanges.put(activityStream, previousRange);
        });

        ranges.addAll(previousDateRanges.values());
        Collections.sort(ranges);

        return ranges;
    }

    public Set<ChannelEntry> getBestStreamsSorted(String userName){

        TreeSet<ChannelEntry> sortedEntries = new TreeSet<>();
        getBestStreams(userName).forEach(((channel, estimate) -> {
                    sortedEntries.add(new ChannelEntry<>(channel, estimate));
                })
        );

        return sortedEntries;
    }

    public Map<Channel, Double> getBestStreams(String userName){
        User processingUser = userRepository.findByName(userName);
        return getBestStreams(processingUser, Sets.newHashSet());
    }

    public Map<Channel, Double> getBestStreams(User user, Channel channel){
        Set<Channel> channelSet = Sets.newHashSet();
        channelSet.add(channel);
        return getBestStreams(user, channelSet);
    }

    public Map<Channel, Double> getBestStreams(@Nonnull User user, @Nonnull Set<Channel> requiredStreams){

        Set<UserActivity> activities = user.getActivities();
        double maxChatActivity = Double.MAX_VALUE;
        double maxMessageCount = 0;
        long totalTime = 0;
        boolean containsRequiredStreams = requiredStreams.size() == 0;
        int foundedRequiredStreams = 0;
        for (UserActivity activity : activities){
            Channel channel = activity.getChannel();
            if (activity.getMessageCount() != 0){
                double chatActivity = channel.getTime() / activity.getMessageCount() / activity.getMessageCount();
                maxChatActivity = Math.min(maxChatActivity, chatActivity);
                maxMessageCount = Math.max(maxMessageCount, activity.getMessageCount());
                totalTime += activity.getTime();
            }
            if (requiredStreams.contains(channel)){
                foundedRequiredStreams++;
            }
            containsRequiredStreams |= foundedRequiredStreams > 0;
        }

        if (!containsRequiredStreams){
            return Maps.newHashMap();
        }

        TreeSet<ChannelEntry<Double>> sortedStreams = new TreeSet<>();

        for (UserActivity activity : activities){

            Double estimate = calculateEstimate(activity, maxChatActivity, totalTime, maxMessageCount);
            if (sortedStreams.size() < BEST_ACTIVITIES_COUNT){
                if (estimate > 0){
                    sortedStreams.add(new ChannelEntry<>(activity.getChannel(), estimate));
                }
            }
            else if (sortedStreams.first().getEstimate() < estimate){
                sortedStreams.remove(sortedStreams.first());
                sortedStreams.add(new ChannelEntry<>(activity.getChannel(), estimate));
            }
        }

        Map<Channel, Double> result = Maps.newHashMap();
        double score = 1;
        for (ChannelEntry streamEstimate : sortedStreams){
            result.put(streamEstimate.getStream(), score * score);
            score++;
        }

        return result;
    }

    private Double calculateEstimate(UserActivity activity, double maxChatActivity, long totalTime, double maxMessageCount){

        long activityTime = activity.getTime();
        long totalStreamTime = activity.getChannel().getTime();

        double estimate = 0;
        if (totalStreamTime > MIN_FAVOURITE_STREAM_TIME && activityTime > MIN_FAVOURITE_STREAM_ACTIVITY_TIME){
            estimate = 1 -
                    Math.exp(-6 * activityTime * (4d / (3d * totalStreamTime)));
            estimate += 2d * activityTime / totalTime;
            double chatEstimate = 0.5 * (activity.getMessageCount() * activity.getMessageCount() / (activity.getChannel().getTime() / maxChatActivity)
                    + activity.getMessageCount() / maxMessageCount);
            estimate += chatEstimate;
        }

        return estimate;
    }

    public double getAggregateScore(Map<Channel, Double> firstEstimates, Map<Channel, Double> secondEstimates){

        double score = 0;

        for (Map.Entry<Channel, Double> entry : firstEstimates.entrySet()){
            Double value = secondEstimates.get(entry.getKey());
            if (value != null){
                score += Math.min(entry.getValue(), value);
            }
        }

        return score;
    }

    public static class ChannelEntry<T> implements Comparable<ChannelEntry<T>> {

        private Channel stream;
        private T value;

        ChannelEntry(Channel stream, T estimate){
            this.stream = stream;
            this.value = estimate;
        }

        public Channel getStream() {
            return stream;
        }

        public void setStream(Channel stream) {
            this.stream = stream;
        }

        public T getEstimate() {
            return value;
        }

        public void setEstimate(T estimate) {
            this.value = estimate;
        }

        @Override
        public int compareTo(ChannelEntry<T> entry) {
            if (value instanceof Comparable){
                Comparable val = (Comparable) value;
                return !val.equals(entry.value)
                        ? val.compareTo(entry.value)
                        : this.toString().compareTo(entry.toString());
            }
            else {
                return this.toString().compareTo(entry.toString());
            }
        }
    }

    private class AggregatedChannelEntries<T> implements Comparable<AggregatedChannelEntries<T>> {

        private Set<Channel> channelEstimates = Sets.newHashSet();
        private T aggregatedValue;

        public AggregatedChannelEntries(Set<Channel> channelEstimates, T aggregatedValue) {
            this.channelEstimates = channelEstimates;
            this.aggregatedValue = aggregatedValue;
        }

        public Set<Channel> getChannelEstimates() {
            return channelEstimates;
        }

        public void setChannelEstimates(Set<Channel> channelEstimates) {
            this.channelEstimates = channelEstimates;
        }

        public T getAggregatedValue() {
            return aggregatedValue;
        }

        public void setAggregatedValue(T aggregatedValue) {
            this.aggregatedValue = aggregatedValue;
        }

        @Override
        public int compareTo(AggregatedChannelEntries entries) {

            if (aggregatedValue instanceof Comparable){

                Comparable value = (Comparable)aggregatedValue;

                return !value.equals(entries.aggregatedValue)
                        ? value.compareTo(entries.aggregatedValue)
                        : this.toString().compareTo(entries.toString());
            }
            else return this.toString().compareTo(entries.toString());
        }
    }

    public class StreamStatistic {

        Set<ChannelEntry<Long>> streamsWithSameViewers;
        List<User> viewers = Lists.newArrayList();

        public StreamStatistic(Set<ChannelEntry<Long>> streamsWithSameViewers, List<User> viewers) {
            this.streamsWithSameViewers = streamsWithSameViewers;
            this.viewers = viewers;
        }

        public Set<ChannelEntry<Long>> getStreamsWithSameViewers() {
            return streamsWithSameViewers;
        }

        public void setStreamsWithSameViewers(Set<ChannelEntry<Long>> streamsWithSameViewers) {
            this.streamsWithSameViewers = streamsWithSameViewers;
        }

        public List<User> getViewers() {
            return viewers;
        }

        public void setViewers(List<User> viewers) {
            this.viewers = viewers;
        }

    }

}
