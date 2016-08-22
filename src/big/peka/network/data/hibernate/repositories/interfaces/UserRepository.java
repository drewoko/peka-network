package big.peka.network.data.hibernate.repositories.interfaces;

import big.peka.network.data.hibernate.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.cdi.Eager;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    @EntityGraph(value = "UserActivitiesAndChannels", type = EntityGraph.EntityGraphType.FETCH)
    Set<User> findAll();

    @EntityGraph(value = "UserActivitiesAndChannels", type = EntityGraph.EntityGraphType.FETCH)
    User findByName(String name);

    @EntityGraph(value = "UserActivitiesAndChannels", type = EntityGraph.EntityGraphType.FETCH)
    User findById(Long id);
}
