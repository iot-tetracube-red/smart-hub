 package red.tetracube.smartHub.resources.security;

import io.quarkus.security.UnauthorizedException;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import red.tetracube.smartHub.resources.security.payloads.LoginDeviceRequest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@OpenAPIDefinition(
        info = @Info(title = "Security API", version = "1.0.0"),
        tags = {
                @Tag(name = "Device")
        }
)
@Path("/secure")
public class SecurityController {

    @Operation(summary = "Login device", description = "Login device that attempts to access to broker")
    @APIResponses(
            value = {
                    @APIResponse(responseCode = "200", description = "Device Logged in"),
                    @APIResponse(responseCode = "401", description = "Wrong credentials")
            }
    )

    @POST
    @Path("/device")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public void deviceLogin(LoginDeviceRequest loginDeviceRequest) {
        throw new UnauthorizedException("The user " + loginDeviceRequest.getClientId() + " cannot access to the broker");
    }
}
