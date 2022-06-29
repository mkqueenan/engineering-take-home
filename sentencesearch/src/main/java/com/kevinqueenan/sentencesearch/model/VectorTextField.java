package com.kevinqueenan.sentencesearch.model;

import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexableFieldType;

public class VectorTextField extends Field {

  public static final FieldType TYPE_NOT_STORED = new FieldType();

  public static final FieldType TYPE_STORED = new FieldType();

  static {
    TYPE_NOT_STORED.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS);
    TYPE_NOT_STORED.setTokenized(true);
    TYPE_NOT_STORED.setStored(false);
    TYPE_NOT_STORED.setStoreTermVectors(true);
    TYPE_NOT_STORED.setStoreTermVectorPositions(true);
    TYPE_NOT_STORED.freeze();

    TYPE_STORED.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS);
    TYPE_STORED.setTokenized(true);
    TYPE_STORED.setStored(true);
    TYPE_STORED.setStoreTermVectors(true);
    TYPE_STORED.setStoreTermVectorPositions(true);
    TYPE_STORED.freeze();
  }

  public VectorTextField(String name, CharSequence value, IndexableFieldType type) {
    super(name, value, type == TextField.TYPE_NOT_STORED ? TYPE_NOT_STORED : TYPE_STORED);
  }
}
