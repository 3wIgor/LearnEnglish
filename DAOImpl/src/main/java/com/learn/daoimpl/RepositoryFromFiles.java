package com.learn.daoimpl;

import com.learn.dao.Repository;

import java.io.*;
import java.util.*;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class RepositoryFromFiles implements Repository {

    private static final Logger logger = LoggerFactory.getLogger(RepositoryFromFiles.class);
    private final String dirLessons = "lessons/";
    private final List<String> lessonFilses = new LinkedList<>();
    private final List<String> textFiles = new LinkedList<>();
    private final String startLessonFileName = "Lesson ";
    private final String startTextFileName = "Text ";

    public RepositoryFromFiles() {
        logger.debug(null);

        int countLesson = 6;
        int countText = 6;

        for (int i = 1; i <= countLesson; i++) {
            lessonFilses.add(startLessonFileName + i);
        }
        for (int i = 1; i <= countText; i++) {
            textFiles.add(startTextFileName + i);
        }
    }

    @Override
    public String[] getLessonsNames() {
        String[] res = lessonFilses.toArray(new String[lessonFilses.size()]);
        logger.debug("return: {}", Arrays.toString(res));
        return res;
    }

    @Override
    public String[] getTextsNames() {
        String[] res = textFiles.toArray(new String[textFiles.size()]);
        logger.debug("return: {}", Arrays.toString(res));
        return res;
    }

    @Override
    public Map<String, String> getWorlds(String lessonName) {
        logger.debug("arg lessonName: {}", lessonName);
        Map<String, String> dictionary = new LinkedHashMap<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                this.getClass().getClassLoader().getResourceAsStream(dirLessons + lessonName), "UTF-8"))) {
            while (br.ready()) {
                StringTokenizer worlds = new StringTokenizer(br.readLine(), "|");
                String str[] = new String[2];
                str[0] = worlds.nextToken();
                str[1] = worlds.nextToken();
                dictionary.put(str[1], str[0]);
            }
        } catch (IOException ex) {
            logger.error("Exception: ", ex);
        }
        logger.debug("return: {}", dictionary);
        return dictionary;
    }

    @Override
    public String[] getLinesOfText(String textName) {
        logger.debug("arg textName: {}", textName);
        List<String> strs = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                RepositoryFromFiles.class.getClassLoader().getResourceAsStream(dirLessons + textName), "UTF-8"))) {
            while (br.ready()) {
                StringTokenizer p = new StringTokenizer(br.readLine(), ".");
                while(p.hasMoreTokens()){
                    strs.add(p.nextToken().trim() + ".");
                }
            }
        } catch (IOException ex) {
            logger.error("Exception: ", ex);
        }
        logger.debug("return: {}", strs);
        return strs.toArray(new String[strs.size()]);
    }

}
