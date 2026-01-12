package ownkey.implement.user.component;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ownkey.implement.user.model.User;
import ownkey.infrastructure.user.jpa.UserRepository;

@Component
@RequiredArgsConstructor
public class UserWriter {

    private final UserRepository userRepository;

    public User save(User user) {
        return userRepository.save(user);
    }
}