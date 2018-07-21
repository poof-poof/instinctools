package com.instinctools.textapp.ui;

import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.Upload;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class FileReceiver implements Upload.Receiver {

    private String uploadsPath;

    public FileReceiver(String uploadsPath){
        this.uploadsPath = uploadsPath;
    }

    @Override
    public OutputStream receiveUpload(String s, String s1) {
        File f = new File(System.getProperty("user.dir") + uploadsPath);
        f.mkdir();
            try {
                return new FileOutputStream(f.getAbsoluteFile() + "/" + s);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        return null;
    }

}
