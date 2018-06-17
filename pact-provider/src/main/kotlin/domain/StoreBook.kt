package domain

interface StoreBook {
    fun store(book: Book)
}

class BookStoreException : RuntimeException()