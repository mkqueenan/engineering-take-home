package com.kevinqueenan.sentencesearch.utility;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TextFileFilterTests {

  TextFileFilter textFileFilter;

  String pathname = "";

  File inputDirectory;

  @BeforeAll
  void setUp() {
    this.textFileFilter = new TextFileFilter();
    this.inputDirectory = new File(this.pathname);
  }

  @Test
  @DisplayName("Text filename filter accepts a filename ending in .txt")
  void validFileNameAccept() {
    assertThat(this.textFileFilter.accept(this.inputDirectory, "testfile.txt")).isTrue();
  }

  @Test
  @DisplayName("Text filename filter rejects a filename not ending in .txt")
  void validFileNameReject() {
    assertThat(this.textFileFilter.accept(this.inputDirectory, "testfile.md")).isFalse();
  }

  @Test
  @DisplayName("Text filename filter rejects an empty filename")
  void emptyFileNameReject() {
    assertThat(this.textFileFilter.accept(this.inputDirectory, "")).isFalse();
  }
}
