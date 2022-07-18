package com.kevinqueenan.sentencesearch.utility;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.kevinqueenan.sentencesearch.utility.StringSanitizer.convertToLowerAndRemovePunctuationFromText;
import static org.assertj.core.api.Assertions.assertThat;

public class StringSanitizerTests {

  @Test
  @DisplayName("Basic confirmation of function capabilities")
  void converting_to_lower_and_removing_punctuation_is_matching_against_correct_output() {
    assertThat(convertToLowerAndRemovePunctuationFromText("Hello, my name is Kevin!"))
        .isEqualTo("hello my name is kevin");
  }

  @Test
  @DisplayName("Basic confirmation that function converts to lower case")
  void converting_to_lower_and_removing_punctuation_is_not_matching_upper_case_characters() {
    assertThat(convertToLowerAndRemovePunctuationFromText("Hello, my name is Kevin!"))
        .isNotEqualTo("Hello my name is Kevin");
  }

  @Test
  @DisplayName("Basic confirmation that function removes punctuation")
  void converting_to_lower_and_removing_punctuation_is_not_matching_punctuation() {
    assertThat(convertToLowerAndRemovePunctuationFromText("Hello, my name is Kevin!"))
        .isNotEqualTo("hello, my name is kevin!");
  }

  @Test
  @DisplayName("Basic confirmation that function accepts an empty string value")
  void converting_to_lower_and_removing_punctuation_is_matching_against_an_empty_string() {
    assertThat(convertToLowerAndRemovePunctuationFromText("")).isEqualTo("");
  }
}
