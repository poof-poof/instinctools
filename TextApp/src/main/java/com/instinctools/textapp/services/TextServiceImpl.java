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

    private List<String> wordBook;
    private Comparator myComp;

    @Override
    public String topWords(String fileName) {
        StringBuffer sb = getDataFromFile(fileName);
        Map<String, Long> words = new HashMap<>();
        String lineText = sb.toString().toLowerCase();
        lineText = lineText.replaceAll("\\p{Punct}", "");//remove !"#$%&'()*+,-./:;<=>?@[\]^_`{|}~
        String[] arr = lineText.split(" ");
        for (int i = 0; i < arr.length; i++) {
            if (!arr[i].toLowerCase().equals("") && !wordBook.contains(arr[i]) ) {
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
        String line = sb.toString().replaceAll("\\p{Alnum}","");//remove digits and charcters
        line = line.replaceAll("\\p{Space}","");
        while (line.length() != 0){
            line = line.replaceAll("\\[\\]","");
            line = line.replaceAll("\\(\\)","");
            line = line.replaceAll("\\{\\}","");
            if(line.length() % 2 != 0 || (line.length() % 2 == 0 && (line.contains("}{") || line.contains(")(") || line.contains("]["))))
                return FAIL_BRACKETS;
        }
        return OK_BRACKETS;
    }

    private StringBuffer getDataFromFile(String fileName) {
        StringBuffer sb = new StringBuffer();
        try (Scanner sc = new Scanner(new FileInputStream(fileName), "UTF-8")) {
            while (sc.hasNextLine())
                sb.append(sc.nextLine());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return sb;
    }

    @PostConstruct
    private void init() {
        //my comparator for sort list words DSC
        myComp = (o1, o2) -> ((Long) ((Map.Entry) o2).getValue()).compareTo(((Long) ((Map.Entry) o1).getValue()));
        wordBook = new ArrayList<>();
        String[] arr = {
               "а","абы","аж","ан","благо","буде","будто","вроде","да","дабы","даже","едва","ежели","если","же","затем",
                "зато","и","ибо","или","итак","кабы","как","когда","коли","коль","ли","либо","лишь","нежели","но","пока",
                "покамест","покуда","поскольку","притом","причем","пускай","пусть","раз","разве","ровно","сиречь",
                "словно","так","также","тоже","только","точно","хоть","хотя","чем","чисто","что","чтоб","чтобы","чуть",
                "якобы","в","без","до","из","к","на","по","о","от","перед","при","через","с","у","за","над","об","под",
                "про","для"
        };
        for (int i = 0; i < arr.length; i++)
            wordBook.add(arr[i]);
    }
}
