package nl.thecheerfuldev.cryptographyspringbootpoc.exception;

public class KeyPasswordPairAlreadyExistsException extends RuntimeException {

    public KeyPasswordPairAlreadyExistsException(int userId) {
        super("KeyPasswordPair already exists for userId -> " + userId);
    }

}
