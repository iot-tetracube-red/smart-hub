package red.tetracube.smartHub.resources.security.payloads;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;

public class LoginDeviceRequest {

    @Parameter(name = "clientId", required = true)
    @JsonProperty("clientId")
    private final String clientId;

    @Parameter(name = "username", required = true)
    @JsonProperty("username")
    private final String username;

    @Parameter(name = "password", required = true)
    @JsonProperty("password")
    private final String password;

    @JsonCreator
    public LoginDeviceRequest(@JsonProperty("clientId") String clientId,
                              @JsonProperty("username") String username,
                              @JsonProperty("password") String password) {
        this.clientId = clientId;
        this.username = username;
        this.password = password;
    }

    public String getClientId() {
        return clientId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
