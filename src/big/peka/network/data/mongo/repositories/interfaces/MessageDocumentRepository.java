package big.peka.network.data.mongo.repositories.interfaces;

import big.peka.network.data.mongo.model.MessageDocument;
import org.springframework.data.repository.CrudRepository;

public interface MessageDocumentRepository extends CrudRepository<MessageDocument, String> {

}
