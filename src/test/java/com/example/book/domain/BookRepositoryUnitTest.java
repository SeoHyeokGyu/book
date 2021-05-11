package com.example.book.domain;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@Transactional
@AutoConfigureTestDatabase(replace = Replace.ANY)
@DataJpaTest
public class BookRepositoryUnitTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    public void save_Test() throws Exception {

        Book book = new Book(null,"책제목","책저자");

        Book bookEntity =bookRepository.save(book);

        assertEquals("책제목",bookEntity.getTitle());

    }
}
