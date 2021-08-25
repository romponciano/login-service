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
import ponciano.rom.domain.entity.User;
import ponciano.rom.domain.repository.UserRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureDataMongo
@AutoConfigureMockMvc
@SpringBootTest
public class LoginTest {

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
    void return_id_when_successfulLogin() throws Exception {
        User saved = repository.save(user);
        saved.setId(null);

        mvc.perform(
                post("/teste/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(saved))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("username").value(saved.getUsername()))
                .andExpect(jsonPath("password").isEmpty());
    }

    @Test
    void return_badRequest_when_inexistentUsername() throws Exception {
        mvc.perform(
                post("/teste/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(user))
        ).andExpect(status().isBadRequest());
    }

    @Test
    void return_badRequest_when_wrongPassword() throws Exception {
        User saved = repository.save(user);
        saved.setPassword("wrong password");

        mvc.perform(
                post("/teste/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(saved))
        ).andExpect(status().isBadRequest());
    }

    @Test
    void return_forbidden_when_usernameAlreadyExists() throws Exception {
        User saved = repository.save(user);

        mvc.perform(get("/teste/" + saved.getUsername() + "/exists"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void return_ok_when_usernameDoestExists() throws Exception {
        mvc.perform(get("/teste/new-username/exists")).andExpect(status().isOk());
    }
}
