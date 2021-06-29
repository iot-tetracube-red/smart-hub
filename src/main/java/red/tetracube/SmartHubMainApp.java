package red.tetracube;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@QuarkusMain
public class SmartHubMainApp {

    public static class SmartHubApp implements QuarkusApplication {

        private static final Logger LOGGER = LoggerFactory.getLogger(SmartHubApp.class);

        @Override
        public int run(String... args) throws Exception {
            Quarkus.waitForExit();
            return 0;
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(SmartHubMainApp.class);

    public static void main(String... args) {
        LOGGER.info("Running Smart Hub Main app");
        Quarkus.run(args);
    }
}
