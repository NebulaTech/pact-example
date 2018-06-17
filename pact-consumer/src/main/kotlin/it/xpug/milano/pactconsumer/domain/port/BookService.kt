package it.xpug.milano.pactconsumer.domain.port

import it.xpug.milano.pactconsumer.domain.model.Book

interface BookService {
    fun listAll(): List<Book>
    fun getById(id: Int): Book?
    fun store(book: Book)
}

class BookStoreException : RuntimeException()