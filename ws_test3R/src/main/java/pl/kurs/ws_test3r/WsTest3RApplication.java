package pl.kurs.ws_test3r;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling

public class WsTest3RApplication {

    public static void main(String[] args) {
        SpringApplication.run(WsTest3RApplication.class, args);
    }

}
