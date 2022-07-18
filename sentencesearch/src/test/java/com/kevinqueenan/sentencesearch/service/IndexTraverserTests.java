package com.kevinqueenan.sentencesearch.service;

import com.kevinqueenan.sentencesearch.model.VectorTextField;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.kevinqueenan.sentencesearch.utility.StringSanitizer.convertToLowerAndRemovePunctuationFromText;
import static com.kevinqueenan.sentencesearch.utility.StringSanitizer.normalizeText;
import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class IndexTraverserTests {

  @Value("${max.hits.per.query}")
  Integer maxHitsPerQuery;

  @Value("${query.field.name}")
  String queryFieldName;

  @Value("${result.field.name}")
  String resultFieldName;

  String inputText =
      "Hello, this is text that will be indexed. Test, test, test, 1, 2, 3. Thank you!";

  Integer termFrequency = 1;

  String indexDirectory = "src/test/resources/index";

  Map<String, Integer> searchTermResults;

  IndexTraverser indexTraverser;

  @BeforeAll
  void setUp() {

    this.createIndexForTraversal();

    this.populateSearchTermResultsMap();

    this.indexTraverser =
        new IndexTraverser(
            indexDirectory, this.maxHitsPerQuery, this.queryFieldName, this.resultFieldName);
  }

  @AfterAll
  void tearDown() {
    this.deleteIndex();
  }

  @Test
  @DisplayName(
      "Traversing the index to retrieve the term frequency for \"text\" yields a matching map")
  void search_term_frequency_result_map_is_matching() throws ParseException {
    assertThat(this.indexTraverser.getSearchTermFrequency("text"))
        .usingRecursiveComparison()
        .isEqualTo(this.searchTermResults);
  }

  @Test
  @DisplayName(
      "Traversing the index to retrieve the term frequency for \"test\" yields a non-matching map")
  void search_term_frequency_result_map_is_not_matching() throws ParseException {
    assertThat(this.indexTraverser.getSearchTermFrequency("test"))
        .usingRecursiveComparison()
        .isNotEqualTo(this.searchTermResults);
  }

  @Test
  @DisplayName(
      "Traversing the index to retrieve the term frequency for \"text\" yields a frequency value of 1")
  void search_term_frequency_text_result_frequency_is_matching() throws ParseException {
    assertThat(this.indexTraverser.getSearchTermFrequency("text").get(this.inputText)).isEqualTo(1);
  }

  @Test
  @DisplayName(
      "Traversing the index to retrieve the term frequency for \"test\" yields a frequency value of 3")
  void search_term_frequency_test_result_frequency_is_matching() throws ParseException {
    assertThat(this.indexTraverser.getSearchTermFrequency("test").get(this.inputText)).isEqualTo(3);
  }

  void createIndexForTraversal() {
    StandardAnalyzer standardAnalyzer = new StandardAnalyzer();
    IndexWriterConfig indexWriterConfig = new IndexWriterConfig(standardAnalyzer);
    indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

    try (Directory index = FSDirectory.open(Path.of(this.indexDirectory));
        IndexWriter indexWriter = new IndexWriter(index, indexWriterConfig)) {

      String inputTextWithoutPunctuation =
          convertToLowerAndRemovePunctuationFromText(this.inputText);
      String normalizedInputText = normalizeText(inputTextWithoutPunctuation);

      Document document = new Document();
      document.add(
          new VectorTextField(this.queryFieldName, normalizedInputText, TextField.TYPE_NOT_STORED));
      document.add(new Field(this.resultFieldName, this.inputText, StoredField.TYPE));

      indexWriter.addDocument(document);

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  void populateSearchTermResultsMap() {
    this.searchTermResults = new LinkedHashMap<>();
    this.searchTermResults.put(this.inputText, this.termFrequency);
  }

  void deleteIndex() {

    StandardAnalyzer standardAnalyzer = new StandardAnalyzer();
    IndexWriterConfig indexWriterConfig = new IndexWriterConfig(standardAnalyzer);
    indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

    try (Directory index = FSDirectory.open(Path.of(this.indexDirectory));
        IndexWriter indexWriter = new IndexWriter(index, indexWriterConfig)) {
      indexWriter.deleteAll();

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
