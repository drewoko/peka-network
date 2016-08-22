package big.peka.network.config;

import big.peka.network.data.hibernate.model.Channel;
import big.peka.network.data.hibernate.model.User;
import big.peka.network.data.hibernate.model.UserActivity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

@Configuration
@Import(RepositoryRestConfigurerAdapter.class)
public class RestDataConfig extends RepositoryRestConfigurerAdapter {

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.exposeIdsFor(
                User.class,
                UserActivity.class,
                Channel.class);
    }

}