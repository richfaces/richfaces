/*
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is XML RenderKit for JSF.
 *
 * The Initial Developer of the Original Code is
 * Orbeon, Inc (info@orbeon.com)
 * Portions created by the Initial Developer are Copyright (C) 2002
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 *
 * Alternatively, the contents of this file may be used under the terms of
 * either the GNU General Public License Version 2 or later (the "GPL"), or
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the MPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the MPL, the GPL or the LGPL.
 */

package org.ajax4jsf.webapp;

import org.ajax4jsf.Messages;
import org.ajax4jsf.io.FastBufferInputStream;
import org.ajax4jsf.io.FastBufferOutputStream;
import org.ajax4jsf.io.FastBufferReader;
import org.ajax4jsf.io.FastBufferWriter;
import org.richfaces.log.RichfacesLogger;
import org.slf4j.Logger;
import org.xml.sax.InputSource;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Base wrapper save JSF page response, for parse to XML with different parsers
 *
 * @author shura (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.1 $ $Date: 2007/01/09 18:58:17 $
 */
public class FilterServletResponseWrapper extends HttpServletResponseWrapper {
    public static final String DEFAULT_ENCODING = "UTF-8";
    private static final Logger LOG = RichfacesLogger.WEBAPP.getLogger();

    protected FastBufferOutputStream byteStream = null;
    
    private int bufferSize = 2048;
    private String charterEncoding = null;
    private String redirectLocation = null;
    private boolean useStream = false;
    private boolean useWriter = false;
    private boolean useNullStream = false;
    private Map<String, Object> headers = new HashMap<String, Object>();
    private boolean error = false;
    private List<Cookie> cookies = new ArrayList<Cookie>();
    private int contentLength = Integer.MIN_VALUE;
    private String contentType;
    private PrintWriter printWriter;
    private ServletOutputStream servletOutputStream;
    private FastBufferWriter stringWriter;

    public FilterServletResponseWrapper(HttpServletResponse response) {
        super(response);
    }

    /**
     * Convert saved context to stream for parsing
     *
     * @return stream from saved content
     */
    public InputStream getContentAsStream() {
        int length = 0;
        FastBufferInputStream result = null;
        String encoding = this.getCharacterEncoding();

//      /        byte[] content;
        if (isUseStream()) {
            try {
                servletOutputStream.flush();
            } catch (IOException ex) {
                LOG.warn(Messages.getMessage(Messages.FLUSH_BUFFERED_STREAM_ERROR), ex);
            }

//          /         content = byteStream.toByteArray();         
            result = new FastBufferInputStream(byteStream);
        } else if (isUseWriter()) {
            printWriter.flush();
            printWriter.close();
            length = stringWriter.getLength();

//          /         String stringContent = stringWriter.toString();
            try {
                FastBufferOutputStream stream = stringWriter.convertToOutputStream(encoding);

                result = new FastBufferInputStream(stream);

//              /             content = stringContent.getBytes(encoding);
            } catch (UnsupportedEncodingException e) {

                // TODO Auto-generated catch block
                LOG.warn(Messages.getMessage(Messages.UNSUPPORTED_ENCODING_WARNING));

                FastBufferOutputStream stream = stringWriter.convertToOutputStream();

                result = new FastBufferInputStream(stream);

//              /             content = stringContent.getBytes();
            }
        } else {
            if (LOG.isDebugEnabled()) {
                LOG.debug(Messages.getMessage(Messages.NO_WRITER_CALLED_INFO));
            }

            return null;
        }

//      /            if (content.length > 0) {
        if (length > 0) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(Messages.getMessage(Messages.PARSE_XML_DOCUMENT_INFO));

//              try {
//                LOG.debug(new String(content, encoding));
//                  }catch (UnsupportedEncodingException e) {
//                  }
            }
        }

        return result;

//      /            return new ByteArrayInputStream(content);
    }

    /**
     * Convert saved context to stream for parsing
     *
     * @return stream from saved content
     */
    public Reader getContentAsReader() {
        FastBufferReader result = null;
        String encoding = this.getCharacterEncoding();

//      /        String content;
        if (isUseWriter()) {
            printWriter.flush();
            printWriter.close();
            result = new FastBufferReader(stringWriter);

//          /         content = stringWriter.toString();
        } else if (isUseStream()) {
            try {
                servletOutputStream.flush();
            } catch (IOException ex) {
                LOG.warn(Messages.getMessage(Messages.FLUSH_BUFFERED_STREAM_ERROR), ex);
            }

            try {
                FastBufferWriter writer = byteStream.convertToWriter(encoding);

                result = new FastBufferReader(writer);

//              /             content = byteStream.toString(encoding);
            } catch (UnsupportedEncodingException e) {
                LOG.warn(Messages.getMessage(Messages.UNSUPPORTED_ENCODING_WARNING_2), e);

                FastBufferWriter writer = byteStream.convertToWriter();

                result = new FastBufferReader(writer);

//              /             content = byteStream.toString();
            }
        } else {
            if (LOG.isDebugEnabled()) {
                LOG.debug(Messages.getMessage(Messages.NO_WRITER_CALLED_INFO));
            }

            return null;
        }

//      if (content.length() > 0) {
//          if (LOG.isDebugEnabled()) {
//              LOG.debug("XML document to parse in filter, return as Reader: ");
//                LOG.debug(content);
//          }
//      }
///      return new StringReader(content);
        return result;
    }

    public PrintWriter getWriter() throws IOException {
        if (useStream) {
            throw new IllegalStateException(Messages.getMessage(Messages.NO_WRITER_POSSIBLE_ERROR));
        }

        if (printWriter == null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(Messages.getMessage(Messages.CREATE_WRITER_INFO));
            }

            printWriter = new PrintWriter(new ServletStringWriter());
            useWriter = true;
        }

        return printWriter;
    }

    public ServletOutputStream getOutputStream() throws IOException {
        if (useWriter) {
            if (isUseNullStream()) {
                return new NullServletOutputStream();
            }

            throw new IllegalStateException(Messages.getMessage(Messages.NO_STREAM_POSSIBLE_ERROR));
        }

        if (servletOutputStream == null) {

//          byteStream = new ByteArrayOutputStream();
            servletOutputStream = new ByteArrayServletOutputStream();
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug(Messages.getMessage(Messages.CREATE_STREAM_INFO));
        }

        useStream = true;

        return servletOutputStream;
    }

//  public boolean isCommitted() {
//    // NOTE: What makes sense here?
//    return false;
//  }

    /**
     * Return a charset from a content-type.
     */
    public static String getContentTypeCharset(String contentType) {
        if (contentType == null) {
            return null;
        }

        int semicolumnIndex = contentType.indexOf(";");

        if (semicolumnIndex == -1) {
            return null;
        }

        int charsetIndex = contentType.indexOf("charset=", semicolumnIndex);

        if (charsetIndex == -1) {
            return null;
        }

        // FIXME: There may be other attributes after charset, right?
        String afterCharset = contentType.substring(charsetIndex + 8);

        afterCharset = afterCharset.replace('"', ' ');

        return afterCharset.trim();
    }

    /*
     *  (non-Javadoc)
     * @see javax.servlet.http.HttpServletResponse#addHeader(java.lang.String, java.lang.String)
     */
    public void addHeader(String name, String value) {
        if ("Content-Type".equals(name)) {
            setContentType(value);
        } else {
            this.headers.put(name, value);
            super.addHeader(name, value);
        }
    }

    /*
     *  (non-Javadoc)
     * @see javax.servlet.http.HttpServletResponse#setHeader(java.lang.String, java.lang.String)
     */
    public void setHeader(String name, String value) {

        // HACK - weblogic do not use setContentType, instead directly set header !
        if ("Content-Type".equals(name)) {
            setContentType(value);
        } else {
            this.headers.put(name, value);
            super.setHeader(name, value);
        }
    }

    public Map<String, Object> getHeaders() {
        return this.headers;
    }

    /*
     *  (non-Javadoc)
     * @see javax.servlet.ServletResponse#getCharacterEncoding()
     */
    public String getCharacterEncoding() {

//      return this.charterEncoding;
        if (null != this.charterEncoding) {
            return this.charterEncoding;
        } else {

            // For cases of implicit setting
            return super.getCharacterEncoding();
        }
    }

    /*
     *  (non-Javadoc)
     * @see javax.servlet.ServletResponse#setCharacterEncoding(java.lang.String)
     */
    public void setCharacterEncoding(String charset) {
        this.charterEncoding = charset;

        // super.setCharacterEncoding(charset);
    }

    public void setContentType(String type) {
        String contentTypeCharset = getContentTypeCharset(type);

        if (null != contentTypeCharset) {
            this.charterEncoding = contentTypeCharset;
        }

        this.contentType = type;

//      super.setContentType(type);
    }

    public void setLocale(Locale loc) {
        super.setLocale(loc);
    }

    public String getContentType() {
        return contentType;
    }

    /**
     * Send buffered content directly to output stream.
     *
     * @param outputStream
     * @throws IOException
     */
    public void sendContent(ServletOutputStream outputStream) throws IOException {
        if (getContentLength() > 0) {
            super.setContentLength(getContentLength());
        }

        if (isUseWriter()) {
            printWriter.flush();
            printWriter.close();
            stringWriter.printTo(outputStream);

//          /         outputStream.print(stringWriter.toString());
        } else if (isUseStream()) {
            try {
                servletOutputStream.flush();
            } catch (IOException ex) {
                LOG.warn(Messages.getMessage(Messages.FLUSH_BUFFERED_STREAM_ERROR), ex);
            }

            byteStream.writeTo(outputStream);
        }
    }

    /**
     * Send buffered content directly to output stream.
     *
     * @param outputStream
     * @throws IOException
     */
    public void sendContent(Writer output) throws IOException {
        if (getContentLength() > 0) {
            super.setContentLength(getContentLength());
        }

        if (isUseWriter()) {
            printWriter.flush();
            printWriter.close();
            stringWriter.writeTo(output);

//          /         outputStream.print(stringWriter.toString());
        } else if (isUseStream()) {
            try {
                servletOutputStream.flush();
            } catch (IOException ex) {
                LOG.warn(Messages.getMessage(Messages.FLUSH_BUFFERED_STREAM_ERROR), ex);
            }

            byteStream.writeTo(output, getCharacterEncoding());
        }
    }

    /**
     * @return Returns the useStream.
     */
    public boolean isUseStream() {
        return this.useStream;
    }

    /**
     * @return Returns the useWriter.
     */
    public boolean isUseWriter() {
        return this.useWriter;
    }

    /**
     * @return
     */
    public InputSource getContentAsInputSource() {

        // Create InputSource
        InputSource inputSource = null;
        String encoding = this.getCharacterEncoding();

        if (isUseWriter()) {
            inputSource = new InputSource(getContentAsReader());
        } else if (isUseStream()) {
            inputSource = new InputSource(getContentAsStream());

            if (encoding != null) {
                inputSource.setEncoding(encoding);
            }
        } else {
            if (LOG.isDebugEnabled()) {
                LOG.debug(Messages.getMessage(Messages.NO_WRITER_CALLED_INFO));
            }

            return null;
        }

        return inputSource;
    }

    /*
     *  (non-Javadoc)
     * @see javax.servlet.ServletResponseWrapper#flushBuffer()
     */
    public void flushBuffer() throws IOException {
        if (isUseStream()) {
            servletOutputStream.flush();
        } else if (isUseWriter()) {
            printWriter.flush();
        }
    }

    /*
     *  (non-Javadoc)
     * @see javax.servlet.ServletResponseWrapper#getBufferSize()
     */
    public int getBufferSize() {

        // TODO Auto-generated method stub
        return bufferSize;
    }

    /*
     *  (non-Javadoc)
     * @see javax.servlet.ServletResponseWrapper#resetBuffer()
     */
    public void resetBuffer() {
        this.printWriter = null;
        this.stringWriter = null;
        this.servletOutputStream = null;
        this.byteStream = null;
        this.useStream = false;
        this.useWriter = false;
    }

    /*
     *  (non-Javadoc)
     * @see javax.servlet.ServletResponseWrapper#setBufferSize(int)
     */
    public void setBufferSize(int arg0) {

        // TODO Auto-generated method stub
        bufferSize = arg0;
    }

    /*
     *  (non-Javadoc)
     * @see javax.servlet.ServletResponseWrapper#reset()
     */
    public void reset() {

        // TODO Auto-generated method stub
        this.resetBuffer();
        this.headers = new HashMap<String, Object>();
        this.contentType = null;
        this.charterEncoding = null;
        super.reset();
    }

    /**
     * @return Returns the contentLength.
     */
    public int getContentLength() {
        return contentLength;
    }

    /**
     * @param contentLength The contentLength to set.
     */
    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    /**
     * Parse saved content to output stream ( and store as Dom Document tree )
     *
     * @param out stream to send parsed content, if null - only build dom tree.
     * @return parsed document as dom tree.
     * @throws IOException
     */
    public void parseContent(Writer out, HtmlParser parser) throws IOException {
        parser.setInputEncoding(getCharacterEncoding());

        if (isUseWriter()) {
            parser.parseHtml(getContentAsReader(), out);
        } else if (isUseStream()) {
            parser.parseHtml(getContentAsStream(), out);
        }
    }

    public void sendRedirect(String arg0) throws IOException {

        // TODO Auto-generated method stub
        this.redirectLocation = arg0;
    }

    public String getRedirectLocation() {
        return redirectLocation;
    }

    /**
     * @return the useNullStream
     */
    public boolean isUseNullStream() {
        return useNullStream;
    }

    /**
     * @param useNullStream the useNullStream to set
     */
    public void setUseNullStream(boolean useNullStream) {
        this.useNullStream = useNullStream;
    }

    public Collection<Cookie> getCookies() {
        return cookies;
    }

    public void addCookie(Cookie cookie) {
        cookies.add(cookie);
        super.addCookie(cookie);
    }

    public void sendError(int sc) throws IOException {
        this.error = true;
        super.sendError(sc);
    }

    public void sendError(int sc, String msg) throws IOException {
        this.error = true;
        super.sendError(sc, msg);
    }

    public boolean isError() {
        return error;
    }

    public class ByteArrayServletOutputStream extends ServletOutputStream {
        private boolean opened = true;

        public ByteArrayServletOutputStream() {
            byteStream = new FastBufferOutputStream(bufferSize);
        }

        /*
         *  (non-Javadoc)
         * @see java.io.OutputStream#close()
         */
        public void close() throws IOException {
            flush();
            opened = false;
        }

        /*
         *  (non-Javadoc)
         * @see java.io.OutputStream#write(byte[])
         */
        public void write(byte[] b) throws IOException {

            // TODO Auto-generated method stub
            if (opened) {
                byteStream.write(b);
            }
        }

        public void write(int b) throws IOException {
            if (opened) {
                byteStream.write(b);
            }
        }

        /*
         *  (non-Javadoc)
         * @see java.io.OutputStream#write(byte[], int, int)
         */
        public void write(byte[] b, int off, int len) throws IOException {
            if (opened) {
                byteStream.write(b, off, len);
            }
        }
    }

    public static class NullServletOutputStream extends ServletOutputStream {

        /*
         *  (non-Javadoc)
         * @see java.io.OutputStream#write(byte[], int, int)
         */
        public void write(byte[] b, int off, int len) throws IOException {

            // this-is null stream, do nothing !
        }

        /*
         *  (non-Javadoc)
         * @see java.io.OutputStream#write(byte[])
         */
        public void write(byte[] b) throws IOException {

            // this-is null stream, do nothing !
        }

        public void write(int b) throws IOException {

            // this-is null stream, do nothing !
        }
    }

    public class ServletStringWriter extends Writer {
        private boolean opened = true;

        public ServletStringWriter() {
            super();
            stringWriter = new FastBufferWriter(bufferSize);
        }

        public void write(char[] cbuf, int off, int len) throws IOException {
            if (opened) {
                stringWriter.write(cbuf, off, len);
            }
        }

        /*
         *  (non-Javadoc)
         * @see java.io.Writer#write(char[])
         */
        public void write(char[] cbuf) throws IOException {

            // TODO Auto-generated method stub
            if (opened) {
                stringWriter.write(cbuf);
            }
        }

        /*
         *  (non-Javadoc)
         * @see java.io.Writer#write(int)
         */
        public void write(int c) throws IOException {

            // TODO Auto-generated method stub
            if (opened) {
                stringWriter.write(c);
            }
        }

        public void flush() throws IOException {
            stringWriter.flush();
        }

        public void close() throws IOException {
            stringWriter.close();
            opened = false;
        }
    }
}
