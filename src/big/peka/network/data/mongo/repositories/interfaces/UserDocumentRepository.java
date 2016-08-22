package big.peka.network.data.mongo.repositories.interfaces;

import big.peka.network.data.mongo.model.UserDocument;
import org.springframework.data.repository.CrudRepository;

public interface UserDocumentRepository extends CrudRepository<UserDocument, String> {
}
