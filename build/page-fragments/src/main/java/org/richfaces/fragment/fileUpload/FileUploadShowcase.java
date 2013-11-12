package org.richfaces.fragment.fileUpload;

import java.io.File;

import org.openqa.selenium.support.FindBy;

public class FileUploadShowcase {

    @FindBy
    private RichFacesFileUpload fileUpload;

    public void showcase_file_upload() {
        fileUpload.addFile(new File("/home/foo/Downloads/bar.jpg"));
        fileUpload.upload();

        fileUpload.clearAll();
    }
}
