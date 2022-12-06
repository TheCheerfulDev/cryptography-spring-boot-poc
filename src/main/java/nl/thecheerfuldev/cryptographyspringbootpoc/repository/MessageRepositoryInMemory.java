package nl.thecheerfuldev.cryptographyspringbootpoc.repository;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class MessageRepositoryInMemory implements MessageRepository {

    private final Map<Integer, List<String>> messages = new HashMap<>();

    @Override
    public void putMessageForUserId(final int userId, final String message) {
        if (!messages.containsKey(userId)) {
            messages.put(userId, new ArrayList<>());
        }
        messages.get(userId).add(message);
    }

    @Override
    public List<String> getMessagesForUserId(final int userId) {
        if (!messages.containsKey(userId)) {
            return List.of();
        }
        return List.copyOf(messages.get(userId));
    }

}
