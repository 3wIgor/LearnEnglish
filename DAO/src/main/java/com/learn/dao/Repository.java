package com.learn.dao;

import java.util.Map;

public interface Repository {
    public String[] getLessonsNames();
    public String[] getTextsNames();
    public Map<String, String> getWorlds(String lessonName);
    public String[] getLinesOfText(String textName);
}
