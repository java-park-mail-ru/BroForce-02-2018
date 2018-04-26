package application.controllers.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;

public class ChangeRequest {

    @NotNull
    private final String modifiedString;
    @NotNull
    private final String password;

    @JsonCreator
    public ChangeRequest(@JsonProperty("password") @NotNull String password,
                           @JsonProperty("change") @NotNull String fieldToChange) {
        this.password = password;
        this.modifiedString = fieldToChange;
    }

    @NotNull
    public String getPassword() {
        return password;
    }

    @NotNull
    public String getModifiedString() {
        return modifiedString;
    }
}
