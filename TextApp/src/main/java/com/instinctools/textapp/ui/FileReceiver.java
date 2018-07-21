package com.instinctools.textapp.ui;

import com.vaadin.ui.Upload;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class FileReceiver implements Upload.Receiver {
    @Override
    public OutputStream receiveUpload(String s, String s1) {
        try {
            return new FileOutputStream(s);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
