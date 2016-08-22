package big.peka.network.data.hibernate.repositories.interfaces;

import big.peka.network.data.hibernate.model.Channel;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChannelRepository extends CrudRepository<Channel, Long>{

    Channel findBySlug(String name);

}
