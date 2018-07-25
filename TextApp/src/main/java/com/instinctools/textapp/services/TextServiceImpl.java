package com.instinctools.textapp.services;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

@Service
public class TextServiceImpl implements TextService {

    private static final String OK_BRACKETS = "correct";
    private static final String FAIL_BRACKETS = "incorrect";

    private List<String> wordBook = new ArrayList<>();
    private Comparator myComp;

    @Override
    public String topWords(String fileName) {
        StringBuffer sb = getDataFromFile(fileName);
        Map<String, Long> words = new HashMap<>();
        String lineText = sb.toString();
        lineText = lineText.replaceAll("\\p{Punct}", "");
        String[] arr = lineText.split(" ");

        for (int i = 0; i < arr.length; i++) {
            if (!wordBook.contains(arr[i])) {
                if (!words.containsKey(arr[i].toLowerCase())) {
                    words.put(arr[i].toLowerCase(), 1l);
                } else {
                    words.replace(arr[i].toLowerCase(),
                            words.get(arr[i].toLowerCase()), words.get(arr[i].toLowerCase()) + 1l);
                }
            }
        }

        List topWords = new ArrayList<Map.Entry>(words.entrySet());
        Collections.sort(topWords, myComp);
        StringBuffer result = new StringBuffer();

        for (int i = 0; i < ((topWords.size() < 10) ? topWords.size() : 10); i++)
            result.append((i + 1) + ": слово - '" + ((Map.Entry) topWords.get(i)).getKey() + "', количество повторений - "
                    + ((Map.Entry) topWords.get(i)).getValue() + ";\n");

        return result.toString();
    }

    @Override
    public String checkBrackets(String fileName) {
        StringBuffer sb = getDataFromFile(fileName);
        //todo
        return FAIL_BRACKETS;
    }

    private StringBuffer getDataFromFile(String fileName) {
        StringBuffer sb = new StringBuffer();
        try (Scanner sc = new Scanner(new FileInputStream(fileName))) {
            while (sc.hasNextLine())
                sb.append(sc.nextLine());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return sb;
    }

    @PostConstruct
    private void init() {
        myComp = (o1, o2) -> ((Long) ((Map.Entry) o2).getValue()).compareTo(((Long) ((Map.Entry) o1).getValue()));
    }
}
