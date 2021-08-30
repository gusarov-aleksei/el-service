package org.example.esl;

import java.util.Map;

public class EnglishContent {
    //TODO add topic and file name of source
    private Map<String, String> glossary;
    private Map<String, String> whatElse;
    private String cultureNotes;

    public Map<String, String> getGlossary() {
        return glossary;
    }

    public void setGlossary(Map<String, String> glossary) {
        this.glossary = glossary;
    }

    public Map<String, String> getWhatElse() {
        return whatElse;
    }

    public void setWhatElse(Map<String, String> whatElse) {
        this.whatElse = whatElse;
    }

    public String getCultureNotes() {
        return cultureNotes;
    }

    public void setCultureNotes(String cultureNotes) {
        this.cultureNotes = cultureNotes;
    }
}
