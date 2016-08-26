package big.peka.network.data.mongo.repositories.interfaces;

import big.peka.network.data.mongo.model.UserActivityDocument;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface UserActivityDocumentRepository extends CrudRepository<UserActivityDocument, String> {

    List<UserActivityDocument> findByUser_Name(String name);

    List<UserActivityDocument> findByStream_Slug(String slug);

    List<UserActivityDocument> deleteByTimeBefore(Date date);
}
