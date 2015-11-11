package net.redcraft.genesis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({SlackConfig.class})
public class GenesisApplication {

    public static void main(String[] args) {
        SpringApplication.run(GenesisApplication.class, args);
    }

}
