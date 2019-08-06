package tu.diplomna.guessGame.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.mem.InMemoryUsersConnectionRepository;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.social.connect.ConnectionFactoryLocator;

import javax.inject.Inject;
import javax.sql.DataSource;
import javax.xml.crypto.Data;

@Configuration
public class Config {

    @Autowired
    private DataSource dataSource;


    @Bean
    BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public UsersConnectionRepository usersConnectionRepository() {
        TextEncryptor textEncryptor = Encryptors.noOpText();
        return new InMemoryUsersConnectionRepository(connectionFactoryLocator());
    }

    @Bean
    public ConnectionFactoryLocator connectionFactoryLocator() {
        FacebookConnectionFactory facebookConnectionFactory = new FacebookConnectionFactory(
                environment.getProperty("spring.social.facebook.appId"),
                environment.getProperty("spring.social.facebook.appSecret"));
        ConnectionFactoryRegistry registry = new ConnectionFactoryRegistry();

        registry.addConnectionFactory(facebookConnectionFactory);
        return registry;
    }

    @Inject
    Environment environment;
}
