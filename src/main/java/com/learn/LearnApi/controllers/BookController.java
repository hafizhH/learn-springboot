package com.learn.LearnApi.controllers;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.learn.LearnApi.entities.Book;
import com.learn.LearnApi.models.ApiException;
import com.learn.LearnApi.models.Response;
import com.learn.LearnApi.services.BookService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class BookController {

  @Autowired
  public BookService bookService;

  @PostMapping(path = "/books")
  public ResponseEntity<Response<Object>> addBook(@RequestBody @JsonView(Book.RequestView.class) Book request) throws Exception {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json; charset=utf-8");
    
    ObjectNode jsonData;
    try {
      int id = bookService.addBook(request);
      jsonData = (ObjectNode) new ObjectMapper().readTree("{\"bookId\":"+id+"}");
    } catch (Exception e) {
      throw e;
    }
    return ResponseEntity.status(HttpStatus.CREATED).headers(headers).body(Response.builder().status("success").message("Buku berhasil ditambahkan").data(jsonData).build());
    // return Response.builder().status("success").message("Buku berhasil ditambahkan").data(jsonData).build();
  }

  @GetMapping(path = "/books")
  public ResponseEntity<Response<Object>> getBooks(@RequestParam("name") Optional<String> name, @RequestParam("reading") Optional<String> reading, @RequestParam("finished") Optional<String> finished) throws Exception {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json; charset=utf-8");
    
    ObjectNode jsonData;
    try {
      Book[] books = bookService.getBooks(name, reading, finished);
      ObjectMapper mapper = new ObjectMapper();
      String result = mapper.writeValueAsString(books);
      jsonData = (ObjectNode) new ObjectMapper().readTree("{\"books\":"+result+"}");
    } catch (Exception e) {
      throw e;
    }
    return ResponseEntity.status(HttpStatus.OK).headers(headers).body(Response.builder().status("success").data(jsonData).build());
  }

  @GetMapping(path = "/books/{bookId}")
  public ResponseEntity<Response<Object>> getBookDetails(@PathVariable("bookId") String bookId) throws Exception {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json; charset=utf-8");
    
    ObjectNode jsonData;
    try {
      Book book = bookService.getBookDetails(bookId);
      ObjectMapper mapper = new ObjectMapper();
      String result = mapper.writeValueAsString(book);
      jsonData = (ObjectNode) new ObjectMapper().readTree("{\"book\":"+result+"}");
    } catch (Exception e) {
      throw e;
    }
    return ResponseEntity.status(HttpStatus.OK).headers(headers).body(Response.builder().status("success").data(jsonData).build());
  }

  @PutMapping(path = "/books/{bookId}")
  public ResponseEntity<Response<Object>> updateBookDetails(@PathVariable("bookId") String bookId, @RequestBody @JsonView(Book.RequestView.class) Book bookRequest) throws Exception {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json;charset=utf-8");
    try {
      bookService.updateBookDetails(bookId, bookRequest);
    } catch (Exception e) {
      throw e;
    }
    return ResponseEntity.status(HttpStatus.OK).headers(headers).body(Response.builder().status("success").message("Buku berhasil diperbarui").build());
  }

  @DeleteMapping(path = "/books/{bookId}")
  public ResponseEntity<Response<Object>> deleteBook(@PathVariable("bookId") String bookId) throws Exception {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json; charset=utf-8");
    try {
      bookService.deleteBook(bookId);
    } catch (Exception e) {
      throw e;
    }
    return ResponseEntity.status(HttpStatus.OK).headers(headers).body(Response.builder().status("success").message("Buku berhasil dihapus").build());
  }

  @ExceptionHandler
  public ResponseEntity<Response<Object>> handleException(HttpServletRequest request, Exception e) {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json;charset=utf-8");
    if (e instanceof ApiException) {
      ApiException e2 = (ApiException) e;
      return ResponseEntity.status(e2.getStatus()).headers(headers).body(Response.builder().status("fail").message(e2.getMessage()).build());
    } else {
      StringWriter sw = new StringWriter();
      PrintWriter pw = new PrintWriter(sw);
      e.printStackTrace(pw);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(headers).body(Response.builder().status("fail").message(e.getMessage()).error(sw.toString()).build());
    }
  }
}
