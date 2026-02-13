package ar.rindoresu.apinotificationchallenge.pokemon.client;

import ar.rindoresu.apinotificationchallenge.pokemon.exception.PokemonNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class PokemonClient {

    private final RestTemplate restTemplate = new RestTemplate();

    public String getPokemonName(int id) {
        String url = "https://pokeapi.co/api/v2/pokemon/" + id;

        try {
            Map response = restTemplate.getForObject(url, Map.class);
            return (String) response.get("name");
        } catch (Exception e) {
            throw new PokemonNotFoundException(id);
        }
    }
}
