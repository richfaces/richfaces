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

import java.util.Map;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ListenerFor;
import javax.faces.event.PostAddToViewEvent;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.event.FileUploadListener;
import org.richfaces.renderkit.RenderKitUtils;

/**
 * @author Konstantin Mishin
 * 
 */
@JsfComponent(tag = @Tag(handler = "org.richfaces.view.facelets.FileUploadHandler"),
    renderer = @JsfRenderer(type = "org.richfaces.FileUploadRenderer"),
    attributes = {"events-props.xml", "core-props.xml", "ajax-props.xml", "i18n-props.xml"})
@ListenerFor(systemEventClass = PostAddToViewEvent.class)
public abstract class AbstractFileUpload extends UIComponentBase {
    
    @Attribute
    public abstract String getAcceptedTypes();

    @Attribute(defaultValue = "false")
    public abstract boolean isDisabled();

    @Attribute(defaultValue = "false")
    public abstract boolean isNoDuplicate();

    @Attribute(events = @EventName("filesubmit"))
    public abstract String getOnfilesubmit();

    @Attribute(events = @EventName("uploadcomplete"))
    public abstract String getOnuploadcomplete();

    @Override
    public void processEvent(ComponentSystemEvent event) throws AbortProcessingException {
        super.processEvent(event);
        FacesContext context = getFacesContext();
        Map<String, UIComponent> facets = getFacets();
        UIComponent component = facets.get("progress");
        if (component == null) {
            try {
                component = context.getApplication().createComponent(context, "org.richfaces.ProgressBar",
                    "org.richfaces.ProgressBarRenderer");
            } catch (FacesException e) {
                // To work without ProgressBar.
            }
            if (component != null) {
                component.setId(getId() + "_pb");
                facets.put("progress", component);
            }
        }
        if (component != null) {
            String resourcePath = RenderKitUtils.getResourcePath(context, "org.richfaces", "fileUploadProgress");
            component.getAttributes().put("resource", resourcePath);
        }
    }
    
    /**
     * <p>Add a new {@link FileUploadListener} to the set of listeners
     * interested in being notified when {@link org.richfaces.FileUploadEvent}s occur.</p>
     *
     * @param listener The {@link FileUploadListener} to be added
     * @throws NullPointerException if <code>listener</code>
     *                              is <code>null</code>
     */
    public void addFileUploadListener(FileUploadListener listener) {
        addFacesListener(listener);
    }

    /**
     * <p>Return the set of registered {@link FileUploadListener}s for this
     * {@link AbstractFileUpload} instance.  If there are no registered listeners,
     * a zero-length array is returned.</p>
     */
    public FileUploadListener[] getFileUploadListeners() {
        return (FileUploadListener[]) getFacesListeners(FileUploadListener.class);
    }

    /**
     * <p>Remove an existing {@link FileUploadListener} (if any) from the
     * set of listeners interested in being notified when
     * {@link FileUploadListener}s occur.</p>
     *
     * @param listener The {@link FileUploadListener} to be removed
     * @throws NullPointerException if <code>listener</code>
     *                              is <code>null</code>
     */
    public void removeFileUploadListener(FileUploadListener listener) {
        removeFacesListener(listener);
    }
}