package red.tetracube.smartHub

import io.quarkus.runtime.Quarkus
import io.quarkus.runtime.QuarkusApplication
import io.quarkus.runtime.annotations.QuarkusMain
import org.slf4j.LoggerFactory

@QuarkusMain
class SmartHubMain {
    class SmartHubApp : QuarkusApplication {

        private val logger = LoggerFactory.getLogger(SmartHubApp::class.java)

        override fun run(vararg args: String?): Int {
            logger.info("Smart Hub is started, notifying components")
            Quarkus.waitForExit()
            return 0
        }
    }
}

fun main(args: Array<String>) {
    Quarkus.run(SmartHubMain.SmartHubApp::class.java, *args)
}