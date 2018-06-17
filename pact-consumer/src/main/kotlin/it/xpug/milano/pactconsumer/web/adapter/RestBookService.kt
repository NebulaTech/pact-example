package it.xpug.milano.pactconsumer.web.adapter

import it.xpug.milano.pactconsumer.domain.model.Book
import it.xpug.milano.pactconsumer.domain.port.BookService
import it.xpug.milano.pactconsumer.domain.port.BookStoreException
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate

class RestBookService(private val baseUrl: String) : BookService {
    private val restTemplate = RestTemplate()

    override fun listAll(): List<Book> {
        val responseEntity = restTemplate.exchange(
                "$baseUrl/books",
                HttpMethod.GET,
                null,
                object : ParameterizedTypeReference<List<Book>>() {}
        )
        return responseEntity.body!!
    }

    override fun getById(id: Int): Book? {
        try {
            val responseEntity = restTemplate.getForEntity(
                    "$baseUrl/books/{id}",
                    Book::class.java,
                    id)

            return responseEntity.body!!
        } catch (e: HttpClientErrorException) {
            if (e.statusCode == HttpStatus.NOT_FOUND) {
                return null
            }
            throw e
        }

    }

    override fun store(book: Book) {
        try {
            restTemplate.postForEntity("$baseUrl/books", book, Unit::class.java)
        } catch (e: HttpClientErrorException) {
            if (e.statusCode == HttpStatus.UNPROCESSABLE_ENTITY) {
                throw BookStoreException()
            }
            throw e
        }
    }
}