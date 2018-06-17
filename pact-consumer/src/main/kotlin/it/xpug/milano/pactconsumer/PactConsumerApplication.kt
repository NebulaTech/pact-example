package it.xpug.milano.pactconsumer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PactConsumerApplication

fun main(args: Array<String>) {
    runApplication<PactConsumerApplication>(*args)
}
