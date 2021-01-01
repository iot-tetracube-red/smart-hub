package iot.tetracubered.smarthub

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SmartHubApplication

fun main(args: Array<String>) {
	runApplication<SmartHubApplication>(*args)
}
