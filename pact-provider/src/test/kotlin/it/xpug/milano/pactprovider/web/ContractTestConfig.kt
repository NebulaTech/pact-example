package it.xpug.milano.pactprovider.web

import domain.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ContractTestConfig {
    @Bean
    fun findBooks(): FindBooks {
        return FakeFindBooks()
    }

    @Bean
    fun storeBook(): StoreBook {
        return FakeStoreBook()
    }
}

class FakeStoreBook : StoreBook {
    private var willFail = false

    override fun store(book: Book) {
        if (willFail) {
            throw BookStoreException()
        }
    }

    fun willFail() {
        this.willFail = true
    }

    fun reset() {
        this.willFail = false
    }
}

class FakeFindBooks : FindBooks {
    private var books = emptyList<Book>()

    override fun all(): List<Book> {
        return books
    }

    override fun byId(id: Int): Book {
        return books.firstOrNull { it -> it.id == id } ?: throw BookNotFoundException()
    }

    fun hasBooks(books: List<Book>) {
        this.books = books
    }

    fun reset() {
        this.books = emptyList()
    }
}