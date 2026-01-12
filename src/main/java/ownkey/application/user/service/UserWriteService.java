package ownkey.application.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ownkey.application.user.dto.UserCommand;
import ownkey.application.user.usecase.UpdateProfileUseCase;
import ownkey.application.user.usecase.WithdrawUseCase;
import ownkey.implement.auth.component.SocialAccountManager;
import ownkey.implement.auth.component.TokenWriter;
import ownkey.implement.user.component.UserReader;
import ownkey.implement.user.component.UserValidator;
import ownkey.implement.user.model.User;
import ownkey.implement.user.model.vo.Nickname;

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
    public void updateProfile(Long userId, UserCommand.UpdateProfile command) {
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