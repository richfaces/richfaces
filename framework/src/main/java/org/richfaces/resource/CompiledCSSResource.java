/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
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
package org.richfaces.resource;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;

import javax.faces.application.ProjectStage;
import javax.faces.application.Resource;
import javax.faces.context.FacesContext;

import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;
import org.richfaces.resource.css.CSSVisitorImpl;
import org.richfaces.skin.Skin;
import org.richfaces.skin.SkinFactory;
import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.CSSParseException;
import org.w3c.css.sac.ErrorHandler;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.css.CSSStyleSheet;

import com.steadystate.css.parser.CSSOMParser;
import com.steadystate.css.parser.SACParserCSS3;

/**
 * @author amarkhel Class, that represented dynamic CSS resource.
 */
public class CompiledCSSResource extends AbstractCacheableResource implements StateHolderResource {
    private static final Logger LOGGER = RichfacesLogger.RESOURCE.getLogger();
    private static final String NULL_STYLESHEET = "Parsed stylesheet for ''{0}'':''{1}'' resource is null.";
    // TODO handle sourceResources headers, etc.
    private Resource sourceResource;

    public CompiledCSSResource(Resource sourceResource) {
        assert sourceResource != null;

        this.sourceResource = sourceResource;
    }

    @Override
    public String getLibraryName() {
        return sourceResource.getLibraryName();
    }

    @Override
    public void setLibraryName(String libraryName) {
        sourceResource.setLibraryName(libraryName);
    }

    @Override
    public String getResourceName() {
        return sourceResource.getResourceName();
    }

    @Override
    public void setResourceName(String resourceName) {
        sourceResource.setResourceName(resourceName);
    }

    public InputStream getResourceInputStream() throws IOException {
        return sourceResource.getInputStream();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        FacesContext ctx = FacesContext.getCurrentInstance();
        InputStream stream = null;
        CSSStyleSheet styleSheet = null;
        try {
            stream = getResourceInputStream();
            if (null == stream) {
                return null;
            }
            InputSource source = new InputSource(new InputStreamReader(stream));
            CSSOMParser parser = new CSSOMParser(new SACParserCSS3());
            ErrorHandlerImpl errorHandler = new ErrorHandlerImpl(this, ctx.isProjectStage(ProjectStage.Production));

            parser.setErrorHandler(errorHandler);

            // parse and create a stylesheet composition
            styleSheet = parser.parseStyleSheet(source, null, null);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    LOGGER.warn(e.getMessage(), e);
                }
            }
        }

        if (styleSheet != null) {
            // TODO nick - handle encoding
            String encoding = ctx.getExternalContext().getResponseCharacterEncoding();
            CSSVisitorImpl cssVisitor = new CSSVisitorImpl(ctx);
            cssVisitor.setEncoding(encoding != null ? encoding : "UTF-8");
            cssVisitor.visitStyleSheet(styleSheet);

            String cssText = cssVisitor.getCSSText();

            return new ByteArrayInputStream(cssText.getBytes(cssVisitor.getEncoding()));
        } else {
            if (!ctx.isProjectStage(ProjectStage.Production)) {
                LOGGER.info(MessageFormat.format(NULL_STYLESHEET, getLibraryName(), getResourceName()));
            }
            return null;
        }
    }

    @Override
    public String getContentType() {
        return "text/css";
    }

    private static int getSkinHashCode(FacesContext context) {
        Skin skin = SkinFactory.getInstance(context).getSkin(context);
        return skin.hashCode(context);
    }

    @Override
    public boolean userAgentNeedsUpdate(FacesContext context) {
        // TODO nick - review
        // return resourceDelegate.userAgentNeedsUpdate(context);

        if (context.isProjectStage(ProjectStage.Development)) {
            return true;
        }
        return super.userAgentNeedsUpdate(context);
    }

    private static final class ErrorHandlerImpl implements ErrorHandler {
        // TODO nick - sort out logging between stages
        private boolean productionStage;
        private Resource resource;
        private String resourceLocator;

        public ErrorHandlerImpl(Resource resource, boolean productionStage) {
            super();
            this.resource = resource;
            this.productionStage = productionStage;
        }

        private String getResourceLocator() {
            if (resourceLocator == null) {
                String libraryName = resource.getLibraryName();
                String resourceName = resource.getResourceName();

                if (libraryName != null && libraryName.length() != 0) {
                    resourceLocator = libraryName + '/' + resourceName;
                } else {
                    resourceLocator = resourceName;
                }
            }

            return resourceLocator;
        }

        private void logException(CSSParseException e) {
            String formattedMessage = MessageFormat.format("Problem parsing ''{0}'' resource: {1}", getResourceLocator(),
                e.getMessage());

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(formattedMessage, e);
            } else {
                LOGGER.warn(formattedMessage);
            }
        }

        public void error(CSSParseException e) throws CSSException {
            logException(e);
        }

        public void fatalError(CSSParseException e) throws CSSException {
            logException(e);
        }

        public void warning(CSSParseException e) throws CSSException {
            logException(e);
        }
    }

    public boolean isTransient() {
        return false;
    }

    public void readState(FacesContext context, DataInput dataInput) throws IOException {
        // do nothing
    }

    public void writeState(FacesContext context, DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(getSkinHashCode(context));
    }
}