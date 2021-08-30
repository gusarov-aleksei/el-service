package org.example.esl;

/**
 * It defines literals which are used in text algorithm for content extraction
 */
public interface EnglishContentLiterals {
    String GLOSSARY_KEY = "GLOSSARY";
    String COMPREHENSION_QUESTION_KEY = "COMPREHENSION QUESTIONS";
    String WHAT_ELSE_KEY = "WHAT ELSE DOES IT MEAN?";
    String CULTURE_NOTE_KEY = "CULTURE NOTE";
    String COMPLETE_TRANSCRIPT_KEY = "COMPLETE TRANSCRIPT";
    String START_NEW_LINE = "\r\n";
    String SPACE = " ";
    String START_NEW_LINE_TWICE = START_NEW_LINE + START_NEW_LINE;
    String START_NEW_LINE_TWICE_2 = START_NEW_LINE + " " + START_NEW_LINE;

    String[] START_NEW_LINE_TWICE_LITERALS = new String[]{START_NEW_LINE_TWICE, START_NEW_LINE_TWICE_2};
    String[] DASH_LITERALS = new String[]{" – ", " - "};

    String PATTERN_IN_TEXT = START_NEW_LINE + "%s";

    String[] GLOSSARY_LITERALS = new String[]{
            String.format(PATTERN_IN_TEXT, GLOSSARY_KEY), GLOSSARY_KEY,  String.format(PATTERN_IN_TEXT, "Glossary")};
    String[] COMPREHENSION_LITERALS = new String[]{
            String.format(PATTERN_IN_TEXT, COMPREHENSION_QUESTION_KEY), COMPREHENSION_QUESTION_KEY,  String.format(PATTERN_IN_TEXT, "Comprehension questions")};
    String[] WHAT_ELSE_LITERALS = new String[]{
            String.format(PATTERN_IN_TEXT, WHAT_ELSE_KEY), WHAT_ELSE_KEY,  String.format(PATTERN_IN_TEXT, "What else does it mean?")};
    String[] CULTURE_NOTE_LITERALS = new String[]{
            String.format(PATTERN_IN_TEXT, CULTURE_NOTE_KEY), CULTURE_NOTE_KEY,  String.format(PATTERN_IN_TEXT, "Culture note")};
    String[] TRANSCRIPT_LITERALS = new String[]{
            String.format(PATTERN_IN_TEXT, COMPLETE_TRANSCRIPT_KEY), COMPLETE_TRANSCRIPT_KEY,  String.format(PATTERN_IN_TEXT, "Complete transcript")};
    //possible sections in document in order of appearance
    String[][] ALL_LITERALS = new String[][]{
            GLOSSARY_LITERALS,
            COMPREHENSION_LITERALS,
            WHAT_ELSE_LITERALS,
            CULTURE_NOTE_LITERALS,
            TRANSCRIPT_LITERALS
    };

    String[] END_OF_CULTURE_NOTES_LITERALS = new String[]{
            START_NEW_LINE + "_", "Comprehension Questions Correct Answer"
    };

}
