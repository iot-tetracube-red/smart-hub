package iot.tetracubered.resources.command.payloads;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PlayActionResponse {

    @JsonProperty("command_success")
    private final Boolean commandSuccess;

    @JsonCreator
    public PlayActionResponse(Boolean commandSuccess) {
        this.commandSuccess = commandSuccess;
    }

    public Boolean getCommandSuccess() {
        return commandSuccess;
    }
}
