package com.instinctools.textapp.services;

import org.springframework.stereotype.Service;

@Service
public class TextServiceImpl implements TextService {
    @Override
    public String topWords(String fileName) {
        return "Words file name: " + fileName;
    }

    @Override
    public String checkBrackets(String fileName) {
        return "Brackets file name: " + fileName;
    }
}
