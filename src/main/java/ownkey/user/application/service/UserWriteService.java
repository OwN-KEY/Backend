package ownkey.user.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ownkey.user.application.command.UpdateProfileCommand;
import ownkey.user.application.usecase.UpdateProfileUseCase;
import ownkey.user.application.usecase.WithdrawUseCase;
import ownkey.auth.implement.SocialAccountManager;
import ownkey.auth.implement.TokenWriter;
import ownkey.user.implement.UserReader;
import ownkey.user.implement.UserValidator;
import ownkey.user.domain.User;
import ownkey.user.domain.vo.Nickname;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserWriteService implements UpdateProfileUseCase, WithdrawUseCase {

    private final UserReader userReader;
    private final UserValidator userValidator;
    private final SocialAccountManager socialAccountManager;
    private final TokenWriter tokenWriter;

    @Override
    public void updateProfile(Long userId, UpdateProfileCommand command) {
        User user = userReader.get(userId);

        Nickname newNickname = null;
        if (command.nickname() != null) {
            newNickname = new Nickname(command.nickname());
            if (!newNickname.equals(user.getNickname())) {
                userValidator.validateNicknameDuplication(newNickname);
            }
        }

        user.updateProfile(newNickname, command.profileImageId(), command.backgroundImageId());
    }

    @Override
    public void withdraw(Long userId) {
        User user = userReader.get(userId);

        socialAccountManager.unlink(user.getKakaoId());
        tokenWriter.deleteByUserId(userId);

        user.withdraw();
    }
}