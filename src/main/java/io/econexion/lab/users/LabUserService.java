package io.econexion.lab.users;

import io.econexion.lab.users.dto.*;
import java.util.*;

public interface LabUserService {
    List<UserDto> findAll();
    Optional<UserDto> findById(UUID id);

    UserDto findById(String id);

    UserDto create(UserCreateRequest req);
    Optional<UserDto> update(UUID id, UserUpdateRequest req);
    boolean delete(UUID id);

    UserDto update(String id, UserUpdateRequest req);

    void delete(String id);
}
