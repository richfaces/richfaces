/**
 * License Agreement.
 *
 *  JBoss RichFaces - Ajax4jsf Component Library
 *
 * Copyright (C) 2007  Exadel, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */



package org.richfaces.renderkit;

import java.io.StringWriter;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.context.ResponseWriter;

import org.jboss.test.faces.AbstractFacesTest;

/**
 * Created 26.10.2007
 * @author Nick Belaevski - mailto:nbelaevski@exadel.com
 * @since 3.2
 */
public class TemplateEncoderRendererBaseTest extends AbstractFacesTest {
    @Override
    public void setUp() throws Exception {
        super.setUp();
        setupFacesRequest();
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testEncodeNonRendered() throws Exception {
        ResponseWriter responseWriter = facesContext.getRenderKit().createResponseWriter(new StringWriter(),
                                            "text/html", "UTF-8");

        facesContext.setResponseWriter(responseWriter);

        TemplateEncoderRendererBase rendererBase = new TemplateEncoderRendererBase() {
            @Override
            protected Class<? extends UIComponent> getComponentClass() {
                return UIOutput.class;
            }
        };
        UIOutput output = new UIOutput();
        UIOutput c = new UIOutput();

        c.setRendered(false);
        c.setValue("");
        output.getChildren().add(c);

        // that should not fail
        rendererBase.writeScriptBody(facesContext, c, true);
    }

    public void testEncode() throws Exception {
        StringWriter controlWriter = new StringWriter();
        ResponseWriter responseWriter = facesContext.getRenderKit().createResponseWriter(controlWriter, "text/html",
                                            "UTF-8");

        facesContext.setResponseWriter(responseWriter);

        TemplateEncoderRendererBase rendererBase = new TemplateEncoderRendererBase() {
            @Override
            protected Class<? extends UIComponent> getComponentClass() {
                return UIOutput.class;
            }
        };
        HtmlPanelGroup c = new HtmlPanelGroup();

        for (int i = 0; i < 3; i++) {
            HtmlPanelGroup c1 = new HtmlPanelGroup();

            c1.setId("panel" + i);

            UIOutput text = new UIOutput();

            text.setValue("some text");
            c1.getChildren().add(text);

            UIOutput text2 = new UIOutput();

            text2.setValue("some text");
            c.getChildren().add(text2);
            c.getChildren().add(c1);
        }

        rendererBase.writeScriptBody(facesContext, c, false);
        System.out.println(controlWriter.toString());
    }
}
