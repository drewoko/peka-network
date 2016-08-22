package big.peka.network.data.mongo.repositories.interfaces;


import big.peka.network.data.mongo.model.UserActivityDocument;
import org.springframework.data.repository.CrudRepository;

public interface UserActivityDocumentRepository extends CrudRepository<UserActivityDocument, String> {
}
