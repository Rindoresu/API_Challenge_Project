package ar.rindoresu.apinotificationchallenge.user;

import ar.rindoresu.apinotificationchallenge.api.AbstractIntegrationTest;
import ar.rindoresu.apinotificationchallenge.pokemon.client.PokemonClient;
import ar.rindoresu.apinotificationchallenge.pokemon.exception.PokemonNotFoundException;
import ar.rindoresu.apinotificationchallenge.api.dto.UserRequest;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.reactive.server.WebTestClient;


import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UserIntegrationTest extends AbstractIntegrationTest {

    @LocalServerPort
    int port;
    private WebTestClient webTestClient;
    @Autowired
    private UserRepository userRepository;

    @MockitoSpyBean
    private PokemonClient pokemonClient;

    private User savedUser;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
        User u = new User(
                "ash",
                "ash@kanto.com",
                "pikachu123",
                List.of(25, 1));
        savedUser = userRepository.save(u);

        this.webTestClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port) // optional, for nicer logs
                .build();
    }


    @Test
    void testCreateUser() {
        // Arrange: mock Pokémon API responses
        Mockito.doReturn("pikachu").when(pokemonClient).getPokemonName(25);
        Mockito.doReturn("bulbasaur").when(pokemonClient).getPokemonName(1);
        Mockito.doThrow(new PokemonNotFoundException(-45))
                .when(pokemonClient)
                .getPokemonName(-45);


        UserRequest request = new UserRequest(
                "misty",
                "misty@kanto.com",
                "water123",
                List.of(25, 1)
        );

        // Act + Assert: call the endpoint
        webTestClient.post()
                .uri("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.username").isEqualTo("misty")
                .jsonPath("$.email").isEqualTo("misty@kanto.com")
                .jsonPath("$.pokemonNames").value(list -> {
                    assertThat(list)
                            .asInstanceOf(InstanceOfAssertFactories.list(String.class))
                            .containsExactlyInAnyOrder("pikachu", "bulbasaur");
                });

        // Verify the mock was actually used
        Mockito.verify(pokemonClient, Mockito.times(1)).getPokemonName(25);
        Mockito.verify(pokemonClient, Mockito.times(1)).getPokemonName(1);
        Mockito.verifyNoMoreInteractions(pokemonClient);

        // Verify the user was actually saved in the DB
        List<User> users = userRepository.findAll();
        assertThat(users).hasSize(2) // ash + misty
                .anyMatch(u -> u.getUsername().equals("misty")
                        && u.getEmail().equals("misty@kanto.com")
                );
    }

    private void assertBadRequest(UserRequest request) {
        webTestClient.post()
                .uri("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.error").exists();
    }

    @Test
    void testCreateUser_ValidationError_MissingUsername() {
        UserRequest request = new UserRequest(
                null,
                "test@example.com",
                "pass123",
                List.of(25)
        );

        assertBadRequest(request);
        Mockito.verifyNoInteractions(pokemonClient);
    }

    @Test
    void testCreateUser_ValidationError_MissingPassword() {
        UserRequest request = new UserRequest(
                "misty",
                "misty@pueblopaleta.pa",
                null,
                List.of(25)
        );

        assertBadRequest(request);
        Mockito.verifyNoInteractions(pokemonClient);
    }

    @Test
    void testCreateUser_ValidationError_MissingEmail() {
        UserRequest request = new UserRequest(
                "misty",
                null,
                "water123",
                List.of(25)
        );

        assertBadRequest(request);
        Mockito.verifyNoInteractions(pokemonClient);
    }

    @Test
    void testCreateUser_ValidationError_InvalidEmail() {
        UserRequest request = new UserRequest(
                "misty",
                "not-an-email",
                "water123",
                List.of(25)
        );

        assertBadRequest(request);
        Mockito.verifyNoInteractions(pokemonClient);
    }

    @Test
    void testCreateUser_ValidationError_MissingPokemonList() {
        UserRequest request = new UserRequest(
                "misty",
                "misty@pueblopaleta.pa",
                "water123",
                null
        );

        assertBadRequest(request);
        Mockito.verifyNoInteractions(pokemonClient);
    }

    @Test
    void testCreateUser_ValidationError_InvalidPokemonID() {
        UserRequest request = new UserRequest(
                "misty",
                "misty@pueblopaleta.pa",
                "water123",
                List.of(-45)
        );

        assertBadRequest(request);
        Mockito.verify(pokemonClient, Mockito.times(1)).getPokemonName(-45);
    }

    @Test
    void testCreateUser_Conflict_UsernameAlreadyExists() {

        UserRequest request = new UserRequest(
                "ash", // same username
                "newemail@kanto.com",
                "newpass123",
                List.of(1)
        );

        // Act + Assert
        webTestClient.post()
                .uri("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT)
                .expectBody()
                .jsonPath("$.error").exists();

        // Pokémon API should NOT be called
        Mockito.verifyNoInteractions(pokemonClient);

        // DB should still contain only the original user
        List<User> users = userRepository.findAll();
        assertThat(users).hasSize(1);
        assertThat(users.get(0).getUsername()).isEqualTo("ash");
    }

    @Test
    void testCreateUser_Conflict_EmailAlreadyExists() {

        UserRequest request = new UserRequest(
                "misty",
                "ash@kanto.com", // same email
                "water123",
                List.of(1)
        );

        // Act + Assert
        webTestClient.post()
                .uri("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT)
                .expectBody()
                .jsonPath("$.error").exists();

        // Pokémon API should NOT be called
        Mockito.verifyNoInteractions(pokemonClient);

        // DB should still contain only the original user
        List<User> users = userRepository.findAll();
        assertThat(users).hasSize(1);
        assertThat(users.get(0).getEmail()).isEqualTo("ash@kanto.com");
    }


    @Test
    void testGetUser() {
        Mockito.doReturn("pikachu").when(pokemonClient).getPokemonName(25);
        Mockito.doReturn("bulbasaur").when(pokemonClient).getPokemonName(1);

        webTestClient.get()
                .uri("/api/users/" + savedUser.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.username").isEqualTo("ash")
                .jsonPath("$.pokemonNames").value(list -> {
                    assertThat(list)
                            .asInstanceOf(InstanceOfAssertFactories.list(String.class))
                            .containsExactlyInAnyOrder("pikachu", "bulbasaur");
                });

        // Verify the mock was actually used
        Mockito.verify(pokemonClient).getPokemonName(25);
        Mockito.verify(pokemonClient).getPokemonName(1);
        Mockito.verifyNoMoreInteractions(pokemonClient);
    }
}
