package io.econexion.lab.users.dto;

import java.util.UUID;

public record UserDto(UUID id, String name, String email) {}
