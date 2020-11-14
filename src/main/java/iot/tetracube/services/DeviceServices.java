package iot.tetracube.services;

import io.quarkus.vertx.ConsumeEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.json.bind.Jsonb;
import java.util.Map;

@ApplicationScoped
public class DeviceServices {

    private final Jsonb jsonb;

    public DeviceServices(Jsonb jsonb) {
        this.jsonb = jsonb;
    }

    @ConsumeEvent("device-provisioning")
    public void manageDeviceProvisioningMessage(String message) {
        final var testObject = this.jsonb.fromJson(message, Map.class);
        final var e = testObject;
    }

}
