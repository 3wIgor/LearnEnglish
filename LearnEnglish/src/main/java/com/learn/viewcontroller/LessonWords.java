package com.learn.viewcontroller;

import com.learn.dao.Repository;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LessonWords {

    private static final Logger logger = LoggerFactory.getLogger(LessonWords.class);
    private final Repository repository;
    private Map<String, String> dictionaryRussianEnglish = new LinkedHashMap<>();
    private final Queue<String> queueRussianWords = new LinkedList<>();
    private String carentLesson;

    public LessonWords(Repository repository) {
        logger.debug("arg repository: {}", repository);
        this.repository = repository;
    }

    public Boolean ckeckWord(String englishWord) {
        Boolean res = englishWord.equalsIgnoreCase(dictionaryRussianEnglish.get(queueRussianWords.peek()));
        logger.debug("arg englishWord: {}, return: {}", new Object[]{englishWord, res});
        return res;
    }

    public String getEnglishWord(String russianWord) {
        String res = dictionaryRussianEnglish.get(russianWord);
        logger.debug("arg russianWord: {}, return: {}", new Object[]{russianWord, res});
        return res;
    }

    public String carentWord() {
        String res = queueRussianWords.peek();
        logger.debug("return: {}", res);
        return res;
    }

    public Boolean nextWord() {
        queueRussianWords.remove();
        Boolean res = !queueRussianWords.isEmpty();
        logger.debug("return: {}", res);
        return res;
    }

    public void refrechLesson() {
        logger.debug(null);
        dictionaryRussianEnglish.clear();
        queueRussianWords.clear();
        initLesson();
    }

    public void setLesson(String lesson) {
        logger.debug("arg lesson: {}", lesson);
        carentLesson = lesson;
        refrechLesson();
    }

    public String[] getAllRusianWords() {
        String[] res = queueRussianWords.toArray(new String[queueRussianWords.size()]);
        logger.debug("return: {}", Arrays.toString(res));
        return res;
    }

    private void initLesson() {
        logger.debug(null);
        dictionaryRussianEnglish = repository.getWorlds(carentLesson);
        queueRussianWords.addAll(dictionaryRussianEnglish.keySet());
    }
}
