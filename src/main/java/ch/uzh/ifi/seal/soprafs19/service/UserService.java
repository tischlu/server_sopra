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
import ch.uzh.ifi.seal.soprafs19.exceptions.notFoundException;

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
        User targetUser = this.userRepository.findByUsername(checkedUser.getUsername());
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

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("hallo"));
        //User targetUser = this.userRepository.findById(id);
        //return targetUser;

    }

    public User editCredentials(long id, User editUser) {
        User targetUser = this.userRepository.findById(id);
        if (targetUser != null && editUser.getId() == id) {
            String newUsername = editUser.getUsername();
            String newBirthday = editUser.getBirthday();
            if (newBirthday!= null) {
                targetUser.setBirthday(newBirthday);
            }
            if (newUsername != null) {
                targetUser.setUsername((newUsername));
            }
            return targetUser;
        }
    }



}
