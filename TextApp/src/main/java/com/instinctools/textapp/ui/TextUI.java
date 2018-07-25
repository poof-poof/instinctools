package com.instinctools.textapp.ui;

import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

@SpringUI
public class TextUI extends UI {

    private static final String WORDS_TITLE_BTN = "Топ 10 слов";
    private static final String BRACKETS_TITLE_BTN = "Проверить скобки";
    private static final String WORDS_DATA = "words";
    private static final String BRACKETS_DATA = "brackets";
    private static final String ERROR_TYPE_MSG = "Ошибка! Выберите файл формата *.txt!";

    @Value("${textapp.file.type}")
    private String MIME_TYPE;

    @Value("${textapp.uploads.path}")
    private String UPLOADS_PATH;

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
        log = new TextArea("Результаты:");
        receiver = new FileReceiver(UPLOADS_PATH);
        buttons = new HorizontalLayout(words, brackets);
        layout = new VerticalLayout(buttons, log);
        UPLOADS_PATH = System.getProperty("user.dir") + UPLOADS_PATH;

        log.setSizeFull();
        layout.setSizeFull();
        layout.setMargin(true);

        layout.setExpandRatio(buttons, 0.1f);
        layout.setExpandRatio(log, 0.9f);

        words.setButtonCaption(WORDS_TITLE_BTN);
        brackets.setButtonCaption(BRACKETS_TITLE_BTN);

        log.setWordWrap(true);
        log.setReadOnly(true);

        words.setReceiver(receiver);
        brackets.setReceiver(receiver);

        words.addSucceededListener(e -> {
            if (MIME_TYPE.equals(e.getMIMEType())) {
                log.clear();
                getData(e.getFilename(), true);
            } else {
                Notification.show(ERROR_TYPE_MSG, Notification.Type.ERROR_MESSAGE);
                removeFileFromServer(e.getFilename());
            }
        });
        brackets.addSucceededListener(e -> {
            if (MIME_TYPE.equals(e.getMIMEType())) {
                log.clear();
                getData(e.getFilename(), false);
            } else {
                Notification.show(ERROR_TYPE_MSG, Notification.Type.ERROR_MESSAGE);
                removeFileFromServer(e.getFilename());
            }
        });

        setContent(layout);
    }

    private void removeFileFromServer(String filename) {
        File f = new File(UPLOADS_PATH + filename);
        f.delete();
    }

    private void putLog(String text) {
        log.setValue(log.getValue() + text + '\n');
    }

    private void getData(String fileName, boolean type) {
        try {
            URL url = new URL("http://localhost:8080/" + ((type) ? WORDS_DATA : BRACKETS_DATA));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "text/plane; charset=UTF-8");
            DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
            fileName = UPLOADS_PATH + fileName;
            dos.write(fileName.getBytes("UTF-8"));
            dos.flush();
            dos.close();
            Scanner in = new Scanner(connection.getInputStream(), "UTF-8");
            while (in.hasNextLine()) {
                putLog(in.nextLine());
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
