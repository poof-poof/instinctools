package com.instinctools.textapp.ui;

import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;

@SpringUI
public class TextUI extends UI {

    private static final String WORDS_TITLE_BTN = "Топ 10 слов";
    private static final String BRACKETS_TITLE_BTN = "Проверить скобки";

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

        setContent(layout);
    }

}
