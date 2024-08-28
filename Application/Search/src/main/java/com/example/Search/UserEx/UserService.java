package com.example.Search.UserEx;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class UserService {

    private final WebClient webClient;

    public UserService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://jsonplaceholder.typicode.com").build();
    }

    public Mono<Object[]> getUsers() {
        return webClient.get()
                .uri("/users")
                .retrieve()
                .bodyToMono(Object[].class);
    }
}
