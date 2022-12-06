package nl.thecheerfuldev.cryptographyspringbootpoc.repository;

import nl.thecheerfuldev.cryptographyspringbootpoc.exception.KeyPasswordPairAlreadyExistsException;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class CryptoKeyRepositoryInMemory implements CryptoKeyRepository {

    private final Map<Integer, PasswordSaltPair> keys = new HashMap<>();

    @Override
    public Optional<PasswordSaltPair> getKeyForUserId(int userId) {
        return Optional.ofNullable(keys.get(userId));
    }

    @Override
    public void setKeyPasswordPairForUserId(int userId, String key, String password) {
        if (keys.containsKey(userId)) {
            throw new KeyPasswordPairAlreadyExistsException(userId);
        }
        keys.put(userId, PasswordSaltPair.builder()
                .salt(key)
                .password(password)
                .build());
    }

    @Override
    public void deleteForUser(int userId) {
        keys.remove(userId);
    }

}
