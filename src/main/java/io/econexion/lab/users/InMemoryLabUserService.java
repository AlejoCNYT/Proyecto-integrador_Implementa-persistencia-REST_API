package io.econexion.lab.users;

import io.econexion.lab.users.dto.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;      // <-- import necesario
import org.springframework.context.annotation.Profile; // <-- import necesario

@Service
@Profile("lab")
public class InMemoryLabUserService implements LabUserService {

    private final Map<UUID, UserDto> store = new ConcurrentHashMap<>();

    public InMemoryLabUserService() {
        // Semilla opcional para que el GET liste algo
        var id = UUID.randomUUID();
        store.put(id, new UserDto(id, "Ada Lovelace", "ada@demo.test"));
    }

    @Override public List<UserDto> findAll() {
        return store.values().stream().toList();
    }

    @Override public Optional<UserDto> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override public UserDto create(UserCreateRequest req) {
        var id = UUID.randomUUID();
        var dto = new UserDto(id, req.name(), req.email());
        store.put(id, dto);
        return dto;
    }

    @Override public Optional<UserDto> update(UUID id, UserUpdateRequest req) {
        if (!store.containsKey(id)) return Optional.empty();
        var dto = new UserDto(id, req.name(), req.email());
        store.put(id, dto);
        return Optional.of(dto);
    }

    @Override public boolean delete(UUID id) {
        return store.remove(id) != null;
    }

    // --- Wrappers requeridos por la interfaz que usa String ---

    @Override
    public io.econexion.lab.users.dto.UserDto findById(String id) {
        return findById(java.util.UUID.fromString(id))
                .orElseThrow(() -> new RuntimeException("User not found: " + id));
    }

    @Override
    public io.econexion.lab.users.dto.UserDto update(String id,
                                                     io.econexion.lab.users.dto.UserUpdateRequest req) {
        return update(java.util.UUID.fromString(id), req)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));
    }

    @Override
    public void delete(String id) {
        delete(java.util.UUID.fromString(id));
    }

}
