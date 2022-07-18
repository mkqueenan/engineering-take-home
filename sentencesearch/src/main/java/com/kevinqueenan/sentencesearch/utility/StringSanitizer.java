package com.kevinqueenan.sentencesearch.utility;

import java.text.Normalizer;

public class StringSanitizer {

  public static String convertToLowerAndRemovePunctuationFromText(String rawText) {
    return rawText.toLowerCase().replaceAll("\\p{Punct}", "");
  }

  public static String normalizeText(String rawText) {
    return Normalizer.normalize(rawText, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
  }
}
