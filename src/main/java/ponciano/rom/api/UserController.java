package ponciano.rom.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ponciano.rom.domain.dto.UserRequest;
import ponciano.rom.domain.entity.User;
import ponciano.rom.domain.service.UserService;

@RestController
@RequestMapping("/${base-path}")
public class UserController {

    @Autowired private UserService service;

    @CrossOrigin(originPatterns = "*")
    @PostMapping("/login")
    public User login(@RequestBody User user) {
        User res = service.login(user);
        if(res == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        res.setPassword(null);
        return res;
    }

    @CrossOrigin(originPatterns = "*")
    @PostMapping("/")
    public User save(@RequestBody User user) {
        try {
            return service.save(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin(originPatterns = "*")
    @PutMapping("/")
    public User update(@RequestBody UserRequest user) {
        try {
            User res = service.update(user);
            if(res == null)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin(originPatterns = "*")
    @GetMapping("/{username}/exists")
    public ResponseEntity<String> exists(@PathVariable String username) {
        return service.existsByUsername(username)
            ? new ResponseEntity<String>(HttpStatus.BAD_REQUEST)
            : new ResponseEntity<String>(HttpStatus.OK);
    }

    @CrossOrigin(originPatterns = "*")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(
            @PathVariable String id,
            @RequestBody UserRequest request
    ) {
        if(!service.exists(id))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        return service.deleteById(id, request)
                ? new ResponseEntity<String>(HttpStatus.OK)
                : new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
    }
}
