package big.peka.network.data.hibernate.repositories.interfaces;

import big.peka.network.data.hibernate.model.UserActivity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.cdi.Eager;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Transient;
import java.util.Set;

@Repository
public interface UserActivityRepository extends CrudRepository<UserActivity, Long> {

    UserActivity findByUserIdAndChannelId(Long userId, Long channelId);

    Set<UserActivity> findByUserId(Long userId);

    Set<UserActivity> findById(Long id);

    Set<UserActivity> findByChannelId(Long channelId);

    Set<UserActivity> findByChannelIdIn(Set<Long> streams);

}
