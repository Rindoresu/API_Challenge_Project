package ar.rindoresu.apinotificationchallenge.user;

import ar.rindoresu.apinotificationchallenge.api.BaseIntegrationTest;
import ar.rindoresu.apinotificationchallenge.pokemon.client.PokemonClient;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;

class UserIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private UserRepository userRepository;

    @Spy
    private PokemonClient pokemonClient;

    @Test
    void testGetUser() {
        Mockito.doReturn("pikachu").when(pokemonClient).getPokemonName(25);

        webTestClient.get()
                .uri("/api/users/ash")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.username").isEqualTo("ash")
                .jsonPath("$.pokemonNames[0]").isEqualTo("pikachu");
    }
}