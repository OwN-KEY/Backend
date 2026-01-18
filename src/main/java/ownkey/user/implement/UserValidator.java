package ownkey.user.implement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ownkey.user.domain.vo.Nickname;
import ownkey.common.exception.BusinessException;
import ownkey.common.exception.ErrorCode;

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