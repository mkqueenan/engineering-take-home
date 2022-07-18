package com.kevinqueenan.sentencesearch.service;

import com.kevinqueenan.sentencesearch.model.VectorTextField;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

import static com.kevinqueenan.sentencesearch.utility.StringSanitizer.convertToLowerAndRemovePunctuationFromText;
import static com.kevinqueenan.sentencesearch.utility.StringSanitizer.normalizeText;
import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class SentenceIndexerTests {

  @Value("${query.field.name}")
  String queryFieldName;

  @Value("${result.field.name}")
  String resultFieldName;

  String inputDirectory = "src/test/resources/input";

  String indexDirectory = "src/test/resources/index";

  ArrayList<String> sentences;

  ArrayList<Document> documents;

  SentenceIndexer sentenceIndexer;

  @BeforeAll
  void setUp() {

    this.populateSentenceList();

    this.populateDocumentList();

    this.sentenceIndexer =
        new SentenceIndexer(
            this.inputDirectory,
            this.indexDirectory,
            true,
            this.queryFieldName,
            this.resultFieldName);
  }

  @AfterAll
  void tearDown() {
    this.deleteIndex();
  }

  @Test
  @DisplayName("Generating a list of documents from an input directory yields a matching list")
  void document_generation_from_file_is_matching_expected_result() {
    assertThat(this.sentenceIndexer.getDocuments())
        .usingRecursiveComparison()
        .isEqualTo(this.documents);
  }

  @Test
  @DisplayName(
      "Generating a list of documents from an input directory yields a match against the expected outcome")
  void document_generation_from_file_is_not_matching_modified_result() {
    Document temporaryRemoval = this.documents.remove(this.documents.size() - 1);
    assertThat(this.sentenceIndexer.getDocuments())
        .usingRecursiveComparison()
        .isNotEqualTo(this.documents);
    this.documents.add(temporaryRemoval);
  }

  void populateSentenceList() {

    this.sentences = new ArrayList<>();

    this.sentences.add("Eggplant or not eggplant?");
    this.sentences.add("Eggplant, eggplant, eggplant, eggplant, and some more eggplant.");
    this.sentences.add("Eggplant!");
  }

  void populateDocumentList() {

    this.documents = new ArrayList<>();

    this.sentences.forEach(
        sentence -> {
          String sentenceWithoutPunctuation = convertToLowerAndRemovePunctuationFromText(sentence);
          String normalizedSentence = normalizeText(sentenceWithoutPunctuation);

          Document document = new Document();
          document.add(
              new VectorTextField(
                  this.queryFieldName, normalizedSentence, TextField.TYPE_NOT_STORED));
          document.add(new Field(this.resultFieldName, sentence, StoredField.TYPE));

          this.documents.add(document);
        });
  }

  void deleteIndex() {

    StandardAnalyzer standardAnalyzer = new StandardAnalyzer();
    IndexWriterConfig indexWriterConfig = new IndexWriterConfig(standardAnalyzer);

    try (Directory index = FSDirectory.open(Path.of(this.indexDirectory));
        IndexWriter indexWriter = new IndexWriter(index, indexWriterConfig)) {
      indexWriter.deleteAll();

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
