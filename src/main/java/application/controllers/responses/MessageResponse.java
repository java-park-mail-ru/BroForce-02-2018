package application.controllers.responses;

import application.controllers.messages.Message;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class MessageResponse {
    private final String message;

    public MessageResponse(@NotNull Message message) {
        this.message = message.getMessage();
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }
}

