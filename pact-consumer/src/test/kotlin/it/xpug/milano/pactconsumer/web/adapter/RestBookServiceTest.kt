package it.xpug.milano.pactconsumer.web.adapter

import au.com.dius.pact.consumer.Pact
import au.com.dius.pact.consumer.PactProviderRuleMk2
import au.com.dius.pact.consumer.PactVerification
import au.com.dius.pact.consumer.dsl.PactDslJsonArray.arrayEachLike
import au.com.dius.pact.consumer.dsl.PactDslJsonBody
import au.com.dius.pact.consumer.dsl.PactDslWithProvider
import au.com.dius.pact.model.RequestResponsePact
import it.xpug.milano.pactconsumer.domain.model.Book
import it.xpug.milano.pactconsumer.domain.port.BookStoreException
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RestBookServiceTest {
    @Rule
    @JvmField
    var mockProvider = PactProviderRuleMk2("Provider Example", this)

    private lateinit var repository: RestBookService

    @Before
    fun setUp() {
        repository = RestBookService(mockProvider.url)
    }

    @Pact(consumer = "Consumer Example")
    fun existingBooks(builder: PactDslWithProvider): RequestResponsePact {
        return builder
                .given("there are books")
                .uponReceiving("a request to list all books")
                .path("/books")
                .method("GET")
                .willRespondWith()
                .status(200)
                .body(arrayEachLike()
                        .integerType("id", 10)
                        .stringType("title", "book 1")
                        .stringType("description", "description of book 1")
                        .closeObject())
                .toPact()
    }

    @Test
    @PactVerification(fragment = "existingBooks")
    fun foundBooks() {
        assertEquals(
                listOf(Book(10, "book 1", "description of book 1")),
                repository.listAll())
    }

    @Pact(consumer = "Consumer Example")
    fun noBooks(builder: PactDslWithProvider): RequestResponsePact {
        return builder
                .given("there are no books")
                .uponReceiving("a request to list all books")
                .path("/books")
                .method("GET")
                .willRespondWith()
                .status(200)
                .body(arrayEachLike(0))
                .toPact()
    }

    @Test
    @PactVerification(fragment = "noBooks")
    fun noBooksFound() {
        assertEquals(emptyList<Book>(), repository.listAll())
    }

    @Pact(consumer = "Consumer Example")
    fun existingBook10(builder: PactDslWithProvider): RequestResponsePact {
        return builder
                .given("there is a book with id 10")
                .uponReceiving("a request to get book with id 10")
                .path("/books/10")
                .method("GET")
                .willRespondWith()
                .status(200)
                .body(PactDslJsonBody()
                        .integerType("id", 10)
                        .stringType("title", "book 1")
                        .stringType("description", "description of book 1"))
                .toPact()
    }

    @Test
    @PactVerification(fragment = "existingBook10")
    fun foundBook() {
        assertEquals(Book(10, "book 1", "description of book 1"), repository.getById(10))
    }

    @Pact(consumer = "Consumer Example")
    fun notExistingBook10(builder: PactDslWithProvider): RequestResponsePact {
        return builder
                .given("there is no book with id 10")
                .uponReceiving("a request to get book with id 10")
                .path("/books/10")
                .method("GET")
                .willRespondWith()
                .status(404)
                .toPact()
    }

    @Test
    @PactVerification(fragment = "notExistingBook10")
    fun bookNotFound() {
        assertNull(repository.getById(10))
    }

    @Pact(consumer = "Consumer Example")
    fun storeBook10(builder: PactDslWithProvider): RequestResponsePact {
        return builder
                .given("there is no book stored with id 10")
                .uponReceiving("a request to store book with id 10")
                .path("/books")
                .method("POST")
                .body(PactDslJsonBody()
                        .integerType("id", 10)
                        .stringType("title", "book 10")
                        .stringType("description", "description of book 10"))
                .willRespondWith()
                .status(201)
                .toPact()
    }

    @Test
    @PactVerification(fragment = "storeBook10")
    fun storeBook() {
        repository.store(Book(10, "book 10", "description of book 10"))
    }

    @Pact(consumer = "Consumer Example")
    fun tryStoreExistingBook10(builder: PactDslWithProvider): RequestResponsePact {
        return builder
                .given("there is already a book with id 10")
                .uponReceiving("a request to store book with id 10")
                .path("/books")
                .method("POST")
                .body(PactDslJsonBody()
                        .integerType("id", 10)
                        .stringType("title", "book 10")
                        .stringType("description", "description of book 10"))
                .willRespondWith()
                .status(422)
                .toPact()
    }

    @Test(expected = BookStoreException::class)
    @PactVerification(fragment = "tryStoreExistingBook10")
    fun tryStoreExistingBook() {
        repository.store(Book(10, "book 10", "description of book 10"))
    }
}