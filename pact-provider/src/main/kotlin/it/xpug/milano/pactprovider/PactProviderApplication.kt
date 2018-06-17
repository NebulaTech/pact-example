package it.xpug.milano.pactprovider

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PactProviderApplication

fun main(args: Array<String>) {
    runApplication<PactProviderApplication>(*args)
}
