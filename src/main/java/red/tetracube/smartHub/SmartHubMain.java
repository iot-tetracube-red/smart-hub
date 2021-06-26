package red.tetracube.smartHub;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@QuarkusMain
public class SmartHubMain {

    public static void main(String... args) {
        Quarkus.run(SmartHubApp.class, args);
    }

    public static class SmartHubApp implements QuarkusApplication {

        private static final Logger LOGGER = LoggerFactory.getLogger(SmartHubApp.class);

        @Override
        public int run(String... args) {
            LOGGER.info("Smart Hub is started, notifying components");
            Quarkus.waitForExit();
            return 0;
        }
    }
}
