package big.peka.network.data.mongo.repositories.interfaces;

import big.peka.network.data.mongo.model.StreamActivityDocument;
import big.peka.network.data.mongo.model.StreamDocument;
import org.springframework.data.repository.CrudRepository;

public interface StreamActivityDocumentRepository extends CrudRepository<StreamActivityDocument, String> {
}
