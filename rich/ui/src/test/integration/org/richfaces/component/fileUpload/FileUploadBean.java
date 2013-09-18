package org.richfaces.component.fileUpload;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;

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
