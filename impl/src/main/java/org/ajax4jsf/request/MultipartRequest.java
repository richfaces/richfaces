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

package org.ajax4jsf.request;

import org.ajax4jsf.exception.FileUploadException;
import org.ajax4jsf.webapp.BaseXMLFilter;
import org.richfaces.component.FileUploadConstants;
import org.richfaces.model.UploadItem;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Request wrapper for supporting multipart requests, used for file uploading.
 *
 * @author Shane Bryzak
 */
public class MultipartRequest extends HttpServletRequestWrapper {
    private static final int BUFFER_SIZE = 2048;
    private static final int CHUNK_SIZE = 512;
    private static final byte CR = 0x0d;
    private static final byte LF = 0x0a;
    private static final String PARAM_CONTENT_TYPE = "Content-Type";
    private static final String PARAM_FILENAME = "filename";
    private static final String PARAM_NAME = "name";
    private static final byte[] CR_LF = {CR, LF};
    private static final Pattern PARAM_VALUE_PATTERN = Pattern.compile("^\\s*([^\\s=]+)\\s*[=:]\\s*(.+)\\s*$");
    private static final Pattern FILE_NAME_PATTERN = Pattern.compile(".*filename=\"(.*)\"");
    boolean initialized = false;
    private int bytesRead = 0;
    private Integer contentLength = 0;
    private String encoding = null;
    private Map<String, Param> parameters = null;
    private Map<String, Object> percentMap = null;
    private int pos = 0;
    private int read = 0;

    // we shouldn't allow to stop until request reaches PhaseListener because of portlets
    private boolean canStop = false;
    private String requestKey = null;
    private Map<String, String> requestKeysMap = null;
    private Map<String, Integer> requestSizeMap = null;
    private List<String> keys = new ArrayList<String>();
    private boolean shouldStop = false;
    private int zeroReadAttempts = 20; // 20 attempts to read not-readable data
    private byte[] boundaryMarker;
    private byte[] buffer;
    private boolean canceled;
    private boolean createTempFiles;
    private InputStream input;
    private ReadState readState;
    private MultipartRequestRegistry requestRegistry;
    private String uid;

    private enum ReadState {
        BOUNDARY, HEADERS, DATA
    }

    public MultipartRequest(HttpServletRequest request, boolean createTempFiles, int maxRequestSize, String uid) {
        super(request);
        this.createTempFiles = createTempFiles;
        this.uid = uid;

        String contentLength = request.getHeader("Content-Length");

        this.contentLength = Integer.parseInt(contentLength);

        if ((contentLength != null) && (maxRequestSize > 0) && (this.contentLength > maxRequestSize)) {

            // TODO : we should make decision if can generate exception in this
            // place
            // throw new FileUploadException(
            // "Multipart request is larger than allowed size");
        }
    }

    private String decodeFileName(String name) {
        String fileName = null;

        try {
            if (getRequest().getParameter("_richfaces_send_http_error") != null) {
                fileName = new String(name.getBytes(encoding), "UTF-8");
            } else {
                StringBuffer buffer = new StringBuffer();
                String[] codes = name.split(";");

                if (codes != null) {
                    for (String code : codes) {
                        if (code.startsWith("&")) {
                            String sCode = code.replaceAll("[&#]*", "");
                            Integer iCode = Integer.parseInt(sCode);

                            buffer.append(Character.toChars(iCode));
                        } else {
                            buffer.append(code);
                        }
                    }

                    fileName = buffer.toString();
                }
            }
        } catch (Exception e) {
            fileName = name;
        }

        return fileName;
    }

    public void cancel() {
        this.canceled = true;

        if (parameters != null) {
            Iterator<Param> it = parameters.values().iterator();

            while (it.hasNext()) {
                Param p = it.next();

                if (p instanceof FileParam) {
                    ((FileParam) p).deleteFile();
                }
            }
        }
    }

    private void fillBuffer() throws IOException {
        if (pos < read) {

            // move the bytes that weren't read to the start of
            // the
            // buffer
            int bytesNotRead = read - pos;

            if (bytesNotRead != buffer.length) {
                System.arraycopy(buffer, pos, buffer, 0, bytesNotRead);
                read = input.read(buffer, bytesNotRead, buffer.length - bytesNotRead);

                if ((read != 0) || (--zeroReadAttempts != 0)) {
                    if (read > 0) {
                        bytesRead += read;
                    }

                    read += bytesNotRead;
                } else {

                    // read is already zero
                    // read = 0;
                }
            } else {
                read = bytesNotRead;
            }
        } else {
            read = input.read(buffer);

            if (read > 0) {
                bytesRead += read;
            }
        }

        fillProgressInfo();
        pos = 0;
    }

    private void readNext() throws IOException {
        Param p = readHeader();

        readData(p);
    }

    private void readData(Param p) throws IOException {
        int localRead = this.read;

        while (localRead > 0) {
            for (int i = this.pos; i < localRead; i++) {

                // If we've encountered another boundary...
                if (checkSequence(buffer, i - boundaryMarker.length - CR_LF.length, CR_LF)
                    && checkSequence(buffer, i, boundaryMarker)) {

                    // Write any data before the boundary (that
                    // hasn't
                    // already been written) to the param
                    if (pos < i - boundaryMarker.length - CR_LF.length - 1) {
                        p.appendData(buffer, pos, i - pos - boundaryMarker.length - CR_LF.length - 1);
                    }

                    if (p instanceof ValueParam) {
                        ((ValueParam) p).complete();
                    }

                    if (checkSequence(buffer, i + CR_LF.length, CR_LF)) {
                        i += CR_LF.length;
                        pos = i + 1;
                    } else {
                        pos = i;
                    }

                    readState = ReadState.HEADERS;

                    break;
                } else if (i > (pos + boundaryMarker.length + CHUNK_SIZE + CR_LF.length)) {

                    // Otherwise write whatever data we have to the param
                    p.appendData(buffer, pos, CHUNK_SIZE);
                    pos += CHUNK_SIZE;
                }
            }

            if (ReadState.DATA.equals(readState)) {
                fillBuffer();
                localRead = this.read;
            } else {
                break;
            }
        }
    }

    private Param readHeader() throws IOException {
        Param p = null;
        Map<String, String> headers = new HashMap<String, String>();
        int localRead = this.read;
        ByteArrayOutputStream baos = new ByteArrayOutputStream(2048);

        while (localRead > 0) {
            for (int i = this.pos; i < localRead; i++) {
                if (checkSequence(buffer, i, CR_LF)) {
                    baos.write(buffer, pos, i - pos - 1);

                    String param = (encoding == null)
                        ? new String(baos.toByteArray()) : new String(baos.toByteArray(), encoding);

                    parseParams(param, "; ", headers);

                    if (checkSequence(buffer, i + CR_LF.length, CR_LF)) {
                        readState = ReadState.DATA;
                        i += CR_LF.length;
                        pos = i + 1;

                        String paramName = headers.get(PARAM_NAME);

                        if (paramName != null) {
                            if (headers.containsKey(PARAM_FILENAME)) {
                                FileParam fp = new FileParam(paramName);

                                if (createTempFiles) {
                                    fp.createTempFile();
                                }

                                fp.setContentType(headers.get(PARAM_CONTENT_TYPE));
                                fp.setFilename(decodeFileName(headers.get(PARAM_FILENAME)));
                                p = fp;
                            } else {
                                if (parameters.containsKey(paramName)) {
                                    p = parameters.get(paramName);
                                } else {
                                    p = new ValueParam(paramName);
                                }
                            }

                            if (!parameters.containsKey(paramName)) {
                                parameters.put(paramName, p);
                            }
                        }

                        headers.clear();
                        baos.reset();

                        break;
                    } else {
                        pos = i + 1;
                        baos.reset();
                    }
                }
            }

            if (ReadState.HEADERS.equals(readState)) {
                baos.write(buffer, pos, read - pos);
                pos = read;
                fillBuffer();
                localRead = this.read;
            } else {
                break;
            }
        }

        return p;
    }

    private void initialize() throws IOException {
        if (!initialized) {
            initialized = true;
            this.boundaryMarker = getBoundaryMarker(super.getContentType());

            if (this.boundaryMarker == null) {
                throw new FileUploadException("The request was rejected because " + "no multipart boundary was found");
            }

            this.encoding = getCharacterEncoding();
            this.parameters = new HashMap<String, Param>();
            this.buffer = new byte[BUFFER_SIZE];
            this.readState = ReadState.BOUNDARY;
            this.input = getInputStream();
            setupProgressData();
            fillBuffer();

            int localRead = this.read;

            while (localRead > 0) {
                for (int i = 0; i < localRead; i++) {
                    if (checkSequence(buffer, i, boundaryMarker) && checkSequence(buffer, i + 2, CR_LF)) {
                        readState = ReadState.HEADERS;
                        i += 2;
                        pos = i + 1;

                        break;
                    }
                }

                if (ReadState.BOUNDARY.equals(readState)) {
                    pos = read - (boundaryMarker.length + CR_LF.length) + 1;
                    fillBuffer();
                    localRead = this.read;
                } else {
                    break;
                }
            }
        }
    }

    public void parseRequest() {
        canStop = true;
        setupProgressData();

        try {
            initialize();

            while (read > 0) {
                readNext();
            }
        } catch (IOException e) {
            this.cancel();

            if (!this.shouldStop) {
                throw new FileUploadException("IO Error parsing multipart request", e);
            }
        }
    }

    public static MultipartRequest lookupRequest(FacesContext context, String uploadId) {
        Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
        Map<String, String> requestKeys = (Map<String,
            String>) sessionMap.get(FileUploadConstants.REQUEST_KEYS_BEAN_NAME);

        if (requestKeys != null) {
            String requestKey = requestKeys.get(uploadId);

            if (requestKey != null) {
                MultipartRequestRegistry requestRegistry = MultipartRequestRegistry.getInstance(context);

                if (requestRegistry != null) {
                    MultipartRequest request = requestRegistry.getRequest(requestKey);

                    if (request != null) {
                        return request;
                    }
                }
            }
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    private void setupProgressData() {
        if ((percentMap == null) || (requestSizeMap == null) || (requestKeysMap == null)) {
            FacesContext facesContext = FacesContext.getCurrentInstance();

            if (facesContext != null) {
                ExternalContext externalContext = facesContext.getExternalContext();

                if (externalContext != null) {
                    Map<String, Object> sessionMap = externalContext.getSessionMap();

                    if (sessionMap != null) {
                        String uploadId = getUploadId();

                        synchronized (sessionMap) {
                            if (percentMap == null) {
                                percentMap = (Map<String,
                                    Object>) sessionMap.get(FileUploadConstants.PERCENT_BEAN_NAME);

                                if (percentMap == null) {
                                    percentMap = new ConcurrentHashMap<String, Object>();
                                    sessionMap.put(FileUploadConstants.PERCENT_BEAN_NAME, percentMap);
                                }
                            }

                            if (requestSizeMap == null) {
                                requestSizeMap =
                                    (Map<String, Integer>) sessionMap.get(FileUploadConstants.REQUEST_SIZE_BEAN_NAME);

                                if (requestSizeMap == null) {
                                    requestSizeMap = new ConcurrentHashMap<String, Integer>();
                                    sessionMap.put(FileUploadConstants.REQUEST_SIZE_BEAN_NAME, requestSizeMap);
                                }
                            }

                            if (requestKeysMap == null) {
                                requestKeysMap =
                                    (Map<String, String>) sessionMap.get(FileUploadConstants.REQUEST_KEYS_BEAN_NAME);

                                if (requestKeysMap == null) {
                                    requestKeysMap = new ConcurrentHashMap<String, String>();
                                    sessionMap.put(FileUploadConstants.REQUEST_KEYS_BEAN_NAME, requestKeysMap);
                                }
                            }
                        }

                        percentMap.put(uploadId, Double.valueOf(0));
                        requestSizeMap.put(uploadId, getSize());
                        requestRegistry = MultipartRequestRegistry.getInstance(facesContext);
                        requestKey = requestRegistry.registerRequest(this);
                        requestKeysMap.put(uploadId, requestKey);
                    }
                }
            }
        }
    }

    private void fillProgressInfo() {
        setupProgressData();

        if (percentMap != null) {
            Double percent = (Double) (100.0 * this.bytesRead / this.contentLength);

            percentMap.put(uid, percent);

            // this.percent = percent;
        }
    }

    private byte[] getBoundaryMarker(String contentType) {
        Map<String, String> params = parseParams(contentType, ";");
        String boundaryStr = (String) params.get("boundary");

        if (boundaryStr == null) {
            return null;
        }

        try {
            return boundaryStr.getBytes("ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            return boundaryStr.getBytes();
        }
    }

    /**
     * Checks if a specified sequence of bytes ends at a specific position
     * within a byte array.
     *
     * @param data
     * @param pos
     * @param seq
     * @return boolean indicating if the sequence was found at the specified
     *         position
     */
    private boolean checkSequence(byte[] data, int pos, byte[] seq) {
        if ((pos - seq.length < -1) || (pos >= data.length)) {
            return false;
        }

        for (int i = 0; i < seq.length; i++) {
            if (data[(pos - seq.length) + i + 1] != seq[i]) {
                return false;
            }
        }

        return true;
    }

    private Map<String, String> parseParams(String paramStr, String separator) {
        Map<String, String> paramMap = new HashMap<String, String>();

        parseParams(paramStr, separator, paramMap);

        return paramMap;
    }

    private void parseParams(String paramStr, String separator, Map<String, String> paramMap) {
        String[] parts = paramStr.split(separator);

        for (String part : parts) {
            Matcher m = PARAM_VALUE_PATTERN.matcher(part);

            if (m.matches()) {
                String key = m.group(1);
                String value = m.group(2);

                // Strip double quotes
                if (value.startsWith("\"") && value.endsWith("\"")) {
                    value = value.substring(1, value.length() - 1);
                }

                if (!"filename".equals(key)) {
                    paramMap.put(key, value);
                } else {
                    paramMap.put(key, parseFileName(paramStr));
                }
            }
        }
    }

    private String parseFileName(String parseStr) {
        Matcher m = FILE_NAME_PATTERN.matcher(parseStr);

        if (m.matches()) {
            String name = m.group(1);

            if (name.startsWith("&")) {
                return decodeFileName(name);
            } else {
                return name;
            }
        }

        return null;
    }

    private Param getParam(String name) {
        Param param = null;

        if (parameters != null) {
            param = parameters.get(name);
        }

        if (param == null) {
            if (!canceled) {
                try {
                    initialize();

                    while ((param == null) && (read > 0)) {
                        readNext();
                        param = parameters.get(name);
                    }
                } catch (IOException e) {
                    this.cancel();

                    throw new FileUploadException("IO Error parsing multipart request", e);
                }
            }
        }

        return param;
    }

    public Integer getSize() {
        return contentLength;
    }

    @Override
    public Enumeration getParameterNames() {
        if (parameters == null) {
            parseRequest();
        }

        return Collections.enumeration(parameters.keySet());
    }

    public byte[] getFileBytes(String name) {
        Param p = getParam(name);

        return ((p != null) && (p instanceof FileParam)) ? ((FileParam) p).getData() : null;
    }

    public InputStream getFileInputStream(String name) {
        Param p = getParam(name);

        return ((p != null) && (p instanceof FileParam)) ? ((FileParam) p).getInputStream() : null;
    }

    public String getFileContentType(String name) {
        Param p = getParam(name);

        return ((p != null) && (p instanceof FileParam)) ? ((FileParam) p).getContentType() : null;
    }

    public Object getFile(String name) {
        Param p = getParam(name);

        return ((p != null) && (p instanceof FileParam)) ? ((FileParam) p).getFile() : null;
    }

    public String getFileName(String name) {
        Param p = getParam(name);

        return ((p != null) && (p instanceof FileParam)) ? ((FileParam) p).getFilename() : null;
    }

    public int getFileSize(String name) {
        Param p = getParam(name);

        return ((p != null) && (p instanceof FileParam)) ? ((FileParam) p).getFileSize() : -1;
    }

    @Override
    public String getParameter(String name) {
        Param p = getParam(name);

        if ((p != null) && (p instanceof ValueParam)) {
            ValueParam vp = (ValueParam) p;

            if (vp.getValue() instanceof String) {
                return (String) vp.getValue();
            }
        } else if ((p != null) && (p instanceof FileParam)) {
            return "---BINARY DATA---";
        } else {
            return super.getParameter(name);
        }

        return null;
    }

    @Override
    public String[] getParameterValues(String name) {
        parseRequest();

        Param p = getParam(name);

        if ((p != null) && (p instanceof ValueParam)) {
            ValueParam vp = (ValueParam) p;

            if (vp.getValue() instanceof List) {
                List vals = (List) vp.getValue();
                String[] values = new String[vals.size()];

                vals.toArray(values);

                return values;
            } else {
                return new String[]{(String) vp.getValue()};
            }
        } else {
            return super.getParameterValues(name);
        }
    }

    @Override
    public Map getParameterMap() {
        if (parameters == null) {
            parseRequest();
        }

        Map<String, Object> params = new HashMap<String, Object>(super.getParameterMap());

        for (String name : parameters.keySet()) {
            Param p = parameters.get(name);

            if (p instanceof ValueParam) {
                ValueParam vp = (ValueParam) p;

                if (vp.getValue() instanceof String) {
                    params.put(name, vp.getValue());
                } else if (vp.getValue() instanceof List) {
                    params.put(name, getParameterValues(name));
                }
            }
        }

        return params;
    }

    public List<UploadItem> getUploadItems() {
        List<UploadItem> uploadItems = new ArrayList<UploadItem>();

        for (String k : keys) {
            uploadItems.add(new UploadItem(getFileName(k), getFileSize(k), getFileContentType(k), getFile(k)));
        }

        return uploadItems;
    }

    public boolean isFormUpload() {
        return "_richfaces_form_upload".equals(uid);
    }

    @Override
    public String getHeader(String name) {
        if (!"Accept".equals(name)) {
            return super.getHeader(name);
        } else {
            return BaseXMLFilter.TEXT_HTML;
        }
    }

    public void stop() {
        if (canStop) {
            shouldStop = true;
        }
    }

    public boolean isStopped() {
        return this.shouldStop;
    }

    public boolean isDone() {
        return !(this.shouldStop
            && (this.canceled
            || ((this.contentLength != null) && (this.contentLength.intValue() != this.bytesRead))));
    }

    @Override
    public String getContentType() {
        return "application/x-www-form-urlencoded";
    }

    protected String getUploadId() {
        return uid;
    }

    public void clearRequestData() {
        String uploadId = getUploadId();

        if (percentMap != null) {
            percentMap.remove(uploadId);
        }

        if (requestSizeMap != null) {
            requestSizeMap.remove(uploadId);
        }

        if (requestKeysMap != null) {
            requestKeysMap.remove(uploadId);
        }

        if (requestRegistry != null) {
            requestRegistry.removeRequest(requestKey);
        }
    }

    private class FileParam extends Param {
        private ByteArrayOutputStream bOut = null;
        private FileOutputStream fOut = null;
        private File tempFile = null;
        private String contentType;
        private int fileSize;
        private String filename;

        public FileParam(String name) {
            super(name);
            keys.add(name);
        }

        public Object getFile() {
            if (null != tempFile) {
                if (fOut != null) {
                    try {
                        fOut.close();
                    } catch (IOException ex) {

                        // TODO Refactoring
                    }

                    fOut = null;
                }

                return tempFile;
            } else if (null != bOut) {
                return bOut.toByteArray();
            }

            return null;
        }

        public String getFilename() {
            return filename;
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }

        public String getContentType() {
            return contentType;
        }

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }

        public int getFileSize() {
            return fileSize;
        }

        public File createTempFile() {
            try {
                tempFile = File.createTempFile(UUID.randomUUID().toString(), ".upload");

                // tempFile.deleteOnExit();
                fOut = new FileOutputStream(tempFile);
            } catch (IOException ex) {
                throw new FileUploadException("Could not create temporary file");
            }

            return tempFile;
        }

        public void deleteFile() {
            try {
                if (fOut != null) {
                    fOut.close();

                    if (tempFile != null) {
                        tempFile.delete();
                    }
                }
            } catch (Exception e) {
                throw new FileUploadException("Could not delete temporary file");
            }
        }

        @Override
        public void appendData(byte[] data, int start, int length) throws IOException {

            // read += length;
            if (fOut != null) {
                fOut.write(data, start, length);
                fOut.flush();
            } else {
                if (bOut == null) {
                    bOut = new ByteArrayOutputStream();
                }

                bOut.write(data, start, length);
            }

            fileSize += length;
        }

        public byte[] getData() {
            if (fOut != null) {
                try {
                    fOut.close();
                } catch (IOException ex) {

                    // TODO Refactoring
                }

                fOut = null;
            }

            if (bOut != null) {
                return bOut.toByteArray();
            } else if (tempFile != null) {
                if (tempFile.exists()) {
                    try {
                        FileInputStream fIn = new FileInputStream(tempFile);
                        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
                        byte[] buf = new byte[512];
                        int read = fIn.read(buf);

                        while (read != -1) {
                            bOut.write(buf, 0, read);
                            read = fIn.read(buf);
                        }

                        bOut.flush();
                        fIn.close();
                        tempFile.delete();

                        return bOut.toByteArray();
                    } catch (IOException ex) { /* too bad? */
                    }
                }
            }

            return null;
        }

        public InputStream getInputStream() {
            if (fOut != null) {
                try {
                    fOut.close();
                } catch (IOException ex) {

                    // TODO Refactoring
                }

                fOut = null;
            }

            if (bOut != null) {
                return new ByteArrayInputStream(bOut.toByteArray());
            } else if (tempFile != null) {
                try {
                    return new FileInputStream(tempFile) {
                        @Override
                        public void close() throws IOException {
                            super.close();
                            tempFile.delete();
                        }
                    };
                } catch (FileNotFoundException ex) {

                    // TODO Refactoring
                }
            }

            return null;
        }
    }

    private abstract class Param {
        private String name;

        public Param(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public abstract void appendData(byte[] data, int start, int length) throws IOException;
    }

    private class ValueParam extends Param {
        private Object value = null;
        private ByteArrayOutputStream buf = new ByteArrayOutputStream();

        public ValueParam(String name) {
            super(name);
        }

        @Override
        public void appendData(byte[] data, int start, int length) throws IOException {

            // read += length;
            buf.write(data, start, length);
        }

        public void complete() throws UnsupportedEncodingException {
            String val = (encoding == null) ? new String(buf.toByteArray()) : new String(buf.toByteArray(), encoding);

            if (value == null) {
                value = val;
            } else {
                if (!(value instanceof List)) {
                    List<String> v = new ArrayList<String>();

                    v.add((String) value);
                    value = v;
                }

                ((List) value).add(val);
            }

            buf.reset();
        }

        public Object getValue() {
            return value;
        }
    }
}
