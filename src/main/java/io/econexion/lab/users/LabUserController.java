package io.econexion.lab.users;

import io.econexion.lab.users.dto.UserCreateRequest;
import io.econexion.lab.users.dto.UserUpdateRequest;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/lab/users")
@Profile("lab")
public class LabUserController {

    private final LabUserService service;
    public LabUserController(LabUserService service) { this.service = service; }

    @GetMapping
    public ResponseEntity<?> list() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable UUID id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody UserCreateRequest req) {
        var created = service.create(req);
        return ResponseEntity.created(URI.create("/lab/users/" + created.id())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody UserUpdateRequest req) {
        return service.update(id, req)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        return service.delete(id) ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
