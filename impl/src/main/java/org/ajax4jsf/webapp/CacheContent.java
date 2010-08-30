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

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.faces.context.ExternalContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.ajax4jsf.io.FastBufferOutputStream;
import org.ajax4jsf.io.FastBufferWriter;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;
import org.richfaces.util.Util;

/**
 * @author shura (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.1 $ $Date: 2007/01/09 18:58:20 $
 */
public class CacheContent implements Serializable {
    private static final long serialVersionUID = 8120940486890871177L;
    private static final Logger LOG = RichfacesLogger.WEBAPP.getLogger();

    boolean filledOutputWriter = false;
    boolean filledOutputStream = false;

    // content to send.
    private byte[] content = null;
    private String writerContent = null;
    private Map<String, Object> headers = new HashMap<String, Object>();
    private Integer contentLength;
    private String contentType;

    // private transient ByteArrayOutputStream outputStream ;
    private transient FastBufferOutputStream outputStream;
    private transient ServletOutputStream servletStream;
    private transient PrintWriter servletWriter;
    private transient FastBufferWriter stringOutputWriter;

    @Deprecated
    public void send(HttpServletResponse response) throws IOException {
        throw new UnsupportedOperationException();
    }

    /**
     * Send saved content to http responce
     *
     * @param response
     * @throws IOException
     */
    public void send(ExternalContext externalContext) throws IOException {
        if (filledOutputStream) {
            OutputStream out = externalContext.getResponseOutputStream();

            if (content != null) {
                out.write(content);
            } else {
                this.outputStream.writeTo(out);
            }

            // out.flush();
            // out.close();
        } else if (filledOutputWriter) {
            Writer out = externalContext.getResponseOutputWriter();

            if (null != writerContent) {
                out.write(writerContent);
            } else {
                stringOutputWriter.writeTo(out);
            }

            // out.flush();
            // out.close();
        }
    }

    /**
     * Send saved headers to http responce.
     *
     * @param response
     */
    public void sendHeaders(ExternalContext externalContext) {

        // set real content-length.
        // / if (null != content) {
        int realContentLength = 0;

        if (filledOutputStream) {
            if ((null != content) && (content.length > 0)) {
                realContentLength = content.length;
            } else if ((null != outputStream) && (outputStream.getLength() > 0)) {
                realContentLength = outputStream.getLength();
            }
        } // TODO - calculate content-lenght for writer ?

        if ((realContentLength <= 0) && (contentLength != null)) {
            realContentLength = contentLength.intValue();
        }

        for (Iterator<Entry<String, Object>> iter = headers.entrySet().iterator(); iter.hasNext();) {
            Entry<String, Object> element = iter.next();
            String header = (String) element.getKey();
            Object headerValue = element.getValue();

            try {
                if (headerValue instanceof Long) {
                    Long time = (Long) headerValue;
                    String formattedDate = Util.formatHttpDate(time);

                    externalContext.setResponseHeader(header, formattedDate);
                } else if (headerValue instanceof Integer) {
                    Integer value = (Integer) headerValue;

                    // Check real content length.
                    if ("Content-Length".equals(header)) {
                        if (realContentLength <= 0) {
                            realContentLength = value.intValue();
                        } else {

                            // do nothing
                        }
                    } else {
                        externalContext.setResponseHeader(header, value.toString());
                    }
                } else {

                    // Don't send "chunked" transfer-encoding type with real content-length
                    if (!((realContentLength > 0) && "Transfer-Encoding".equals(header)
                        && "chunked".equals(headerValue))) {
                        externalContext.setResponseHeader(header, (String) headerValue);
                    }
                }
            } catch (Exception e) {
                LOG.error("Error set response header " + header + "for value " + headerValue, e);
            }

            if (realContentLength > 0) {
                externalContext.setResponseContentLength(realContentLength);
            }

            if (null != contentType) {
                externalContext.setResponseContentType(this.contentType);
            }
        }
    }

    @Deprecated
    public void sendHeaders(HttpServletResponse response) {
        throw new UnsupportedOperationException();
    }

    public void setDateHeader(String name, long value) {

        // Expires not stored in cache - must be re-calculated for every
        // response.
        if (!"Expires".equals(name)) {
            headers.put(name, new Long(value));
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ajax4jsf.framework.resource.ResourceContext#setHeader(java.lang.String,
     *      java.lang.String)
     */
    public void setHeader(String name, String value) {
        headers.put(name, value);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ajax4jsf.framework.resource.ResourceContext#setIntHeader(java.lang.String,
     *      int)
     */
    public void setIntHeader(String name, int value) {
        headers.put(name, new Integer(value));
    }

    /**
     * Create UNIX command 'tee' like stream - send all data to servlet
     *
     * @param responseStream
     * @return
     */
    public OutputStream getOutputStream() {
        if (null == servletStream) {
            outputStream = new FastBufferOutputStream(1024);
            servletStream = new ServletOutputStream() {
                @Override
                public void close() {
                    filledOutputStream = true;
                    content = null;
                }

                @Override
                public void flush() {
                }

                @Override
                public void write(byte[] b, int off, int len) throws IOException {
                    outputStream.write(b, off, len);
                }

                @Override
                public void write(byte[] b) throws IOException {
                    outputStream.write(b);
                }

                @Override
                public void write(int b) throws IOException {
                    outputStream.write(b);
                }
            };
        }

        return servletStream;
    }

    public PrintWriter getWriter() {
        if (null == servletWriter) {
            stringOutputWriter = new FastBufferWriter(1024);

            Writer out = new Writer() {
                @Override
                public void write(char[] cbuf, int off, int len) throws IOException {
                    stringOutputWriter.write(cbuf, off, len);
                }

                @Override
                public void flush() throws IOException {
                }

                @Override
                public void close() throws IOException {
                    filledOutputWriter = true;
                    writerContent = null;
                }
            };

            servletWriter = new PrintWriter(out);
        }

        return servletWriter;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * @return Returns the contentType.
     */
    public String getContentType() {
        return contentType;
    }

    private void writeObject(java.io.ObjectOutputStream s) throws IOException {
        if (filledOutputStream) {
            if (outputStream != null) {
                content = outputStream.toByteArray();
            }
        } else if (filledOutputWriter) {
            if (stringOutputWriter != null) {
                char[] cs = stringOutputWriter.toCharArray();

                writerContent = new String(cs);
            }
        }

        s.defaultWriteObject();
    }

    @Deprecated
    public void flush(HttpServletResponse response) throws IOException {
        throw new UnsupportedOperationException();
    }

    /**
     * Flush used output writer/stream, to fix https://jira.jboss.org/jira/browse/RF-4017
     *
     * @param response
     * @throws IOException
     */
    public void flush(ExternalContext externalContext) throws IOException {
        if (filledOutputStream) {
            OutputStream out = externalContext.getResponseOutputStream();

            out.flush();
        } else if (filledOutputWriter) {
            Writer out = externalContext.getResponseOutputWriter();

            out.flush();
        }
    }

    public int getContentLength() {
        if (contentLength == null) {
            throw new IllegalStateException("Content length hasn't been set yet!");
        }

        return contentLength.intValue();
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }
}
