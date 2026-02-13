package ar.rindoresu.apinotificationchallenge.user;

import ar.rindoresu.apinotificationchallenge.user.dto.UserRequest;
import ar.rindoresu.apinotificationchallenge.user.dto.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.FailedApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "User management API")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    // -----------------------------
    // 1. CREATE
    // -----------------------------
    @Operation(summary = "Create a new user")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "User values are invalid"),
            @ApiResponse(responseCode = "409", description = "Username or email already exists")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse create(@Validated(UserRequest.Create.class) @RequestBody UserRequest request) {
        return service.create(request);
    }

    // -----------------------------
    // 2. READ ALL
    // -----------------------------
    @Operation(summary = "Get all users")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of users")
    })
    @GetMapping
    public List<UserResponse> findAll() {
        return service.findAll();
    }

    // -----------------------------
    // 3. READ BY ID
    // -----------------------------
    @Operation(summary = "Get a user by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User found"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}")
    public UserResponse findById(@PathVariable Long id) {
        return service.findById(id);
    }

    // -----------------------------
    // 4. UPDATE
    // -----------------------------
    @Operation(summary = "Update a user by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User updated"),
        @ApiResponse(responseCode = "400", description = "User values are invalid"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "409", description = "Username or email already exists")
    })
    @PutMapping("/{id}")
    public UserResponse updateUser(@PathVariable Long id, @Validated(UserRequest.Update.class) @RequestBody UserRequest request) {
        return service.update(id, request);
    }

    // -----------------------------
    // 5. DELETE
    // -----------------------------
    @Operation(summary = "Delete a user by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "User deleted"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

}