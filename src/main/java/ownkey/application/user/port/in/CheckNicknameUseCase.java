package ownkey.application.user.port.in;

public interface CheckNicknameUseCase {
    boolean execute(String nickname);
}