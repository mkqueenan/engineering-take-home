package com.kevinqueenan.sentencesearch.controller;

import com.kevinqueenan.sentencesearch.model.QueryResponse;
import com.kevinqueenan.sentencesearch.service.IndexTraverser;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
public class QueryController {

  private final IndexTraverser indexTraverser;

  public QueryController(final IndexTraverser indexTraverser) {
    this.indexTraverser = indexTraverser;
  }

  @RequestMapping(value = "/count", method = RequestMethod.GET)
  @ResponseBody
  public QueryResponse getSearchTermFrequency(@RequestParam(name = "word") String searchTerm)
      throws ParseException {
    if (searchTerm == null || searchTerm.isEmpty() || searchTerm.isBlank()) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Search term cannot be null, empty, or blank. Please provide a valid string in your request.");
    } else {
      return new QueryResponse(searchTerm, this.indexTraverser.getSearchTermFrequency(searchTerm));
    }
  }
}
