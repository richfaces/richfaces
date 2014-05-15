/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2014, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 *******************************************************************************/
package org.richfaces.fragment.fileUpload;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.fragment.Root;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.AdvancedInteractions;
import org.richfaces.fragment.common.Utils;
import org.richfaces.fragment.common.picker.ChoicePicker;
import org.richfaces.fragment.common.picker.ChoicePickerHelper;
import org.richfaces.fragment.list.AbstractListComponent;
import org.richfaces.fragment.list.ListComponent;
import org.richfaces.fragment.list.RichFacesListItem;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;

public class RichFacesFileUpload implements FileUpload, AdvancedInteractions<RichFacesFileUpload.AdvancedFileUploadInteractions> {

    @Root
    private WebElement rootElement;

    @ArquillianResource
    private JavascriptExecutor executor;

    @FindBy(className = "rf-fu-lst")
    private RichFacesFileUploadList items;

    @FindBy(className = "rf-fu-btn-cnt-add")
    private WebElement addButtonElement;
    @FindBy(className = "rf-fu-btn-cnt-add-dis")
    private WebElement disabledAddButtonElement;
    @FindBy(className = "rf-fu-btn-clr")
    private WebElement clearAllButtonElement;
    @FindBy(className = "rf-fu-btn-cnt-upl")
    private WebElement uploadButtonElement;
    @FindByJQuery(".rf-fu-inp:last")
    private WebElement fileInputElement;
    @FindBy(className = "rf-fu-inp")
    private List<WebElement> fileInputElements;
    @FindBy(css = ".rf-fu-btn-add > span")
    private WebElement inputContainer;

    private final AdvancedFileUploadInteractions interactions = new AdvancedFileUploadInteractions();

    @Override
    public boolean addFile(File file) {
        final int expectedSize = fileInputElements.size() + 1;
        String containerStyleClassBefore = inputContainer.getAttribute("class");
        Utils.jQ("attr('class', '')", inputContainer);
        fileInputElement.sendKeys(file.getAbsolutePath());
        Utils.jQ("attr('class', '" + containerStyleClassBefore + "')", inputContainer);
        try {
            Graphene.waitGui().withTimeout(1, TimeUnit.SECONDS).until(new Predicate<WebDriver>() {
                @Override
                public boolean apply(WebDriver input) {
                    return fileInputElements.size() == expectedSize;
                }
            });
        } catch (TimeoutException ignored) {
            return FALSE;
        }
        return TRUE;
    }

    @Override
    public AdvancedFileUploadInteractions advanced() {
        return interactions;
    }

    @Override
    public FileUpload clearAll() {
        clearAllButtonElement.click();
        Graphene.waitGui().until(new Predicate<WebDriver>() {
            @Override
            public boolean apply(WebDriver input) {
                return items.isEmpty();
            }
        });
        return this;
    }

    @Override
    public FileUpload upload() {
        uploadButtonElement.click();
        return this;
    }

    public class FileUploadItemImpl extends RichFacesListItem implements FileUploadItem {

        @FindBy(className = "rf-fu-itm-lbl")
        private WebElement filenameElement;
        @FindBy(className = "rf-fu-itm-st")
        private WebElement stateElement;
        @FindBy(className = "rf-fu-itm-lnk")
        private WebElement clearOrDeleteElement;

        @Override
        public WebElement getClearOrDeleteElement() {
            return clearOrDeleteElement;
        }

        @Override
        public String getFilename() {
            return filenameElement.getText();
        }

        @Override
        public WebElement getFilenameElement() {
            return filenameElement;
        }

        @Override
        public String getState() {
            return stateElement.getText();
        }

        @Override
        public WebElement getStateElement() {
            return stateElement;
        }

        @Override
        public boolean isUploaded() {
            if (Utils.isVisible(stateElement) && getState().equals(advanced().getDoneLabel())) {
                return TRUE;
            }
            return FALSE;
        }

        @Override
        public void remove() {
            clearOrDeleteElement.click();
        }
    }

    public static class RichFacesFileUploadList extends AbstractListComponent<FileUploadItemImpl> {
    }

    public class AdvancedFileUploadInteractions {

        private static final String DEFAULT_DONE_LABEL = "Done";
        private String doneLabel;

        public WebElement getRootElement() {
            return rootElement;
        }

        public WebElement getAddButtonElement() {
            return addButtonElement;
        }

        public WebElement getDisabledAddButtonElement() {
            return disabledAddButtonElement;
        }

        public WebElement getClearAllButtonElement() {
            return clearAllButtonElement;
        }

        public WebElement getFileInputElement() {
            return fileInputElement;
        }

        public WebElement getUploadButtonElement() {
            return uploadButtonElement;
        }

        public String getDoneLabel() {
            return Optional.fromNullable(doneLabel).or(DEFAULT_DONE_LABEL);
        }

        public ListComponent<? extends FileUploadItem> getItems() {
            return items;
        }

        public void removeFile(int index) {
            removeFile(ChoicePickerHelper.byIndex().index(index));
        }

        public void removeFile(String match) {
            removeFile(ChoicePickerHelper.byVisibleText().match(match));
        }

        public void removeFile(ChoicePicker picker) {
            getItems().getItem(picker).remove();
        }

        public void setupDoneLabel() {
            this.doneLabel = DEFAULT_DONE_LABEL;
        }

        public void setupDoneLabel(String doneLabel) {
            this.doneLabel = doneLabel;
        }
    }
}
