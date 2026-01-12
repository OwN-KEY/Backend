package ownkey.implement.user.component;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ownkey.implement.user.model.vo.Nickname;
import ownkey.presentation.common.exception.BusinessException;
import ownkey.presentation.common.exception.ErrorCode;

@Component
@RequiredArgsConstructor
public class UserValidator {

    private final UserReader userReader;

    public void validateNicknameDuplication(Nickname nickname) {
        if (userReader.existsNickname(nickname)) {
            throw new BusinessException(ErrorCode.DUPLICATE_NICKNAME);
        }
    }
}