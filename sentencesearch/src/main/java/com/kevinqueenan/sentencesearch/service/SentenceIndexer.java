package com.kevinqueenan.sentencesearch.service;

import com.kevinqueenan.sentencesearch.model.VectorTextField;
import com.kevinqueenan.sentencesearch.utility.TextFileFilter;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static com.kevinqueenan.sentencesearch.utility.StringSanitizer.convertToLowerAndRemovePunctuationFromText;
import static com.kevinqueenan.sentencesearch.utility.StringSanitizer.normalizeText;

@Slf4j
@Service
@Getter
public class SentenceIndexer {

  private final String inputDirectory;

  private final String indexDirectory;

  private final Boolean indexClearOnStart;

  private final String queryFieldName;

  private final String resultFieldName;

  private final ArrayList<Document> documents;

  public SentenceIndexer(
      @Value("${input.directory}") final String inputDirectory,
      @Value("${index.directory}") final String indexDirectory,
      @Value("${index.clear.on.start}") final Boolean indexClearOnStart,
      @Value("${query.field.name}") final String queryFieldName,
      @Value("${result.field.name}") final String resultFieldName) {
    this.inputDirectory = inputDirectory;
    this.indexDirectory = indexDirectory;
    this.indexClearOnStart = indexClearOnStart;
    this.queryFieldName = queryFieldName;
    this.resultFieldName = resultFieldName;
    this.documents = this.generateDocumentsFromSentences();
    this.createIndexFromDocuments(this.documents);
  }

  public ArrayList<Document> generateDocumentsFromSentences() {
    ArrayList<Document> documentsToBeIndexed = new ArrayList<>();
    File inputDirectory = new File(this.inputDirectory);
    ArrayList<File> inputTextFiles =
        new ArrayList<>(
            Arrays.asList(Objects.requireNonNull(inputDirectory.listFiles(new TextFileFilter()))));
    inputTextFiles.forEach(
        textFile -> {
          try (BufferedReader reader = new BufferedReader(new FileReader(textFile))) {
            reader
                .lines()
                .forEach(
                    sentence -> {
                      String sentenceWithoutPunctuation =
                          convertToLowerAndRemovePunctuationFromText(sentence);
                      String normalizedSentence = normalizeText(sentenceWithoutPunctuation);
                      Document document = new Document();
                      document.add(
                          new VectorTextField(
                              this.queryFieldName, normalizedSentence, TextField.TYPE_NOT_STORED));
                      document.add(new Field(this.resultFieldName, sentence, StoredField.TYPE));
                      documentsToBeIndexed.add(document);
                    });
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        });
    return documentsToBeIndexed;
  }

  private void createIndexFromDocuments(ArrayList<Document> documents) {
    StandardAnalyzer standardAnalyzer = new StandardAnalyzer();
    IndexWriterConfig indexWriterConfig = new IndexWriterConfig(standardAnalyzer);
    if (this.indexClearOnStart) {
      indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
    } else {
      indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.APPEND);
    }
    try (Directory index = FSDirectory.open(Path.of(this.indexDirectory));
        IndexWriter indexWriter = new IndexWriter(index, indexWriterConfig)) {
      indexWriter.addDocuments(documents);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
