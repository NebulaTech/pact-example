package it.xpug.milano.pactprovider.web

import au.com.dius.pact.provider.junit.Provider
import au.com.dius.pact.provider.junit.State
import au.com.dius.pact.provider.junit.loader.PactBroker
import au.com.dius.pact.provider.junit.target.HttpTarget
import au.com.dius.pact.provider.junit.target.TestTarget
import au.com.dius.pact.provider.spring.SpringRestPactRunner
import domain.Book
import org.junit.Before
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import javax.annotation.Resource

@RunWith(SpringRestPactRunner::class)
@Provider("Provider Example")
@PactBroker(host = "xpug-pact-broker.herokuapp.com", protocol = "https", port = "443")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        classes = [(PactProviderApplication::class), (ContractTestConfig::class)])
@TestPropertySource(properties = ["server.port=9192"])
class BookServiceContractTest {
    @JvmField
    @TestTarget
    final val target = HttpTarget(9192)

    @Resource
    private lateinit var storeBook: FakeStoreBook

    @Resource
    private lateinit var findBooks: FakeFindBooks

    @Before
    fun setUp() {
        storeBook.reset()
        findBooks.reset()
    }

    @State("there are books")
    fun thereAreBooks() {
        findBooks.hasBooks(listOf(
                Book(1, "title", "description"),
                Book(2, "another title", "another description")
        ))
    }

    @State("there are no books")
    fun thereAreNoBooks() {
        findBooks.hasBooks(emptyList())
    }

    @State("there is a book with id 10")
    fun thereIsABookWithId10() {
        findBooks.hasBooks(listOf(
                Book(10, "title", "description")
        ))
    }

    @State("there is no book with id 10")
    fun thereIsNoBookWithId10() {
        findBooks.hasBooks(emptyList())
    }

    @State("there is already a book with id 10")
    fun thereIsAlreadyABookWithId10() {
        storeBook.willFail()
    }

    @State("there is no book stored with id 10")
    fun thereIsNoBookStoredWithId10() {
    }
}