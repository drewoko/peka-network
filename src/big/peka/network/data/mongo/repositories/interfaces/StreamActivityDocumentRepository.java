package big.peka.network.data.mongo.repositories.interfaces;

import big.peka.network.data.mongo.model.StreamActivityDocument;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface StreamActivityDocumentRepository extends CrudRepository<StreamActivityDocument, String> {

    List<StreamActivityDocument> deleteByActivityTimeBefore(Date date);
}
