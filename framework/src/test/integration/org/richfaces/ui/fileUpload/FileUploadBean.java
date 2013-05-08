package org.richfaces.ui.fileUpload;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.richfaces.model.UploadedFile;
import org.richfaces.ui.input.fileUpload.FileUploadEvent;

@Named
@RequestScoped
public class FileUploadBean {

    private UploadedFile uploadedFile;

    public void listener(FileUploadEvent event) throws Exception {
        uploadedFile = event.getUploadedFile();
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }
}
