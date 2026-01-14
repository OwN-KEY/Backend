package ownkey.user.implement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ownkey.user.domain.User;
import ownkey.user.infrastructure.jpa.UserRepository;

@Component
@RequiredArgsConstructor
public class UserWriter {

    private final UserRepository userRepository;

    public User save(User user) {
        return userRepository.save(user);
    }
}