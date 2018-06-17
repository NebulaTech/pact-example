package domain

interface FindBooks {
    fun all(): List<Book>
    fun byId(id: Int): Book
}

class BookNotFoundException : RuntimeException()