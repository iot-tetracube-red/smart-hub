package red.tetracube.smarthub.smartHubApplication;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import red.tetracube.smarthub.iot.SmartBrokerHub;

import javax.inject.Inject;

@QuarkusMain
public class SmartHubMain {

    public static void main(String... args) {
        Quarkus.run(SmartHubApp.class, args);
    }

    public static class SmartHubApp implements QuarkusApplication {

        @Inject
        SmartBrokerHub smartBrokerHub;

        private final static Logger LOGGER = LoggerFactory.getLogger(SmartHubApp.class);

        @Override
        public int run(String... args) throws Exception {
            LOGGER.info("Smart Hub is started, notifying components");
            smartBrokerHub.setupMqttConnection();
            Quarkus.waitForExit();
            return 0;
        }
    }
}
