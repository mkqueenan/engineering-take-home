package com.kevinqueenan.sentencesearch.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class QueryResponse {

  @JsonProperty(value = "word")
  private String searchTerm;

  @JsonProperty(value = "sentences")
  private List<SearchTermHit> searchTermHits;

  public QueryResponse(String searchTerm, Map<String, Integer> searchTermHits) {
    this.searchTerm = searchTerm;
    this.searchTermHits =
        searchTermHits.entrySet().stream()
            .map(entry -> new SearchTermHit(entry.getKey(), entry.getValue()))
            .collect(Collectors.toList());
  }
}
