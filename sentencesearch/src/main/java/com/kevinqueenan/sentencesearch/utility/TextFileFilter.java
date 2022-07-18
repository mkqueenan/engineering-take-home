package com.kevinqueenan.sentencesearch.utility;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FilenameFilter;

public class TextFileFilter implements FilenameFilter {

  @Override
  public boolean accept(File directory, @NotNull String fileName) {
    return fileName.toLowerCase().endsWith(".txt");
  }
}
