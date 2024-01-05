package com.learn.LearnApi.entities;

import java.util.List;
import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookData {
  private List<Book> bookList;

  public BookData() {
    bookList = new ArrayList<Book>();
  }
}
