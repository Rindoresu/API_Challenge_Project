package ar.rindoresu.apinotificationchallenge.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public class UserRequest {

    public interface Create {}
    public interface Update {}

    @NotBlank(groups = Create.class, message = "Username is required")
    private String username;

    @Email(groups = {Create.class, Update.class}, message = "Email must be valid")
    @NotBlank(groups = Create.class, message = "Email is required")
    private String email;

    @NotBlank(groups = Create.class, message = "Password is required")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRequest(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public UserRequest() {
    }
}