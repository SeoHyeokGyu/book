package com.example.book.web;


import com.example.book.domain.Book;
import com.example.book.domain.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Slf4j
@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class BookControllerIntegreTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    public void init(){
        entityManager.createNativeQuery("ALTER TABLE book ALTER COLUMN id RESTART  WITH 1").executeUpdate();
    }

    @Test
    public void save_Test() throws Exception {

        Book book = new Book(null,"스프링따라하기","홍길동");
        String content =  new ObjectMapper().writeValueAsString(book);


        ResultActions resultAction = mockMvc.perform(post("/book")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .content(content));


        resultAction
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("스프링따라하기"))
                .andDo(MockMvcResultHandlers.print());

        log.info(content);
    }

    @Test
    public void findAll_test() throws Exception{

        List<Book> books = new ArrayList<>();
        books.add(new Book(1L,"스플이부트 따라하기","홍길동"));
        books.add(new Book(2L,"리액트 따라하기","김길동"));

        bookRepository.saveAll(books);

        ResultActions resultActions = mockMvc.perform(get("/books")
                .accept(MediaType.APPLICATION_JSON_UTF8));

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$.[0].title").value("스프링부트 따라하기"))
                .andDo(MockMvcResultHandlers.print());

    }
    @Test
    public void findById_test() throws Exception{

        List<Book> books = new ArrayList<>();
        books.add(new Book(1L,"스플이부트 따라하기","홍길동"));
        books.add(new Book(2L,"리액트 따라하기","김길동"));
        bookRepository.saveAll(books);

        Long id = 1L;

        ResultActions resultActions = mockMvc.perform(get("/book/{id}",id)
                .accept(MediaType.APPLICATION_JSON_UTF8));

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("자바공부하기"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void update_test() throws Exception{

        List<Book> books = new ArrayList<>();
        books.add(new Book(1L,"스플이부트 따라하기","홍길동"));
        books.add(new Book(2L,"리액트 따라하기","김길동"));
        bookRepository.saveAll(books);

        Long id = 1L;
        Book book = new Book(null,"C++따라하기","홍길동");
        String content =  new ObjectMapper().writeValueAsString(book);


        ResultActions resultActions = mockMvc.perform(put("/book/{id}",id)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .content(content));

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("C++따라하기"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void delete_test() throws Exception{

        List<Book> books = new ArrayList<>();
        books.add(new Book(1L,"스플이부트 따라하기","홍길동"));
        books.add(new Book(2L,"리액트 따라하기","김길동"));
        bookRepository.saveAll(books);

        Long id = 1L;

        ResultActions resultActions = mockMvc.perform(delete("/book/{id}",id)
                .accept(MediaType.TEXT_PLAIN));


        resultActions
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

        MvcResult mvcResult = resultActions.andReturn();
        String result = mvcResult.getResponse().getContentAsString();

        assertEquals("OK",result);

    }
}
