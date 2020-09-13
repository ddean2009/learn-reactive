package com.flydean.config;

import io.netty.util.internal.StringUtil;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import lombok.Data;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import static io.r2dbc.spi.ConnectionFactoryOptions.*;

/**
 * @author wayne
 * @version DBConfig,  2020/9/12
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "r2dbc")
public class DBConfig {

    private String url;
    private String user;
    private String password;

    @Bean
    public ConnectionFactory connectionFactory() {
        ConnectionFactoryOptions baseOptions = ConnectionFactoryOptions.parse(url);
        ConnectionFactoryOptions.Builder ob = ConnectionFactoryOptions.builder().from(baseOptions);
        if (!StringUtil.isNullOrEmpty(user)) {
            ob = ob.option(USER, user);
        }
        if (!StringUtil.isNullOrEmpty(password)) {
            ob = ob.option(PASSWORD, password);
        }
        return ConnectionFactories.get(ob.build());
    }

    @Bean
    public CommandLineRunner initDatabase(ConnectionFactory cf) {

        return (args) ->
                Flux.from(cf.create())
                        .flatMap(c ->
                                Flux.from(c.createBatch()
                                        .add("drop table if exists Users")
                                        .add("create table Users(" +
                                                "id IDENTITY(1,1)," +
                                                "firstname varchar(80) not null," +
                                                "lastname varchar(80) not null)")
                                        .add("insert into Users(firstname,lastname)" +
                                                "values('flydean','ma')")
                                        .add("insert into Users(firstname,lastname)" +
                                                "values('jacken','yu')")
                                        .execute())
                                        .doFinally((st) -> c.close())
                        )
                        .log()
                        .blockLast();
    }

}
