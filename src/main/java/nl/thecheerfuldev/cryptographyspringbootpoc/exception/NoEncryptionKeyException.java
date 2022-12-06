package nl.thecheerfuldev.cryptographyspringbootpoc.exception;

public class NoEncryptionKeyException extends RuntimeException {

    public NoEncryptionKeyException(int userId) {
        super("No Encryption Key available for userId -> " + userId);
    }
}
