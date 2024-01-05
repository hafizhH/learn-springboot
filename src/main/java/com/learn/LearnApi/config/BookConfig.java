package com.learn.LearnApi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.learn.LearnApi.entities.BookData;

@Configuration
public class BookConfig {
  @Bean
  public BookData books() {
    BookData books = new BookData();
    return books;
  }
}
