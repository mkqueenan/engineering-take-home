package com.kevinqueenan.sentencesearch.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SearchTermHit {

  @JsonProperty(value = "sentence")
  private String sentence;

  @JsonProperty(value = "count")
  private Integer frequency;

  public SearchTermHit(String sentence, Integer frequency) {
    this.sentence = sentence;
    this.frequency = frequency;
  }
}
