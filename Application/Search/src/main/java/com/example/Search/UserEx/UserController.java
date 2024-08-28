package com.example.Search.UserEx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public Mono<User[]> getUsers() {
        return userService.getUsers()
            .doOnNext(users -> {
                logger.info("Received {} users from API", users.length);
                for (Object user : users) {
                    logger.info("User: id={}, name={}, email={}", user.getId(), user.getName(), user.getEmail());
                }
            })
            .doOnError(error -> logger.error("Error fetching users", error));
    }
}

