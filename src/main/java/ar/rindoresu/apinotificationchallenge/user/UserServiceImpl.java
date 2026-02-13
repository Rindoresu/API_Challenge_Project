package ar.rindoresu.apinotificationchallenge.user;

import ar.rindoresu.apinotificationchallenge.pokemon.client.PokemonClient;
import ar.rindoresu.apinotificationchallenge.user.dto.UserRequest;
import ar.rindoresu.apinotificationchallenge.user.dto.UserResponse;
import ar.rindoresu.apinotificationchallenge.user.exception.UserNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository repo;
    private final PokemonClient pokemonClient;

    public UserServiceImpl(UserRepository repo, PokemonClient pokemonClient) {
        this.repo = repo;
        this.pokemonClient = pokemonClient;
    }

    @Override
    public UserResponse create(UserRequest req) {
        User user = new User(req.getEmail(), req.getUsername(), req.getPassword(), req.getPokemonIds());
        return toResponse(repo.save(user));
    }

    @Override
    public List<UserResponse> findAll() {
        return repo.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public UserResponse findById(Long id) {
        return repo.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public UserResponse update(Long id, UserRequest req) {
        User user = repo.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        user.setUsername(req.getUsername());
        user.setEmail(req.getEmail());
        user.setPassword(req.getPassword());
        user.setPokemonIds(req.getPokemonIds());

        return toResponse(repo.save(user));
    }

    @Override
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        repo.deleteById(id);
    }

    public UserResponse toResponse(User user) {
        List<String> pokemonNames = user.getPokemonIds().stream()
                .map(pokemonClient::getPokemonName)
                .toList();
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                pokemonNames
        );
    }
}
