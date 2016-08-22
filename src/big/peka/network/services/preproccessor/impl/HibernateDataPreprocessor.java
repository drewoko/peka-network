package big.peka.network.services.preproccessor.impl;

import big.peka.network.data.hibernate.model.Channel;
import big.peka.network.data.hibernate.model.UserActivity;
import big.peka.network.data.hibernate.repositories.interfaces.ChannelRepository;
import big.peka.network.data.hibernate.repositories.interfaces.UserActivityRepository;
import big.peka.network.data.hibernate.repositories.interfaces.UserRepository;
import big.peka.network.funstream.data.api.model.response.Stream;
import big.peka.network.funstream.data.api.model.response.User;
import big.peka.network.funstream.data.model.MessageInfo;
import big.peka.network.funstream.data.model.StreamActivityInfo;
import big.peka.network.funstream.data.model.UserActivityInfo;
import big.peka.network.services.preproccessor.intrefaces.FunstreamsDataPreprocessor;
import com.google.common.collect.Maps;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class HibernateDataPreprocessor implements FunstreamsDataPreprocessor {

    @Autowired
    ChannelRepository channelRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserActivityRepository userActivityRepository;

    @Override
    public void safeData(List<UserActivityInfo> userActivityInfos, List<MessageInfo> messageInfos, List<StreamActivityInfo> streamActivityInfos) {
        Map<Pair<User, Stream>, Long> usersActivityTime = Maps.newHashMap();

        for (UserActivityInfo info : userActivityInfos){

            User user = info.getUser();
            Set<Stream> streams = info.getStreams();

            for (Stream stream : streams){
                Pair<User, Stream> key = new Pair<>(user, stream);
                Long currentTimeValue = usersActivityTime.get(key);
                if (currentTimeValue == null){
                    currentTimeValue = 0L;
                }
                currentTimeValue += 20000L;

                usersActivityTime.put(key, currentTimeValue);
            }
        }

        for (Map.Entry<Pair<User, Stream>, Long> entry : usersActivityTime.entrySet()){
            User user = entry.getKey().getKey();
            Stream stream = entry.getKey().getValue();
            long time = entry.getValue();

            big.peka.network.data.hibernate.model.User userEntity = userRepository.findOne(user.getId());
            if (userEntity == null){
                userEntity = new big.peka.network.data.hibernate.model.User();
                userEntity.setName(user.getName());
                userEntity.setId(user.getId());
                userRepository.save(userEntity);
            }

            Channel channelEntity = channelRepository.findOne(stream.getId());
            if (channelEntity == null){

                long ownerId = stream.getOwner().getId();
                big.peka.network.data.hibernate.model.User ownerEntity = userRepository.findOne(ownerId);
                if (ownerEntity == null){
                    ownerEntity = new big.peka.network.data.hibernate.model.User();
                    ownerEntity.setName(stream.getOwner().getName());
                    ownerEntity.setId(ownerId);
                    userRepository.save(ownerEntity);
                }

                channelEntity = new Channel();
                channelEntity.setSlug(stream.getSlug());
                channelEntity.setId(stream.getId());
                channelEntity.setOwner(ownerEntity);
                channelRepository.save(channelEntity);
            }

            UserActivity userActivityEntity = userActivityRepository.findByUserIdAndChannelId(
                    user.getId(),
                    stream.getId()
            );

            if (userActivityEntity == null){
                userActivityEntity = new UserActivity();
                userActivityEntity.setChannel(channelEntity);
                userActivityEntity.setUser(userEntity);
            }

            userActivityEntity.addTime(time);
            userActivityRepository.save(userActivityEntity);

        }

        Map<Pair<User, Stream>, Long> usersMessagesCount = Maps.newHashMap();

        for (MessageInfo info : messageInfos){

            User user = info.getSender();
            Stream channel = info.getStream();

            Pair<User, Stream> key = new Pair<>(user, channel);
            Long currentTimeValue = usersMessagesCount.get(key);
            if (currentTimeValue == null){
                currentTimeValue = 0L;
            }
            currentTimeValue ++;

            usersMessagesCount.put(key, currentTimeValue);
        }

        for (Map.Entry<Pair<User, Stream>, Long> entry : usersMessagesCount.entrySet()){
            User user = entry.getKey().getKey();
            Stream channel = entry.getKey().getValue();
            long messageCount = entry.getValue();

            big.peka.network.data.hibernate.model.User userEntity = userRepository.findOne(user.getId());
            if (userEntity == null) {
                userEntity = new big.peka.network.data.hibernate.model.User();
                userEntity.setName(user.getName());
                userEntity.setId(user.getId());
                userRepository.save(userEntity);
            }

            Channel channelEntity = channelRepository.findOne(channel.getId());
            if (channelEntity == null){

                long ownerId = channel.getOwner().getId();
                big.peka.network.data.hibernate.model.User ownerEntity = userRepository.findOne(ownerId);
                if (ownerEntity == null){
                    ownerEntity = new big.peka.network.data.hibernate.model.User();
                    ownerEntity.setName(channel.getOwner().getName());
                    ownerEntity.setId(ownerId);
                    userRepository.save(ownerEntity);
                }

                channelEntity = new Channel();
                channelEntity.setId(channel.getId());
                channelEntity.setSlug(channel.getSlug());
                channelEntity.setOwner(ownerEntity);
                channelRepository.save(channelEntity);
            }

            UserActivity userActivityEntity = userActivityRepository.findByUserIdAndChannelId(
                    user.getId(),
                    channel.getId()
            );

            if (userActivityEntity == null){
                userActivityEntity = new UserActivity();
                userActivityEntity.setChannel(channelEntity);
                userActivityEntity.setUser(userEntity);
            }

            userActivityEntity.addMessageCount(messageCount);
            userActivityRepository.save(userActivityEntity);

        }

        for(StreamActivityInfo info : streamActivityInfos){

            Stream stream = info.getStream();
            Channel channelEntity = channelRepository.findOne(stream.getId());
            if (channelEntity == null){

                long ownerId = stream.getOwner().getId();
                big.peka.network.data.hibernate.model.User ownerEntity = userRepository.findOne(ownerId);
                if (ownerEntity == null){
                    ownerEntity = new big.peka.network.data.hibernate.model.User();
                    ownerEntity.setName(stream.getOwner().getName());
                    ownerEntity.setId(ownerId);
                    userRepository.save(ownerEntity);
                }

                channelEntity = new Channel();
                channelEntity.setId(stream.getId());
                channelEntity.setSlug(stream.getSlug());
                channelEntity.setOwner(ownerEntity);
                channelRepository.save(channelEntity);
            }

            Long currentTimeValue = channelEntity.getTime();
            if (currentTimeValue == null){
                currentTimeValue = 0L;
            }
            currentTimeValue += 20000L;
            channelEntity.setTime(currentTimeValue);
            channelRepository.save(channelEntity);
        }
    }
}
