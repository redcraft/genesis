package net.redcraft.genesis;

import com.mongodb.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

@SpringBootApplication
public class GenesisApplication {

    public static void main(String[] args) {
        SpringApplication.run(GenesisApplication.class, args);
    }

}
