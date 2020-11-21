package iot.tetracube.devices;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.vertx.mutiny.core.eventbus.EventBus;
import iot.tetracube.SmartHubDeviceEnvironment;
import iot.tetracube.models.dto.ManageDeviceProvisioningResponse;
import org.junit.jupiter.api.Test;

import java.util.UUID;

@QuarkusTest
@TestProfile(SmartHubDeviceEnvironment.class)
public class DeviceProvisioningTest {

    private final EventBus eventBus;

    public DeviceProvisioningTest(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Test
    public void testDeviceProvisioning() {
        var deviceProvisioningMessage = """
                {
                  "circuit_id": "09543601-23ee-4d3b-a3a5-527b3a6a8386",
                  "default_name": "Multimedia",
                  "actions": [
                    {
                      "id": "7e0d7f80-0eef-4d4f-b47c-ab886ca05126",
                      "name": "Turn on",
                      "type": "BUTTON"
                    },
                    {
                      "id": "457aad22-9659-4239-ad26-7f84472c16e7",
                      "name": "Turn off",
                      "type": "BUTTON"
                    }\s
                  ]
                }
                """;
        var returnedMessage = this.eventBus
                .<ManageDeviceProvisioningResponse>requestAndAwait("device-provisioning", deviceProvisioningMessage);
        assert (returnedMessage.isSend());
        assert (returnedMessage.body().getCircuitId().equals(UUID.fromString("09543601-23ee-4d3b-a3a5-527b3a6a8386")));
        assert (returnedMessage.body().getSuccess());
    }

}
