package it.xpug.milano.pactprovider.web

import domain.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
class BookController {
    @Autowired
    private lateinit var findBooks: FindBooks

    @Autowired
    private lateinit var storeBook: StoreBook

    @GetMapping("/books")
    fun listAll(): List<Book> {
        return findBooks.all()
    }

    @GetMapping("/books/{id}")
    fun getById(@PathVariable id: Int): Book {
        return findBooks.byId(id)
    }

    @PostMapping("/books")
    @ResponseStatus(HttpStatus.CREATED)
    fun store(@RequestBody book: Book) {
        storeBook.store(book)
    }

    @ExceptionHandler(BookNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun bookNotFound() {}

    @ExceptionHandler(BookStoreException::class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    fun storeException() {}
}