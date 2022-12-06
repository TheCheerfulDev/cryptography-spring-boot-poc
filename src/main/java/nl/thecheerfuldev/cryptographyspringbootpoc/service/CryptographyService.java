package nl.thecheerfuldev.cryptographyspringbootpoc.service;

import nl.thecheerfuldev.cryptographyspringbootpoc.exception.NoEncryptionKeyException;
import nl.thecheerfuldev.cryptographyspringbootpoc.repository.CryptoKeyRepository;
import nl.thecheerfuldev.cryptographyspringbootpoc.repository.PasswordSaltPair;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CryptographyService {

    private static final String MASTER_PASSWORD_FROM_VAULT = "MasterPassword99!";
    private static final String MASTER_SALT_FROM_VAULT = "2d6fe59a14740061";

    private final TextEncryptor masterEncryptor;
    private final CryptoKeyRepository cryptoKeyRepository;

    public CryptographyService(CryptoKeyRepository cryptoKeyRepository) {
        this.cryptoKeyRepository = cryptoKeyRepository;
        this.masterEncryptor = createTextEncryptor(MASTER_PASSWORD_FROM_VAULT, MASTER_SALT_FROM_VAULT);
    }

    public void generateKeyPasswordPairForUserId(final int userId) {
        final String salt = this.encrypt(this.generateCryptoSalt(), this.masterEncryptor);
        final String password = this.encrypt(this.createPassword(), this.masterEncryptor);
        this.cryptoKeyRepository.setKeyPasswordPairForUserId(userId, salt, password);
    }

    public void deletePasswordSaltPairForUserId(final int userId) {
        this.cryptoKeyRepository.deleteForUser(userId);
    }

    private String createPassword() {
        return "pwd-" + UUID.randomUUID();
    }

    public String encryptTextForUserId(final String text, final int userId) {
        final PasswordSaltPair passwordSaltPair = fetchKeyPasswordPairForUserId(userId);
        final String decryptedSalt = this.decrypt(passwordSaltPair.getSalt(), this.masterEncryptor);
        final String decryptedPassword = this.decrypt(passwordSaltPair.getPassword(), this.masterEncryptor);
        return this.encrypt(text, createTextEncryptor(decryptedPassword, decryptedSalt));
    }

    public List<String> encryptTextForUserId(final List<String> messages, final int userId) {
        final PasswordSaltPair passwordSaltPair = fetchKeyPasswordPairForUserId(userId);
        final String decryptedSalt = this.decrypt(passwordSaltPair.getSalt(), this.masterEncryptor);
        final String decryptedPassword = this.decrypt(passwordSaltPair.getPassword(), this.masterEncryptor);
        final TextEncryptor textEncryptor = createTextEncryptor(decryptedPassword, decryptedSalt);
        return messages.stream().map(message -> this.encrypt(message, textEncryptor)).toList();
    }

    public String decryptTextForUserId(final String text, final int userId) {
        final PasswordSaltPair passwordSaltPair;
        try {
            passwordSaltPair = fetchKeyPasswordPairForUserId(userId);
        } catch (NoEncryptionKeyException e) {
            return "ENCRYPTED";
        }
        final String decryptedSalt = this.decrypt(passwordSaltPair.getSalt(), this.masterEncryptor);
        final String decryptedPassword = this.decrypt(passwordSaltPair.getPassword(), this.masterEncryptor);
        return this.decrypt(text, createTextEncryptor(decryptedPassword, decryptedSalt));
    }

    public List<String> decryptTextForUserId(final List<String> messages, final int userId) {
        final PasswordSaltPair passwordSaltPair;
        try {
            passwordSaltPair = fetchKeyPasswordPairForUserId(userId);
        } catch (NoEncryptionKeyException e) {
            return List.of("ENCRYPTED");
        }
        final String decryptedSalt = this.decrypt(passwordSaltPair.getSalt(), this.masterEncryptor);
        final String decryptedPassword = this.decrypt(passwordSaltPair.getPassword(), this.masterEncryptor);
        final TextEncryptor textEncryptor = createTextEncryptor(decryptedPassword, decryptedSalt);
        return messages.stream().map(message -> this.decrypt(message, textEncryptor)).toList();
    }

    private PasswordSaltPair fetchKeyPasswordPairForUserId(final int userId) {
        final Optional<PasswordSaltPair> passwordSaltPairOptional = this.cryptoKeyRepository.getKeyForUserId(userId);
        return passwordSaltPairOptional.orElseThrow(() -> new NoEncryptionKeyException(userId));
    }

    private String generateCryptoSalt() {
        return KeyGenerators.string().generateKey();
    }

    private String encrypt(final String text, final TextEncryptor encryptor) {
        return encryptor.encrypt(text);
    }

    private String decrypt(final String text, final TextEncryptor encryptor) {
        return encryptor.decrypt(text);
    }

    private TextEncryptor createTextEncryptor(final String password, final String salt) {
        return Encryptors.delux(password, salt);
    }

}
