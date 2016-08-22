package big.peka.network.services.preproccessor;

import big.peka.network.funstream.client.FunstreamsClientContext;
import big.peka.network.funstream.data.model.MessageInfo;
import big.peka.network.funstream.data.model.StreamActivityInfo;
import big.peka.network.funstream.data.model.UserActivityInfo;
import big.peka.network.services.preproccessor.impl.HibernateDataPreprocessor;
import big.peka.network.services.preproccessor.impl.MongoDataPreprocessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@EnableScheduling
public class DataPreprocessor {

    @Autowired
    FunstreamsClientContext funstreamsClientContext;
    @Autowired
    HibernateDataPreprocessor hibernateDataPreprocessor;
    @Autowired
    MongoDataPreprocessor mongoDataPreprocessor;

    @Scheduled(fixedRate = 45000L)
    public void aggregateData() {
        Thread processingThread = new Thread(this::processAndSafeNewData);
        processingThread.start();
    }

    private void processAndSafeNewData() {

        List<UserActivityInfo> userActivityInfos = funstreamsClientContext.pollUserActivityInfos();
        List<MessageInfo> messageInfos = funstreamsClientContext.pollMessageInfos();
        List<StreamActivityInfo> streamActivityInfos = funstreamsClientContext.pollStreamActivityInfo();

        hibernateDataPreprocessor.safeData(userActivityInfos, messageInfos, streamActivityInfos);
        mongoDataPreprocessor.safeData(userActivityInfos, messageInfos, streamActivityInfos);
    }
}