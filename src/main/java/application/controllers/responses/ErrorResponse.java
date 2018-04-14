package application.controllers.responses;

import application.controllers.messages.Message;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class ErrorResponse {
    private final String error;

    public ErrorResponse(@NotNull Message message) {
        this.error = message.getMessage();
    }

    @JsonProperty("error")
    public String getMessage() {
        return error;
    }
}
