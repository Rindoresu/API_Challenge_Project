package ar.rindoresu.apinotificationchallenge.pokemon.exception;

public class PokemonNotFoundException extends RuntimeException {
    public PokemonNotFoundException(int id) {
        super("Pokémon with ID " + id + " not found");
    }
}
