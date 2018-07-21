package com.instinctools.textapp.ui;

import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

@SpringUI
public class TextUI extends UI {

    private static final String WORDS_TITLE_BTN = "Топ 10 слов";
    private static final String BRACKETS_TITLE_BTN = "Проверить скобки";
    private static final String WORDS_DATA = "words";
    private static final String BRACKETS_DATA = "brackets";

    private Upload words;
    private Upload brackets;

    private FileReceiver receiver;

    private TextArea log;
    private VerticalLayout layout;
    private HorizontalLayout buttons;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        words = new Upload();
        brackets = new Upload();
        receiver = new FileReceiver();
        log = new TextArea("Результаты:");
        buttons = new HorizontalLayout(words,brackets);
        layout = new VerticalLayout(buttons,log);

        log.setSizeFull();
        layout.setSizeFull();
        layout.setMargin(true);

        layout.setExpandRatio(buttons,0.1f);
        layout.setExpandRatio(log,0.9f);

        words.setButtonCaption(WORDS_TITLE_BTN);
        brackets.setButtonCaption(BRACKETS_TITLE_BTN);

        log.setWordWrap(true);
        log.setReadOnly(true);

        words.setReceiver(receiver);
        brackets.setReceiver(receiver);

        words.addSucceededListener(e -> {
            log.clear();
            getData(e.getFilename(),true);
        });
        brackets.addSucceededListener(e ->{
            log.clear();
            getData(e.getFilename(),false);
        });

        setContent(layout);
    }

    private void putLog(String text){
        log.setValue(log.getValue() + text + '\n');
    }

    private void getData(String fileName,boolean type){
        try {
            URL url = new URL("http://localhost:8080/" + ((type) ? WORDS_DATA : BRACKETS_DATA));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "text/plane; charset=UTF-8");
            DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
            dos.write(fileName.getBytes("UTF-8"));
            dos.flush();
            dos.close();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                putLog(inputLine);
            }
            in.close();
            connection.disconnect();
        } catch (MalformedURLException e) {
            System.out.println(e.getMessage());
        } catch (ProtocolException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
