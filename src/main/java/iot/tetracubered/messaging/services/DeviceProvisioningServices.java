package iot.tetracubered.messaging.services;

import io.quarkus.vertx.ConsumeEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.json.JsonObject;

@ApplicationScoped
public class DeviceProvisioningServices {

    @ConsumeEvent("device-provisioning")
    public void deviceProvisioning(byte[] jsonObject) {
        var tmp = jsonObject;
    }
}
