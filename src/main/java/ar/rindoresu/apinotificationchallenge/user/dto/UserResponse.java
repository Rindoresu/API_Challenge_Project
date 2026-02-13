package ar.rindoresu.apinotificationchallenge.user.dto;

import java.util.List;

public class UserResponse {

    private Long id;
    private String username;
    private String email;
    private String password;
    private List<String> pokemonNames;

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public List<String> getPokemonNames() {
        return pokemonNames;
    }

    public UserResponse(Long id, String username, String email, String password, List<String> pokemonNames) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.pokemonNames = pokemonNames;
    }
}