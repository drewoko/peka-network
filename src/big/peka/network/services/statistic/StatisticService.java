package big.peka.network.services.statistic;

import big.peka.network.data.hibernate.model.Channel;
import big.peka.network.data.hibernate.model.User;
import big.peka.network.data.hibernate.model.UserActivity;
import big.peka.network.data.hibernate.repositories.interfaces.ChannelRepository;
import big.peka.network.data.hibernate.repositories.interfaces.UserRepository;
import big.peka.network.web.controllers.dto.StreamStatisticDto;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.assertj.core.util.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class StatisticService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ChannelRepository channelRepository;

    private static final int BEST_ACTIVITIES_COUNT = 10;
    private static final int TOP_STREAMS_COUNT = 15;
    private static final int TOP_VIEWERS_ANOTHER_STREAMS = 10;
    private static final int MAX_NOT_FAMILIAR_STREAM_TIME = 60000;
    private static final int MIN_FAVOURITE_STREAM_TIME = 3600000;
    private static final int MIN_FAVOURITE_STREAM_ACTIVITY_TIME = 1800000;

    public StreamStatisticDto getCommonStatistic(){

        Set<User> users = userRepository.findAll();

        TreeSet<ChannelEntry<Long>> sortedStreamsByCount =
                new TreeSet<>((Comparator<ChannelEntry<Long>>)
                        (e1, e2) ->
                                !e1.getEstimate().equals(e2.getEstimate())
                                        ? e1.getEstimate().compareTo(e2.getEstimate())
                                        : e1.toString().compareTo(e2.toString())
                );
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
                    if (sortedStreamsByCount.size() < TOP_STREAMS_COUNT){
                        sortedStreamsByCount.add(new ChannelEntry<>(channel, count));
                    }
                    else {
                        if (count > sortedStreamsByCount.first().getEstimate()){
                            sortedStreamsByCount.remove(sortedStreamsByCount.first());
                            sortedStreamsByCount.add(new ChannelEntry<>(channel, count));
                        }
                    }
        });

        return new StreamStatisticDto(sortedStreamsByCount, null);

    }

    public StreamStatisticDto getStreamStatistic(String streamName){

        Channel stream = channelRepository.findBySlug(streamName);
        Set<User> users = userRepository.findAll();

        TreeSet<ChannelEntry<Long>> sortedStreamsByCount =
                new TreeSet<>((Comparator<ChannelEntry<Long>>)
                        (e1, e2) ->
                                !e1.getEstimate().equals(e2.getEstimate())
                                        ? e1.getEstimate().compareTo(e2.getEstimate())
                                        : e1.toString().compareTo(e2.toString())
                );

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
                    if (sortedStreamsByCount.size() < TOP_VIEWERS_ANOTHER_STREAMS){
                        sortedStreamsByCount.add(new ChannelEntry<>(channel, count));
                    }
                    else {
                        if (count > sortedStreamsByCount.first().getEstimate()){
                            sortedStreamsByCount.remove(sortedStreamsByCount.first());
                            sortedStreamsByCount.add(new ChannelEntry<>(channel, count));
                        }
                    }
                }
        );

        return new StreamStatisticDto(sortedStreamsByCount, viewers);
    }

    public Set<Channel> getRecommendStreams(String userName, int streamsCount){

        TreeSet<AggregatedChannelEntries<Double>> sortedChannelEstimates =
                new TreeSet<>((Comparator<AggregatedChannelEntries<Double>>)
                        (e1, e2) ->
                            !e1.getAggregatedValue().equals(e2.getAggregatedValue())
                                    ? e1.getAggregatedValue().compareTo(e2.getAggregatedValue())
                                    : e1.toString().compareTo(e2.toString())
                );

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

        TreeSet<ChannelEntry<Long>> sortedStreamsByCount =
                new TreeSet<>((Comparator<ChannelEntry<Long>>)
                        (e1, e2) ->
                                !e1.getEstimate().equals(e2.getEstimate())
                                        ? e1.getEstimate().compareTo(e2.getEstimate())
                                        : e1.toString().compareTo(e2.toString())
                );

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
                            && (userActivity == null || userActivity.getTime() < MAX_NOT_FAMILIAR_STREAM_TIME)){
                        if (sortedStreamsByCount.size() < streamsCount){
                            sortedStreamsByCount.add(new ChannelEntry<>(channel, count));
                        }
                        else {
                            if (count > sortedStreamsByCount.last().getEstimate()){
                                sortedStreamsByCount.remove(sortedStreamsByCount.last());
                                sortedStreamsByCount.add(new ChannelEntry<>(channel, count));
                            }
                        }
                    }
                }
        );

        return sortedStreamsByCount.stream().map(ChannelEntry::getStream).collect(Collectors.toSet());

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
        long totalTime = 0;
        boolean containsRequiredStreams = requiredStreams.size() == 0;
        int foundedRequiredStreams = 0;
        for (UserActivity activity : activities){
            Channel channel = activity.getChannel();
            if (activity.getMessageCount() != 0){
                double chatActivity = channel.getTime() / activity.getMessageCount() / activity.getMessageCount();
                maxChatActivity = Math.min(maxChatActivity, chatActivity);
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

        TreeSet<ChannelEntry<Double>> sortedStreams =
                new TreeSet<>((Comparator<ChannelEntry<Double>>)
                        (e1, e2) ->
                                !e1.getEstimate().equals(e2.getEstimate())
                                        ? e1.getEstimate().compareTo(e2.getEstimate())
                                        : e1.toString().compareTo(e2.toString())
                );

        for (UserActivity activity : activities){

            Double estimate = calculateEstimate(activity, maxChatActivity, totalTime);
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

    private Double calculateEstimate(UserActivity activity, double maxChatActivity, long totalTime){

        long activityTime = activity.getTime();
        long totalStreamTime = activity.getChannel().getTime();

        double estimate = 0;
        if (totalStreamTime > MIN_FAVOURITE_STREAM_TIME && activityTime > MIN_FAVOURITE_STREAM_ACTIVITY_TIME){
            estimate = 1 -
                    Math.exp(-6 * activityTime * (2d / totalStreamTime + 6d / activityTime / totalTime));
            double chatEstimate = activity.getMessageCount() * activity.getMessageCount() / (activity.getChannel().getTime() / maxChatActivity);
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

    public class ChannelEntry<T> {

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
    }

    private class AggregatedChannelEntries<T> {

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
    }
}
