package nl.thecheerfuldev.cryptographyspringbootpoc.controller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Message {

    private String message;

    public Message(String message) {
        this.message = message;
    }

    public Message() {
    }
}
