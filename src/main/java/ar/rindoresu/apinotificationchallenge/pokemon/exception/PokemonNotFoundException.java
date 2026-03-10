package ar.rindoresu.apinotificationchallenge.pokemon.exception;

public class PokemonNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public PokemonNotFoundException(int id) {
        super("Pokémon with ID " + id + " not found");
    }
}
