package big.peka.network.data.mongo.repositories.interfaces;

import big.peka.network.data.mongo.model.MessageDocument;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface MessageDocumentRepository extends CrudRepository<MessageDocument, String> {

    List<MessageDocument> findByStream_Slug(String slug);

    List<MessageDocument> deleteByTimeBefore(Date date);
}
