package com.learn.LearnApi.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Book {
  public interface RequestView {};

  @JsonView(RequestView.class)
  private Integer id;

  @JsonView(RequestView.class)
  private String name;

  @JsonView(RequestView.class)
  private Integer year;

  @JsonView(RequestView.class)
  private String author;

  @JsonView(RequestView.class)
  private String summary;

  @JsonView(RequestView.class)
  private String publisher;

  @JsonView(RequestView.class)
  private Integer pageCount;

  @JsonView(RequestView.class)
  private Integer readPage;

  @JsonView(RequestView.class)
  private Boolean reading;

  private Boolean finished;
  private Long insertedAt;
  private Long updatedAt;
}
