package ar.rindoresu.apinotificationchallenge.user;

import ar.rindoresu.apinotificationchallenge.user.dto.UserRequest;
import ar.rindoresu.apinotificationchallenge.user.dto.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse create(UserRequest req);
    List<UserResponse> findAll();
    UserResponse findById(Long id);
    UserResponse update(Long id, UserRequest req);
    void delete(Long id);
}
