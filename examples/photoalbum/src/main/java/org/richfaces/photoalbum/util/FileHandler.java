package org.richfaces.photoalbum.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import org.richfaces.model.UploadedFile;

/**
 * A wrapper class for handling two different classes: RichFaces' UploadedFile and java.io.File
 * 
 *      UploadedFile is created from a local file when using rich:fileUploader
 *      File is created from a remote file when downloading a file from an URL
 * 
 * @author mpetrov
 *
 */

public class FileHandler {
    private File file;
    private UploadedFile uFile;
    
    private boolean isStandardFile;
    
    public FileHandler(File file) {
        if (file != null) {
            this.file = file;
            isStandardFile = true;
        }
    }
    
    public FileHandler(UploadedFile uFile) {
        if (uFile != null) {
            this.uFile = uFile;
            isStandardFile = false;
        }
    }
    
    public InputStream getInputStream() throws FileNotFoundException, IOException {
        return isStandardFile ? new FileInputStream(file) : uFile.getInputStream();
    }
    
    public String getName() {
        return isStandardFile ? file.getName() : uFile.getName();
    }
    
    public long getSize() {
        return isStandardFile ? file.length() : uFile.getSize();
    }
    
    public void delete() throws IOException {
        if (isStandardFile) {
            Files.delete(file.toPath());
            return;
        }
        
        uFile.delete();
    }
}
