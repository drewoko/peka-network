package big.peka.network.services.preproccessor.intrefaces;

import big.peka.network.funstream.data.model.MessageInfo;
import big.peka.network.funstream.data.model.StreamActivityInfo;
import big.peka.network.funstream.data.model.UserActivityInfo;

import java.util.List;

public interface FunstreamsDataPreprocessor {

    void safeData(List<UserActivityInfo> userActivityInfos, List<MessageInfo> messageInfos, List<StreamActivityInfo> streamActivityInfos);
}
