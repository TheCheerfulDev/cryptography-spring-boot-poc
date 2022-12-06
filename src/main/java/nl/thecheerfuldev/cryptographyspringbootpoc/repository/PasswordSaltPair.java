package nl.thecheerfuldev.cryptographyspringbootpoc.repository;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PasswordSaltPair {

    private final String salt;
    private final String password;

    private PasswordSaltPair(String salt, String password) {
        this.salt = salt;
        this.password = password;
    }
}
