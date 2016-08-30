package big.peka.network.services.preproccessor.impl;

import big.peka.network.common.DateUtils;
import big.peka.network.data.mongo.model.*;
import big.peka.network.data.mongo.model.StreamDocument;
import big.peka.network.data.mongo.repositories.interfaces.MessageDocumentRepository;
import big.peka.network.data.mongo.repositories.interfaces.StreamActivityDocumentRepository;
import big.peka.network.data.mongo.repositories.interfaces.UserActivityDocumentRepository;
import big.peka.network.funstream.data.model.MessageInfo;
import big.peka.network.funstream.data.model.StreamActivityInfo;
import big.peka.network.funstream.data.model.UserActivityInfo;
import big.peka.network.services.preproccessor.intrefaces.FunstreamsDataPreprocessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static big.peka.network.common.DateUtils.getDateBeforeDuration;

@Service
public class MongoDataPreprocessor implements FunstreamsDataPreprocessor {

    @Autowired
    UserActivityDocumentRepository userActivityDocumentRepository;
    @Autowired
    MessageDocumentRepository messageDocumentRepository;
    @Autowired
    StreamActivityDocumentRepository streamActivityDocumentRepository;

    @Scheduled(fixedRate = DateUtils.MILLIS_PER_HOUR)
    public void deleteOldDocuments(){

        Date currentDate = GregorianCalendar.getInstance().getTime();
        Date dateBeforeLastDay = getDateBeforeDuration(currentDate, DateUtils.MILLIS_PER_DAY);

        messageDocumentRepository.deleteByTimeBefore(dateBeforeLastDay);
        streamActivityDocumentRepository.deleteByActivityTimeBefore(dateBeforeLastDay);
        userActivityDocumentRepository.deleteByTimeBefore(dateBeforeLastDay);
    }


    @Override
    public void safeData(List<UserActivityInfo> userActivityInfos, List<MessageInfo> messageInfos, List<StreamActivityInfo> streamActivityInfos) {

        userActivityInfos.forEach( info -> {
                    UserActivityDocument activityDocument = new UserActivityDocument();
                    info.getStreams().forEach( stream -> {

                                UserDocument ownerDocument = new UserDocument();
                                ownerDocument.setName(stream.getOwner().getName());

                                StreamDocument streamDocument = new StreamDocument();
                                streamDocument.setOwner(ownerDocument);
                                streamDocument.setSlug(stream.getSlug());

                                UserDocument userDocument = new UserDocument();
                                userDocument.setName(info.getUser().getName());

                                activityDocument.setStream(streamDocument);
                                activityDocument.setTime(info.getActivityTime());
                                activityDocument.setUser(userDocument);

                                userActivityDocumentRepository.save(activityDocument);
                            }
                    );
                }
        );

        messageInfos.forEach( info -> {
            MessageDocument messageDocument = new MessageDocument();

            UserDocument ownerDocument = new UserDocument();
            ownerDocument.setName(info.getStream().getOwner().getName());

            StreamDocument streamDocument = new StreamDocument();
            streamDocument.setOwner(ownerDocument);
            streamDocument.setSlug(info.getStream().getSlug());

            messageDocument.setStream(streamDocument);

            UserDocument senderDocument = new UserDocument();
            senderDocument.setName(info.getSender().getName());

            messageDocument.setSender(senderDocument);
            messageDocument.setMessageText(info.getText());
            messageDocument.setTime(info.getTime());

            messageDocumentRepository.save(messageDocument);
        });

        streamActivityInfos.forEach( info -> {
            StreamActivityDocument streamActivityDocument = new StreamActivityDocument();

            UserDocument ownerDocument = new UserDocument();
            ownerDocument.setName(info.getStream().getOwner().getName());

            StreamDocument streamDocument = new StreamDocument();
            streamDocument.setOwner(ownerDocument);
            streamDocument.setSlug(info.getStream().getSlug());

            streamActivityDocument.setStream(streamDocument);
            streamActivityDocument.setActivityTime(info.getActivityTime());

            streamActivityDocumentRepository.save(streamActivityDocument);
        });
    }
}
