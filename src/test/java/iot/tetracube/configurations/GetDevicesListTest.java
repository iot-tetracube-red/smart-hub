package iot.tetracube.configurations;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import iot.tetracube.SmartHubDeviceEnvironment;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
@TestProfile(SmartHubDeviceEnvironment.class)
public class GetDevicesListTest {

    @Test
    public void testEmptyDeviceList() {
        given()
                .when().get("/devices")
                .then()
                .statusCode(200)
                .body("$.size()", equalTo(0));
    }

}
