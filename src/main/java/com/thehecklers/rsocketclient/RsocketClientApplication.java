package com.thehecklers.rsocketclient;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Random;

@SpringBootApplication
public class RsocketClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(RsocketClientApplication.class, args);
    }

}

@Configuration
class ClientConfig {
    @Bean
    RSocketRequester requester(RSocketRequester.Builder builder) {
        return builder.tcp("localhost", 9091);
    }
}

@Component
@EnableScheduling
//@RestController
@AllArgsConstructor
class ThirdComponent {
    private final RSocketRequester requester;

    @Scheduled(fixedRate = 1_000)
    void reqStream() {
//    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    Flux<Aircraft> reqStream() {
//        return requester.route("reqstream")
        requester.route("reqstream")
                .data(Instant.now())
                .retrieveFlux(Aircraft.class)
                .subscribe(ac -> System.out.println("🛫 " + ac));
    }
}

@Data
class Aircraft {
    private String callsign, flightno, reg, type;
    private int altitude, heading, speed;
    private double lat, lon;
}