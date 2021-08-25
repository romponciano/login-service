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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureDataMongo
@AutoConfigureMockMvc
@SpringBootTest
public class SaveTest {

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
    void return_saved_when_correctUser() throws Exception {
        mvc.perform(
                post("/teste/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(user))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("password").isNotEmpty())
                .andExpect(jsonPath("username").value(user.getUsername()));

        assertNotNull(repository.findByUsername(user.getUsername()));
    }

    @Test
    void return_badRequest_when_usernameAlreadyExists() throws Exception {
        User saved = repository.save(user);
        saved.setId("different id");

        mvc.perform(
                post("/teste/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(saved))
        ).andExpect(status().isBadRequest());
    }

    @Test
    void return_badRequest_when_idAlreadyExists() throws Exception {
        User saved = repository.save(user);
        saved.setUsername("different username");

        mvc.perform(
                post("/teste/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(saved))
        ).andExpect(status().isBadRequest());
    }

    @Test
    void return_badRequest_when_idAndUsernameAlreadyExists() throws Exception {
        User saved = repository.save(user);

        mvc.perform(
                post("/teste/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(saved))
        ).andExpect(status().isBadRequest());
    }
}
