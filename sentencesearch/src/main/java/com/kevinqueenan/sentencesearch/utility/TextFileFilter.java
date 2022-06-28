package com.kevinqueenan.sentencesearch.utility;

import java.io.File;
import java.io.FilenameFilter;

public class TextFileFilter implements FilenameFilter {

    @Override
    public boolean accept(File directory, String fileName) {
        return fileName.toLowerCase().endsWith(".txt");
    }

}
