package com.kevinqueenan.sentencesearch.service;

import com.kevinqueenan.sentencesearch.utility.TextFileFilter;
import com.kevinqueenan.sentencesearch.utility.VectorTextField;
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
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Path;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;

@Slf4j
@Service
public class SentenceIndexer {

    private final String inputDirectory;
    private final String indexDirectory;

    public SentenceIndexer(@Value("${input.directory}") final String inputDirectory, @Value("${index.directory}") final String indexDirectory) throws IOException {
        this.inputDirectory = inputDirectory;
        this.indexDirectory = indexDirectory;
        ArrayList<Document> documents = this.generateDocumentsFromSentences();
        this.createIndexFromDocuments(documents);
    }

    private ArrayList<Document> generateDocumentsFromSentences() {
        ArrayList<Document> documentsToBeIndexed = new ArrayList<>();
        File inputDirectory = new File(this.inputDirectory);
        ArrayList<File> inputTextFiles = new ArrayList<>(Arrays.asList(inputDirectory.listFiles(new TextFileFilter())));
        inputTextFiles.forEach(textFile -> {
            try (BufferedReader reader = new BufferedReader(new FileReader(textFile))) {
                reader.lines().forEach(sentence -> {
                    String sentenceWithoutPunctuation = sentence.toLowerCase().replaceAll("\\p{Punct}", "");
                    String normalizedSentence = Normalizer.normalize(sentenceWithoutPunctuation, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
                    Document document = new Document();
                    document.add(new VectorTextField("normalizedText", normalizedSentence, TextField.TYPE_NOT_STORED));
                    document.add(new Field("originalSentence", sentence, StoredField.TYPE));
                    documentsToBeIndexed.add(document);
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return documentsToBeIndexed;
    }

    private void createIndexFromDocuments(ArrayList<Document> documents) throws IOException {
        StandardAnalyzer standardAnalyzer = new StandardAnalyzer();
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(standardAnalyzer);
        try (Directory index = FSDirectory.open(Path.of(this.indexDirectory)); IndexWriter indexWriter = new IndexWriter(index, indexWriterConfig)) {
            indexWriter.deleteAll();
            indexWriter.addDocuments(documents);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
