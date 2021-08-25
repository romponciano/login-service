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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureDataMongo
@AutoConfigureMockMvc
@SpringBootTest
public class DeleteTest {

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
    void return_ok_when_deleteValidId() throws Exception {
        User saved = repository.save(user);

        mvc.perform(
                delete("/teste/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(saved))
        ).andExpect(status().isOk());

        assertFalse(repository.findById(saved.getId()).isPresent());
    }

    @Test
    void return_badRequest_when_deleteInvalidId() throws Exception {
        mvc.perform(
                delete("/teste/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(user))
        ).andExpect(status().isBadRequest());
    }

    @Test
    void return_badRequest_when_deleteInvalidPassword() throws Exception {
        User saved = repository.save(user);
        saved.setPassword("wrong pass");

        mvc.perform(
                delete("/teste/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(saved))
        ).andExpect(status().isBadRequest());
    }
}
