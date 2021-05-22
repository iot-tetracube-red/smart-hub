package red.tetracube.smarthub

import io.quarkus.runtime.Quarkus
import io.quarkus.runtime.QuarkusApplication
import io.quarkus.runtime.annotations.QuarkusMain
import org.slf4j.LoggerFactory
import red.tetracube.smarthub.iot.SmartBrokerHub

@QuarkusMain
class SmartHubMain {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Quarkus.run(SmartHubApp::class.java, *args)
        }
    }

    class SmartHubApp(private val smartBrokerHub: SmartBrokerHub) : QuarkusApplication {

        private val logger = LoggerFactory.getLogger(SmartHubApp::class.java)

        @Throws(Exception::class)
        override fun run(vararg args: String): Int {
            logger.info("Smart Hub is started, notifying components")
            smartBrokerHub.setupMqttConnection()
            Quarkus.waitForExit()
            return 0
        }
    }
}