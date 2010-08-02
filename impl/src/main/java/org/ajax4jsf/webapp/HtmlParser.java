/**
 * License Agreement.
 *
 * Rich Faces - Natural Ajax for Java Server Faces (JSF)
 *
 * Copyright (C) 2007 Exadel, Inc.
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

package org.ajax4jsf.webapp;

import org.w3c.dom.Node;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;

public interface HtmlParser {
    public abstract void parseHtml(InputStream input, Writer output) throws IOException;

    public abstract void parseHtml(Reader input, Writer output) throws IOException;

    /**
     * @param encoding
     */
    public abstract void setInputEncoding(String encoding);

    /**
     * @param encoding
     */
    public abstract void setOutputEncoding(String encoding);

    /**
     * Setup, must tidy move style etc. elements to head or not.
     *
     * @param move
     */
    public abstract void setMoveElements(boolean move);

    public abstract void setHeadNodes(Node[] events);

    public abstract void setDoctype(String doctype);

    /**
     * @param viewState The viewState to set.
     */
    public abstract void setViewState(String viewState);

    public abstract boolean setMime(String mimeType);
}
