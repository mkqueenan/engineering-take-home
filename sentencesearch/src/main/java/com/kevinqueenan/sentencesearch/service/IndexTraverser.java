package com.kevinqueenan.sentencesearch.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

@Slf4j
@Service
public class IndexTraverser {

    private final String indexDirectory;

    public IndexTraverser(@Value("${index.directory}") final String indexDirectory) {
        this.indexDirectory = indexDirectory;
    }

    public HashMap<String, Integer> getSearchTermFrequency (final String searchTerm) throws ParseException {
        HashMap<String, Integer> searchTermResults = new HashMap<>();
        StandardAnalyzer analyzer = new StandardAnalyzer();
        Query query = new QueryParser("normalizedText", analyzer).parse(searchTerm);
        try (Directory index = FSDirectory.open(Path.of(this.indexDirectory)); IndexReader indexReader = DirectoryReader.open(index)) {
            IndexSearcher indexSearcher = new IndexSearcher(indexReader);
            TopDocs topDocs = indexSearcher.search(query, 1000);
            ScoreDoc[] documentHits = topDocs.scoreDocs;
            for (int i = 0; i < documentHits.length; ++i) {
                int documentId = documentHits[i].doc;
                Document document = indexSearcher.doc(documentId);
                Terms terms = indexReader.getTermVector(documentId, "normalizedText");
                TermsEnum termsEnum = terms.iterator();
                while (termsEnum.next() != null) {
                    String currentTerm = termsEnum.term().utf8ToString();
                    if (currentTerm.equals(searchTerm)) {
                        searchTermResults.put(document.get("originalSentence"), Math.toIntExact(termsEnum.totalTermFreq()));
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
