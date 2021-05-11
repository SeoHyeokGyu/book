package com.example.book.service;

import com.example.book.domain.Book;
import com.example.book.domain.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BookService {

    private final BookRepository bookRepository;

    @Transactional
    public Book Save(Book book){
        return bookRepository.save(book);
    }

    @Transactional(readOnly = true)
    public Book LoadOne(Long id){
        return bookRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("id check please") );
    }

    @Transactional(readOnly = true)
    public List<Book> LoadAll(){
        return bookRepository.findAll();
    }

    @Transactional
    public Book Update(Long id, Book book){
        Book bookEntity = bookRepository.findById(id)
                  .orElseThrow(()->new IllegalArgumentException("id check please") );
        ;
        bookEntity.setTitle(book.getTitle());
        bookEntity.setAuthor(book.getAuthor());
        return bookEntity;
    }

    @Transactional
    public String Delete(Long id){
        bookRepository.deleteById(id);
        return "OK";
    }
}
