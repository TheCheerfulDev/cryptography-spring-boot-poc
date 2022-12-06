package nl.thecheerfuldev.cryptographyspringbootpoc.repository;

import java.util.Optional;

public interface CryptoKeyRepository {
    Optional<PasswordSaltPair> getKeyForUserId(int userId);

    void setKeyPasswordPairForUserId(int userId, String key, String password);

    void deleteForUser(int userId);
}
