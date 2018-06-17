package it.xpug.milano.pactprovider.web

import domain.Book
import domain.FindBooks
import domain.StoreBook
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class UseCaseConfiguration {
    @Bean
    fun findBooks(): FindBooks {
        return object: FindBooks {
            override fun all(): List<Book> {
                TODO("not implemented")
            }

            override fun byId(id: Int): Book {
                TODO("not implemented")
            }

        }
    }

    @Bean
    fun storeBook(): StoreBook {
        return object : StoreBook {
            override fun store(book: Book) {
                TODO("not implemented")
            }

        }
    }
}