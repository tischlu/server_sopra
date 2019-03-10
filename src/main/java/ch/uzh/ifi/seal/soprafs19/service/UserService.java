package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ch.uzh.ifi.seal.soprafs19.exceptions.userException;

import java.time.LocalDate;

import java.util.UUID;

@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;


    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Iterable<User> getUsers() {
        return this.userRepository.findAll();
    }

    public User createUser(User newUser) {
        newUser.setToken(UUID.randomUUID().toString());
        newUser.setStatus(UserStatus.ONLINE);
        newUser.setCreationDate(LocalDate.now().toString());
        userRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    public User checkUsername(User checkedUser) throws userException  {
        User targetUser = userRepository.findByUsername(checkedUser.getUsername());
        if (targetUser != null) {
            if (targetUser.getPassword().equals(checkedUser.getPassword())) {
                targetUser.setStatus(UserStatus.ONLINE);
                log.debug("login successfull");
                return targetUser;
            } else {
                throw new userException("Wrong password");
            }
        } else {
            throw new userException("Invalid username");
        }
    }

    public Long returnID(User loggedInUser) {
        Long id = loggedInUser.getId();
        return id;
    }
}
