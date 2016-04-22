/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
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
 */
package org.richfaces.component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.el.MethodExpression;
import javax.faces.component.UIComponentBase;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.FacesEvent;
import javax.faces.event.ListenerFor;
import javax.faces.event.PostAddToViewEvent;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.component.attribute.AjaxProps;
import org.richfaces.component.attribute.CoreProps;
import org.richfaces.component.attribute.DisabledProps;
import org.richfaces.component.attribute.ErrorProps;
import org.richfaces.component.attribute.EventsKeyProps;
import org.richfaces.component.attribute.EventsMouseProps;
import org.richfaces.component.attribute.I18nProps;
import org.richfaces.event.FileUploadEvent;
import org.richfaces.event.FileUploadListener;
import org.richfaces.model.UploadedFile;
import org.richfaces.renderkit.FileUploadRendererBase;
import org.richfaces.view.facelets.FileUploadHandler;

/**
 * <p>
 * The &lt;rich:fileUpload&gt; component allows the user to upload files to a server. It features multiple uploads, progress
 * bars, restrictions on file types, and restrictions on sizes of the files to be uploaded.
 * </p>
 *
 * @author Konstantin Mishin
 * @author Lukas Fryc
 * @author Simone Cinti
 */
@JsfComponent(tag = @Tag(generate = false, handlerClass = FileUploadHandler.class), renderer = @JsfRenderer(type = "org.richfaces.FileUploadRenderer"))
@ListenerFor(systemEventClass = PostAddToViewEvent.class)
public abstract class AbstractFileUpload extends UIComponentBase implements AjaxProps, CoreProps, DisabledProps,
    EventsKeyProps, EventsMouseProps, ErrorProps, I18nProps {

    public static final String COMPONENT_TYPE = "org.richfaces.FileUpload";
    public static final String COMPONENT_FAMILY = "org.richfaces.FileUpload";

    private static final String QUEUED_FILE_UPLOAD_EVENTS_ATTR = "queuedFileUploadEvents";

    enum Properties {
        maxFileSize
    }

    /**
     * Defines comma separated list of file extensions accepted by component. The format of the file extension can be "png" or
     * ".png", the second format will filter extensions in the dialog window. The component does not provide any feedback when
     * rejecting file. For introducing feedback for rejection, use ontyperejected parameter.
     */
    @Attribute
    public abstract String getAcceptedTypes();

    /**
     * Defines maximum number of files allowed to be uploaded. After a number of files in the list equals to the value of this
     * attribute, "Add" button disappears and nothing could be uploaded even if you clear the whole list. In order to upload
     * files again you should rerender the component. (Negative numbers means no limits; default value -1).
     */
    @Attribute(defaultValue = "-1")
    public abstract Integer getMaxFilesQuantity();

    /**
     * Defines the maximum allowed size of a file. Files exceeding the size will not be added in the component.
     * Default value is equal to 'org.richfaces.fileUpload.maxRequestSize' context parameter if it is set, otherwise 0 (no limit).
     */
    @Attribute(defaultValue = "0L")
    public long getMaxFileSize() {
        return (Long) getStateHelper().eval(Properties.maxFileSize, FileUploadRendererBase.getMaxRequestSize());
    }

    public void setMaxFileSize(long maxFileSize) {
        getStateHelper().put(Properties.maxFileSize, maxFileSize);
    }

    /**
     * <p>
     * If "true" duplicate file uploads are prevented
     * </p>
     * <p>
     * Default is "false"
     * </p>
     */
    @Attribute(defaultValue = "false")
    public abstract boolean isNoDuplicate();

    /**
     * <p>
     * If "true" upload stats immediately after file selection
     * </p>
     * <p>
     * Default is "false"
     * </p>
     */
    @Attribute(defaultValue = "false")
    public abstract boolean isImmediateUpload();

    /**
     * Javascript code executed when a file is selected
     */
    @Attribute(events = @EventName("fileselect"))
    public abstract String getOnfileselect();

    /**
     * Javascript code executed when a file is submitted
     */
    @Attribute(events = @EventName("filesubmit"))
    public abstract String getOnfilesubmit();

    /**
     * Javascript code executed when a file does not meet the conditions stated by acceptedTypes parameter.
     */
    @Attribute(events = @EventName("typerejected"))
    public abstract String getOntyperejected();

    /**
     * Javascript code executed when a file exceeds the allowed size defined by maxFileSize parameter
     *  or 'org.richfaces.fileUpload.maxRequestSize' context parameter.
     */
    @Attribute(events = @EventName("sizerejected"))
    public abstract String getOnsizerejected();

    /**
     * Javascript code executed when a file upload is complete
     */
    @Attribute(events = @EventName("uploadcomplete"))
    public abstract String getOnuploadcomplete();

    /**
     * Javascript code executed when a file is cleared
     */
    @Attribute(events = @EventName("clear"))
    public abstract String getOnclear();

    /**
     * The client-side script method to be called when an error has occurred during Ajax communications
     */
    @Attribute(events = @EventName("error"))
    public abstract String getOnerror();

    /**
     * The label for the Add button.
     */
    @Attribute
    public abstract String getAddLabel();

    /**
     * The label for the Upload button.
     */
    @Attribute
    public abstract String getUploadLabel();

    /**
     * The label for the Clear button.
     */
    @Attribute
    public abstract String getClearAllLabel();

    /**
     * The label displayed when a file is successfully uploaded.
     */
    @Attribute
    public abstract String getDoneLabel();

    /**
     * The label displayed when a file exceeds the size limit.
     */
    @Attribute
    public abstract String getSizeExceededLabel();

    /**
     * The label displayed when a file upload fails due to a server error.
     */
    @Attribute
    public abstract String getServerErrorLabel();

    /**
     * The label displayed for the Clear link
     */
    @Attribute
    public abstract String getClearLabel();

    /**
     * The label displayed for the Delete link
     */
    @Attribute
    public abstract String getDeleteLabel();

    /**
     * <p>
     * Defines height of file list.
     * </p>
     * <p>
     * Default value is "210px".
     * </p>
     */
    @Attribute(defaultValue = "210px")
    public abstract String getListHeight();

    @Override
    public void processEvent(ComponentSystemEvent event) throws AbortProcessingException {
        super.processEvent(event);

        if (event.getSource() == this) {
            if (event instanceof PostAddToViewEvent) {
                this.getAttributes().put("queuedFileUploadEvents", new AtomicInteger(0));
            }
        }
    }

    /**
     * A listener function on the server side after each file is uploaded. The listener should process files as required, such
     * as storing them in the session/db/filesystem/ directory. The component itself does not store uploaded files, so if the
     * listener is not implemented they are not stored anywhere
     */
    @Attribute
    public abstract MethodExpression getFileUploadListener();

    /**
     * <p>
     * Add a new {@link FileUploadListener} to the set of listeners interested in being notified when
     * {@link org.richfaces.event.FileUploadEvent}s occur.
     * </p>
     *
     * @param listener The {@link FileUploadListener} to be added
     * @throws NullPointerException if <code>listener</code> is <code>null</code>
     */
    public void addFileUploadListener(FileUploadListener listener) {
        addFacesListener(listener);
    }

    /**
     * <p>
     * Return the set of registered {@link FileUploadListener}s for this {@link AbstractFileUpload} instance. If there are no
     * registered listeners, a zero-length array is returned.
     * </p>
     */
    public FileUploadListener[] getFileUploadListeners() {
        return (FileUploadListener[]) getFacesListeners(FileUploadListener.class);
    }

    /**
     * <p>
     * Remove an existing {@link FileUploadListener} (if any) from the set of listeners interested in being notified when
     * {@link FileUploadListener}s occur.
     * </p>
     *
     * @param listener The {@link FileUploadListener} to be removed
     * @throws NullPointerException if <code>listener</code> is <code>null</code>
     */
    public void removeFileUploadListener(FileUploadListener listener) {
        removeFacesListener(listener);
    }

    /**
     * Get a list of accepted types from {@link #getAcceptedTypes()} attribute.
     */
    public List<String> getAcceptedTypesList() {
        String acceptedTypes = this.getAcceptedTypes();
        if (acceptedTypes == null || acceptedTypes.isEmpty()) {
            return Collections.emptyList();
        } else {
            return Arrays.asList(acceptedTypes.toLowerCase().replaceAll("\\.", "").split("\\s*,\\s*"));
        }
    }

    /**
     * Checks whether this component can accept given {@link UploadedFile}..
     *
     * First, the number of enqueued {@link FileUploadEvent} events can't exceed {@link #getMaxFilesQuantity()}.
     *
     * Then, the file extension of uploaded file needs to be acceptable by this component (see {@link #getAcceptedTypes()}).
     */
    public boolean acceptsFile(UploadedFile file) {
        final String clientId = this.getClientId();
        final int maxFilesQuantity = this.getMaxFilesQuantity();
        final List<String> acceptedTypes = this.getAcceptedTypesList();

        if ((maxFilesQuantity > 0) && (queuedFileUploadEvents().get() >= maxFilesQuantity)) {
            return false;
        }

        if (clientId.equals(file.getParameterName())) {
            if (acceptedTypes.isEmpty()) {
                return true;
            }

            if (acceptedTypes.contains(file.getFileExtension().toLowerCase())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Increments number of {@link FileUploadEvent} which were queued
     */
    @Override
    public void queueEvent(FacesEvent event) {
        if (event instanceof FileUploadEvent) {
            queuedFileUploadEvents().addAndGet(1);
        }
        super.queueEvent(event);
    }

    /**
     * Returns a number of {@link FileUploadEvent} which were already queued for this component
     */
    private AtomicInteger queuedFileUploadEvents() {
        AtomicInteger i = (AtomicInteger) this.getAttributes().get(QUEUED_FILE_UPLOAD_EVENTS_ATTR);
        return i;
    }
}
