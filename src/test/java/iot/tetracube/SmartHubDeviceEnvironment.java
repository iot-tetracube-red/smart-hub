package iot.tetracube;

import io.quarkus.test.junit.QuarkusTestProfile;

public class SmartHubDeviceEnvironment implements QuarkusTestProfile {

    @Override
    public String getConfigProfile() {
        return "test";
    }

}
