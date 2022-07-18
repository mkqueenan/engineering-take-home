package com.kevinqueenan.sentencesearch.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Service
public class IndexTraverser {

  private final String indexDirectory;

  private final Integer maxHitsPerQuery;

  private final String queryFieldName;

  private final String resultFieldName;

  public IndexTraverser(
      @Value("${index.directory}") final String indexDirectory,
      @Value("${max.hits.per.query}") final Integer maxHitsPerQuery,
      @Value("${query.field.name}") final String queryFieldName,
      @Value("${result.field.name}") final String resultFieldName) {

    this.indexDirectory = indexDirectory;
    this.maxHitsPerQuery = maxHitsPerQuery;
    this.queryFieldName = queryFieldName;
    this.resultFieldName = resultFieldName;
  }

  public Map<String, Integer> getSearchTermFrequency(final String searchTerm)
      throws ParseException {

    Map<String, Integer> searchTermResults = new LinkedHashMap<>();

    StandardAnalyzer analyzer = new StandardAnalyzer();
    Query query = new QueryParser(this.queryFieldName, analyzer).parse(searchTerm);

    try (Directory index = FSDirectory.open(Path.of(this.indexDirectory));
        IndexReader indexReader = DirectoryReader.open(index)) {

      IndexSearcher indexSearcher = new IndexSearcher(indexReader);
      TopDocs topDocs = indexSearcher.search(query, this.maxHitsPerQuery);
      ScoreDoc[] documentHits = topDocs.scoreDocs;

      for (int i = 0; i < documentHits.length; ++i) {

        int documentId = documentHits[i].doc;
        Document document = indexSearcher.doc(documentId);

        Terms terms = indexReader.getTermVector(documentId, this.queryFieldName);
        TermsEnum termsEnum = terms.iterator();

        while (termsEnum.next() != null) {

          String currentTerm = termsEnum.term().utf8ToString();
          if (currentTerm.equals(searchTerm)) {

            searchTermResults.put(
                document.get(this.resultFieldName), Math.toIntExact(termsEnum.totalTermFreq()));
            break;
          }
        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return searchTermResults;
  }
}
