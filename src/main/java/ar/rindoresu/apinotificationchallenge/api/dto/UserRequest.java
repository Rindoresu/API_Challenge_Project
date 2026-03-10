package ar.rindoresu.apinotificationchallenge.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class UserRequest {

    @NotBlank(groups = Create.class, message = "Username is required")
    private String username;
    @Email(groups = {Create.class, Update.class}, message = "Email must be valid")
    @NotBlank(groups = Create.class, message = "Email is required")
    private String email;
    @NotBlank(groups = Create.class, message = "Password is required")
    private String password;
    // Pokémon IDs the user owns
    @NotNull(groups = {Create.class, Update.class}, message = "You need to provide a Pokemon List with valid IDs")
    private List<Integer> pokemonIds;

    public UserRequest(String username, String email, String password, List<Integer> pokemonIds) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.pokemonIds = pokemonIds;
    }

    public UserRequest() {
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

    public List<Integer> getPokemonIds() {
        return pokemonIds;
    }

    public interface Create {
    }

    public interface Update {
    }
}