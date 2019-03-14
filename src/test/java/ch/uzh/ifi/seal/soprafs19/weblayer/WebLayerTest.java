package ch.uzh.ifi.seal.soprafs19.weblayer;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs19.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;

import org.json.JSONObject;

import java.time.LocalDate;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class WebLayerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Test
    public void addNewUser() throws Exception {
        JSONObject user = new JSONObject();
        user.put("username", "testusername");
        user.put("password", "testpassword");

        this.mockMvc.perform(post("/users").contentType("application/json;charset=UTF-8").content(user.toString()))
                .andExpect(status().isOk());
    }

    @Test
    public void addExistingUser() throws Exception {
        User existingUser = new User();
        existingUser.setUsername("takenname");
        existingUser.setPassword("password");
        existingUser.setBirthday("01.01.1990");
        existingUser.setEmail("email@test");
        existingUser.setCreationDate(LocalDate.now().toString());
        this.userRepository.save(existingUser);


        JSONObject newUser = new JSONObject();
        newUser.put("username", "takenname"); //Taken
        newUser.put("password", "pw");
        newUser.put("email", "an@email");
        newUser.put("birthday", "10.10.2010");


        this.mockMvc.perform(post("/users").contentType("application/json;charset=UTF-8").content(newUser.toString()))
                .andExpect(status().isConflict());

    }

    @Test
    public void getUserProfileByID() throws Exception {
        User existingUser2 = new User();
        existingUser2.setUsername("aname");
        existingUser2.setPassword("password");
        existingUser2.setBirthday("01.01.1990");
        existingUser2.setEmail("anemail@test");
        existingUser2.setCreationDate(LocalDate.now().toString());
        existingUser2.setId(1l);
        this.userRepository.save(existingUser2);


        this.mockMvc.perform(get("/users/{id}", 1l).contentType("application/json;charset=UTF-8").content(existingUser2.toString()))
                .andExpect(status().isOk());
    }

    @Test
    public void idNotFound() throws Exception {

        this.mockMvc.perform(get("/users/{id}", 12l))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateUserProfile() throws Exception {
        User editedUser = new User();
        editedUser.setUsername("anothername");
        editedUser.setPassword("password");
        editedUser.setBirthday("01.01.1990");
        editedUser.setEmail("anothermail@test");
        editedUser.setCreationDate(LocalDate.now().toString());
        editedUser.setId(1l);
        this.userRepository.save(editedUser);

        JSONObject updatedU = new JSONObject();
        updatedU.put("username", "newname");
        updatedU.put("birthday", "19.12.2001");


        this.mockMvc.perform(put("/users/{id}", 1l).contentType("application/json;charset=UTF-8").content(updatedU.toString()))
                .andExpect(status().isOk());

    }

    @Test
    public void updateUserProfileFail() throws Exception{

        JSONObject updateUser = new JSONObject();
        updateUser.put("username", "takennname"); //Taken
        updateUser.put("birthday", "1444-10-10");


        this.mockMvc.perform(put("/users/{id}", 50l).contentType("application/json;charset=UTF-8").content(updateUser.toString()))
                .andExpect(status().isNotFound());

    }


}
