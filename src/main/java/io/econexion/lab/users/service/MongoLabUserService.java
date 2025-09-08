package io.econexion.lab.users.service;

import io.econexion.lab.users.LabUserService;
import io.econexion.lab.users.dto.UserCreateRequest;
import io.econexion.lab.users.dto.UserDto;
import io.econexion.lab.users.dto.UserUpdateRequest;
import io.econexion.lab.users.model.LabUser;
import io.econexion.lab.users.repository.LabUserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Profile("mongo")
public class MongoLabUserService implements LabUserService {

    private final LabUserRepository repo;

    public MongoLabUserService(LabUserRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<UserDto> findAll() {
        return repo.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public Optional<UserDto> findById(UUID id) {
        return repo.findById(id.toString()).map(this::toDto);
    }

    @Override
    public UserDto findById(String id) {
        return null;
    }

    @Override
    public UserDto create(UserCreateRequest req) {
        // Tus DTOs son records: req.name(), req.email()
        // Generamos un UUID como _id en Mongo (guardado como String)
        LabUser u = new LabUser(UUID.randomUUID().toString(), req.name(), req.email(), null);
        return toDto(repo.save(u));
    }

    @Override
    public Optional<UserDto> update(UUID id, UserUpdateRequest req) {
        return repo.findById(id.toString()).map(current -> {
            if (req.name()  != null) current.setName(req.name());
            if (req.email() != null) current.setEmail(req.email());
            return toDto(repo.save(current));
        });
    }

    @Override
    public boolean delete(UUID id) {
        String sid = id.toString();
        if (!repo.existsById(sid)) return false;
        repo.deleteById(sid);
        return true;
    }

    @Override
    public UserDto update(String id, UserUpdateRequest req) {
        return null;
    }

    @Override
    public void delete(String id) {

    }

    /* ===================== helpers ===================== */

    private UserDto toDto(LabUser u) {
        UUID uid = u.getId() != null ? UUID.fromString(u.getId()) : null;
        // Ajusta al constructor REAL de tu UserDto. Asumo: (UUID id, String name, String email)
        return new UserDto(uid, u.getName(), u.getEmail());
        // Si tu UserDto tiene otro orden/campos, cámbialo aquí.
    }

    /* --- Si tienes métodos con String en tu interfaz, descomenta y deja SIN @Override --- */
    /*
    public UserDto findById(String id) {
        return repo.findById(id).map(this::toDto)
                   .orElseThrow(() -> new RuntimeException("User not found: " + id));
    }

    public UserDto update(String id, UserUpdateRequest req) {
        LabUser current = repo.findById(id).orElseThrow(() -> new RuntimeException("User not found: " + id));
        if (req.name()  != null) current.setName(req.name());
        if (req.email() != null) current.setEmail(req.email());
        return toDto(repo.save(current));
    }

    public void delete(String id) { repo.deleteById(id); }
    */

    // --- Wrappers con String para cumplir la interfaz ---


}
