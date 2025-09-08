package io.econexion.lab.users;

import io.econexion.lab.users.dto.UserCreateRequest;
import io.econexion.lab.users.dto.UserDto;
import io.econexion.lab.users.dto.UserUpdateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lab/users")
// @Profile({"lab","mongo"}) // <-- opcional, sólo si quieres restringir. Mejor sin @Profile.
public class LabUserController {

    private final LabUserService service;

    public LabUserController(LabUserService service) {
        this.service = service;
    }

    @GetMapping
    public List<UserDto> list() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public UserDto get(@PathVariable String id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<UserDto> create(@RequestBody UserCreateRequest body) {
        UserDto created = service.create(body);
        return ResponseEntity.created(URI.create("/lab/users/" + created.id())).body(created);
    }

    @PutMapping("/{id}")
    public UserDto update(@PathVariable String id, @RequestBody UserUpdateRequest body) {
        return service.update(id, body);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
