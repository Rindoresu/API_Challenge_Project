package ar.rindoresu.apinotificationchallenge.api.dto;

import java.util.List;

public record UserResponse(
        Long id,
        String username,
        String email,
        String password,
        List<String> pokemonNames
) {}