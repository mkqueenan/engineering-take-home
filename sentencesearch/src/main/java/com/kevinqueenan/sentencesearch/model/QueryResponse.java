package com.kevinqueenan.sentencesearch.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.Map;

@Builder
public class QueryResponse {

  @JsonProperty(value = "word")
  private String searchTerm;

  @JsonProperty(value = "sentences")
  private Map<String, Integer> searchTermFrequency;
}
