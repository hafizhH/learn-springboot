package com.learn.LearnApi.services;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonView;
import com.learn.LearnApi.entities.Book;
import com.learn.LearnApi.entities.BookData;
import com.learn.LearnApi.models.ApiException;

@Service
public class BookService {
  
  @Autowired
  ApplicationContext context;

  public int addBook(@JsonView(Book.RequestView.class) Book bookRequest) throws ApiException {
    if (bookRequest.getName() == null) {
      throw new ApiException(HttpStatus.BAD_REQUEST, "Gagal menambahkan buku. Mohon isi nama buku");
    }
    if (bookRequest.getReadPage() > bookRequest.getPageCount()) {
      throw new ApiException(HttpStatus.BAD_REQUEST, "Gagal menambahkan buku. readPage tidak boleh lebih besar dari pageCount");
    }

    BookData bookData = context.getBean("books", BookData.class);
    int id = bookData.getBookList().size();
    bookRequest.setId(id);
    bookRequest.setFinished(bookRequest.getReadPage() == bookRequest.getPageCount());
    Long time = Instant.now().toEpochMilli();
    bookRequest.setInsertedAt(time);
    bookRequest.setUpdatedAt(time);
    bookData.getBookList().add(bookRequest);
    return id;
  }

  public Book[] getBooks(Optional<String> name, Optional<String> reading, Optional<String> finished) {
    BookData bookData = context.getBean("books", BookData.class);
    List<Book> mappedBooks = new ArrayList<Book>();
    for (Book book : bookData.getBookList()) {
      if ((!name.isPresent() || book.getName().toLowerCase().contains(name.get().toLowerCase())) && (!reading.isPresent() || (reading.get().contains("1")) == book.getReading()) && (!finished.isPresent() || (finished.get().contains("1")) == book.getFinished())) {
        mappedBooks.add(Book.builder().id(book.getId()).name(book.getName()).publisher(book.getPublisher()).build());
      }
    }
    return mappedBooks.toArray(new Book[0]);
  }

  public Book getBookDetails(String bookId) throws ApiException {
    int id;
    try {
      id = Integer.parseInt(bookId);
    } catch (Exception e) {
      throw new ApiException(HttpStatus.NOT_FOUND, "Buku tidak ditemukan");
    }
    BookData bookData = (BookData) context.getBean("books");
    Iterator<Book> it = bookData.getBookList().iterator();
    Book selectedBook = null;
    while (it.hasNext()) {
      selectedBook = it.next();
      if (selectedBook.getId() == id)
        break;
    }
    if (selectedBook == null) {
      throw new ApiException(HttpStatus.NOT_FOUND, "Buku tidak ditemukan");
    }
    return selectedBook;
  }

  public void updateBookDetails(String bookId, @JsonView(Book.RequestView.class) Book bookRequest) throws ApiException {
    int id;
    try {
      id = Integer.parseInt(bookId);
    } catch (Exception e) {
      throw new ApiException(HttpStatus.NOT_FOUND, "Gagal memperbarui buku. Id tidak ditemukan");
    }
    if (bookRequest.getName() == null) {
      throw new ApiException(HttpStatus.BAD_REQUEST, "Gagal memperbarui buku. Mohon isi nama buku");
    }
    if (bookRequest.getReadPage() > bookRequest.getPageCount()) {
      throw new ApiException(HttpStatus.BAD_REQUEST, "Gagal memperbarui buku. readPage tidak boleh lebih besar dari pageCount");
    }

    BookData bookData = context.getBean("books", BookData.class);
    int selectedIndex = -1;
    for (int i = 0; i < bookData.getBookList().size(); i++) {
      if (bookData.getBookList().get(i).getId() == id) {
        selectedIndex = i;
        break;
      }
    }
    if (selectedIndex == -1) {
      throw new ApiException(HttpStatus.NOT_FOUND, "Gagal memperbarui buku. Id tidak ditemukan");
    }
    bookRequest.setId(id);
    bookRequest.setInsertedAt(bookData.getBookList().get(selectedIndex).getInsertedAt());
    bookRequest.setFinished(bookRequest.getReadPage() == bookRequest.getPageCount());
    bookRequest.setUpdatedAt(Instant.now().toEpochMilli());
    bookData.getBookList().set(selectedIndex, bookRequest);
    return;
  }

  public void deleteBook(String bookId) throws ApiException {
    int id;
    try {
      id = Integer.parseInt(bookId);
    } catch (Exception e) {
      throw new ApiException(HttpStatus.NOT_FOUND, "Buku gagal dihapus. Id tidak ditemukan");
    }
    BookData bookData = context.getBean("books", BookData.class);
    int selectedIndex = -1;
    for (int i = 0; i < bookData.getBookList().size(); i++) {
      if (bookData.getBookList().get(i).getId() == id) {
        selectedIndex = i;
        break;
      }
    }
    if (selectedIndex == -1) {
      throw new ApiException(HttpStatus.NOT_FOUND, "Gagal memperbarui buku. Id tidak ditemukan");
    }
    bookData.getBookList().remove(selectedIndex);
  }
}
