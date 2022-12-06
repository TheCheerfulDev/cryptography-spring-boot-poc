package nl.thecheerfuldev.cryptographyspringbootpoc.repository;

import java.util.List;

public interface MessageRepository {
    void putMessageForUserId(int userId, String message);

    List<String> getMessagesForUserId(int userId);
}
