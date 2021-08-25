package ponciano.rom;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ponciano.rom.domain.dto.UserRequest;
import ponciano.rom.domain.entity.User;
import ponciano.rom.domain.repository.UserRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureDataMongo
@AutoConfigureMockMvc
@SpringBootTest
public class UpdateTest {

    @Autowired private UserRepository repository;
    @Autowired private MockMvc mvc;

    private User user;
    private final Gson gson = new Gson();

    @BeforeEach
    void setup() {
        user = new User(null, "test-user", "user-pass");
        repository.deleteAll();
    }

    @Test
    void return_ok_when_updateValidUsername() throws Exception {
        User saved = repository.save(user);

        UserRequest request = new UserRequest();
        request.setUsername(user.getUsername());
        request.setPassword(user.getPassword());
        request.setNewUsername("another username");

        mvc.perform(
                put("/teste/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(request))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(saved.getId()))
                .andExpect(jsonPath("username").value(request.getNewUsername()))
                .andExpect(jsonPath("password").value(request.getPassword()));
    }

    @Test
    void return_ok_when_updateValidPassword() throws Exception {
        User saved = repository.save(user);

        UserRequest request = new UserRequest();
        request.setUsername(user.getUsername());
        request.setPassword(user.getPassword());
        request.setNewPassword("new pass");

        mvc.perform(
                put("/teste/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(request))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(saved.getId()))
                .andExpect(jsonPath("username").value(request.getUsername()))
                .andExpect(jsonPath("password").value(request.getNewPassword()));
    }

    @Test
    void return_ok_when_updateValidUsernameAndPassword() throws Exception {
        User saved = repository.save(user);

        UserRequest request = new UserRequest();
        request.setUsername(user.getUsername());
        request.setPassword(user.getPassword());
        request.setNewUsername("another username");
        request.setNewPassword("new pass");

        mvc.perform(
                put("/teste/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(request))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(saved.getId()))
                .andExpect(jsonPath("username").value(request.getNewUsername()))
                .andExpect(jsonPath("password").value(request.getNewPassword()));
    }

    @Test
    void return_badRequest_when_updateInvalidUsername() throws Exception {
        repository.save(user);

        UserRequest request = new UserRequest();
        request.setUsername("invalid username");
        request.setPassword(user.getPassword());
        request.setNewUsername("another username");
        request.setNewPassword("new pass");

        mvc.perform(
                put("/teste/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(request))
        ).andExpect(status().isBadRequest());
    }

    @Test
    void return_badRequest_when_updateInvalidPassword() throws Exception {
        repository.save(user);

        UserRequest request = new UserRequest();
        request.setUsername(user.getUsername());
        request.setPassword("invalid pass");
        request.setNewUsername("another username");
        request.setNewPassword("new pass");

        mvc.perform(
                put("/teste/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(request))
        ).andExpect(status().isBadRequest());
    }

    @Test
    void return_badRequest_when_updateInvalidUsernameAndPassword() throws Exception {
        repository.save(user);

        UserRequest request = new UserRequest();
        request.setUsername("invalid username");
        request.setPassword("invalid pass");
        request.setNewUsername("another username");
        request.setNewPassword("new pass");

        mvc.perform(
                put("/teste/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(request))
        ).andExpect(status().isBadRequest());
    }
}
