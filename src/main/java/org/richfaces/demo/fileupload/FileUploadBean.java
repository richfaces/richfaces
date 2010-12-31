package org.richfaces.demo.fileupload;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;

/**
 * @author Ilya Shaikovsky
 * 
 */
@ManagedBean
@SessionScoped
public class FileUploadBean implements Serializable {

    private ArrayList<UploadedFile> files = new ArrayList<UploadedFile>();
    private int uploadsAvailable = 5;

    public void paint(OutputStream stream, Object object) throws IOException {
        stream.write(getFiles().get((Integer) object).getData());
        stream.close();
    }

    public void listener(FileUploadEvent event) throws Exception {
        files.add(event.getUploadedFile());
        uploadsAvailable--;
    }

    public String clearUploadData() {
        files.clear();
        setUploadsAvailable(5);
        return null;
    }

    public int getSize() {
        if (getFiles().size() > 0) {
            return getFiles().size();
        } else {
            return 0;
        }
    }

    public ArrayList<UploadedFile> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<UploadedFile> files) {
        this.files = files;
    }

    public long getTimeStamp() {
        return System.currentTimeMillis();
    }

    public int getUploadsAvailable() {
        return uploadsAvailable;
    }

    public void setUploadsAvailable(int uploadsAvailable) {
        this.uploadsAvailable = uploadsAvailable;
    }

}
