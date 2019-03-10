package ch.uzh.ifi.seal.soprafs19.controller;

import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.exceptions.userException;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs19.service.UserService;
import org.springframework.web.bind.annotation.*;
import ch.uzh.ifi.seal.soprafs19.entity.User;

@RestController
public class UserController {

    private final UserService service;

    UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/users")
    Iterable<User> all() {
        return service.getUsers();
    }

    @PostMapping("/users")
    User createUser(@RequestBody User newUser) {
        return this.service.createUser(newUser);
    }

    @PostMapping("/login")
    @ExceptionHandler({userException.class})
    User checkUser(@RequestBody User checkedUser) {
        return this.service.checkUsername(checkedUser);
    }

    @GetMapping("/profile")
    Long returnID(@RequestBody User loggedInUser) { return this.service.returnID(loggedInUser); }

}
