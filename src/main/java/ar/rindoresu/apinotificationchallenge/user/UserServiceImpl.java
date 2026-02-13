package ar.rindoresu.apinotificationchallenge.user;

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

    public UserServiceImpl(UserRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserResponse create(UserRequest req) {
        User user = new User(req.getEmail(), req.getUsername(), req.getPassword());
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

        return toResponse(repo.save(user));
    }

    @Override
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        repo.deleteById(id);
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getPassword());
    }
}
