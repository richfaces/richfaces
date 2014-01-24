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
package org.richfaces.request;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.richfaces.exception.FileUploadException;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;
import org.richfaces.model.UploadedFile;
import org.richfaces.request.ByteSequenceMatcher.BytesHandler;

import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

/**
 * @author Nick Belaevski
 *
 */
public final class MultipartRequestParser {
    static final String PARAM_NAME = "name";
    static final String PARAM_FILENAME = "filename";
    static final String PARAM_CONTENT_TYPE = "Content-Type";
    private static final byte CR = 0x0d;
    private static final byte LF = 0x0a;
    private static final byte[] CR_LF = { CR, LF };
    private static final byte[] HYPHENS = { 0x2d, 0x2d }; // '--'
    private static final int BUFFER_SIZE = 2048;
    private static final int CHUNK_SIZE = 1024;
    private static final int MAX_HEADER_SIZE = 32768;
    private static final Logger LOGGER = RichfacesLogger.APPLICATION.getLogger();
    private static final Pattern FILE_NAME_PATTERN = Pattern.compile(".*filename=\"(.*)\"");
    private static final Pattern PARAM_VALUE_PATTERN = Pattern.compile("^\\s*([^\\s=]+)\\s*[=:]\\s*(.+)\\s*$");
    private static final BytesHandler NOOP_HANDLER = new BytesHandler() {
        public void handle(byte[] bytes, int length) {
            // do nothing
        }
    };

    private class HeadersHandler implements BytesHandler {
        private ByteArrayOutputStream baos = new ByteArrayOutputStream(BUFFER_SIZE);

        public void handle(byte[] bytes, int length) throws IOException {
            if (length != 0) {
                if (baos.size() + length > MAX_HEADER_SIZE) {
                    throw new IOException("Header section is too big");
                }

                baos.write(bytes, 0, length);
            }
        }

        public boolean dataEquals(byte[] bytes) {
            return (baos.size() == bytes.length) && Arrays.equals(HYPHENS, baos.toByteArray());
        }

        public String asString() throws UnsupportedEncodingException {
            if (request.getCharacterEncoding() != null) {
                return baos.toString(request.getCharacterEncoding());
            } else {
                return baos.toString();
            }
        }

        public void reset() {
            baos.reset();
        }
    }

    private HttpServletRequest request;
    private boolean createTempFiles;
    private String tempFilesDirectory;
    private Multimap<String, String> parametersMap = LinkedListMultimap.create();
    private List<UploadedFile> uploadedFiles = Lists.newArrayList();
    private byte[] boundaryMarker;
    private ByteSequenceMatcher sequenceMatcher;
    private HeadersHandler headersHandler;
    private ProgressControl progressControl;

    /**
     * @param request
     * @param createTempFiles
     * @param tempFilesDirectory
     * @param uploadId
     */
    public MultipartRequestParser(HttpServletRequest request, boolean createTempFiles, String tempFilesDirectory,
        ProgressControl progressControl) {

        this.request = request;
        this.createTempFiles = createTempFiles;
        this.tempFilesDirectory = tempFilesDirectory;
        this.progressControl = progressControl;
    }

    private void cancel() {
        for (UploadedFile uploadedFile : uploadedFiles) {
            try {
                uploadedFile.delete();
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

    public Multimap<String, String> getParameters() {
        return parametersMap;
    }

    public Iterable<UploadedFile> getUploadedFiles() {
        return uploadedFiles;
    }

    public void parse() throws FileUploadException {
        try {
            initialize();

            while (!sequenceMatcher.isEOF()) {
                readNext();
            }
        } catch (IOException e) {
            this.cancel();

            throw new FileUploadException(MessageFormat.format("Exception parsing multipart request: {0}", e.getMessage()), e);
        }
    }

    private void initialize() throws IOException, FileUploadException {
        this.boundaryMarker = getBoundaryMarker(request.getContentType());
        if (this.boundaryMarker == null) {
            throw new FileUploadException("The request was rejected because no multipart boundary was found");
        }

        if (HYPHENS.length + boundaryMarker.length + CHUNK_SIZE + CR_LF.length > BUFFER_SIZE) {
            throw new FileUploadException("Boundary marker is too long");
        }

        this.sequenceMatcher = new ByteSequenceMatcher(progressControl.wrapStream(request.getInputStream()), BUFFER_SIZE);

        readProlog();
    }

    private String getFirstParameterValue(Multimap<String, String> multimap, String key) {
        Collection<String> values = multimap.get(key);

        if (values.isEmpty()) {
            return null;
        }

        return Iterables.get(values, 0);
    }

    private byte[] getBoundaryMarker(String contentType) {
        Multimap<String, String> params = parseParams(contentType, ";");
        String boundaryStr = getFirstParameterValue(params, "boundary");

        if (boundaryStr == null) {
            return null;
        }

        try {
            return boundaryStr.getBytes("ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            return boundaryStr.getBytes();
        }
    }

    private Multimap<String, String> parseParams(String paramStr, String separator) {
        Multimap<String, String> paramMap = LinkedListMultimap.create();
        parseParams(paramStr, separator, paramMap);
        return paramMap;
    }

    private void parseParams(String paramStr, String separator, Multimap<String, String> paramMap) {
        String[] parts = paramStr.split(separator);

        for (String part : parts) {
            Matcher m = PARAM_VALUE_PATTERN.matcher(part);
            if (m.matches()) {
                String key = m.group(1).toLowerCase(Locale.US);
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

    // TODO - URI decoder?
    private static String decodeFileName(String name) {
        String fileName = null;
        try {
            StringBuilder builder = new StringBuilder();
            String[] codes = name.split(";");
            if (codes != null) {
                for (String code : codes) {
                    if (code.startsWith("&")) {
                        String sCode = code.replaceAll("[&#]*", "");
                        Integer iCode = Integer.parseInt(sCode);
                        builder.append(Character.toChars(iCode));
                    } else {
                        builder.append(code);
                    }
                }
                fileName = builder.toString();
            }
        } catch (Exception e) {
            fileName = name;
        }

        return fileName;
    }

    static String parseFileName(String parseStr) {
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

    private void readProlog() throws IOException {
        sequenceMatcher.setBytesHandler(NOOP_HANDLER);
        sequenceMatcher.findSequence(-1, HYPHENS, boundaryMarker);
        if (!sequenceMatcher.isMatchedAndNotEOF()) {
            throw new IOException("Request prolog cannot be read");
        }
    }

    private void readData(FileUploadParam uploadParam) throws IOException {
        sequenceMatcher.setBytesHandler(uploadParam);
        sequenceMatcher.findSequence(CHUNK_SIZE, CR_LF, HYPHENS, boundaryMarker);
        sequenceMatcher.setBytesHandler(null);
        if (!this.sequenceMatcher.isMatchedAndNotEOF()) {
            throw new IOException("Request data cannot be read");
        }
    }

    private void readNext() throws IOException {
        Multimap<String, String> headers = readHeaders();
        FileUploadParam param = createParam(headers);

        if (param == null) {
            return;
        }

        param.create();

        try {
            readData(param);
        } finally {
            param.complete();
        }

        if (param.isFileParam()) {
            uploadedFiles.add(new UploadedFile25(param.getName(), param.getResource(), headers));
        } else {
            parametersMap.put(param.getName(), param.getValue());
        }
    }

    private FileUploadParam createParam(Multimap<String, String> headers) {
        if (headers == null) {
            return null;
        }

        String parameterName = getFirstParameterValue(headers, PARAM_NAME);

        if (Strings.isNullOrEmpty(parameterName)) {
            return null;
        }

        boolean isFile = !Strings.isNullOrEmpty(getFirstParameterValue(headers, PARAM_FILENAME));

        FileUploadParam param;

        if (isFile) {
            if (createTempFiles) {
                param = new FileUploadDiscResource(parameterName, tempFilesDirectory);
            } else {
                param = new FileUploadMemoryResource(parameterName, tempFilesDirectory);
            }
        } else {
            param = new FileUploadValueParam(parameterName, request.getCharacterEncoding());
        }

        return param;
    }

    private Multimap<String, String> readHeaders() throws IOException {
        if (sequenceMatcher.isEOF()) {
            return null;
        }

        if (headersHandler == null) {
            headersHandler = new HeadersHandler();
        } else {
            headersHandler.reset();
        }

        sequenceMatcher.setBytesHandler(headersHandler);
        sequenceMatcher.findSequence(-1, CR_LF);

        if (sequenceMatcher.isMatchedAndNotEOF() && !headersHandler.dataEquals(HYPHENS)) {
            headersHandler.reset();

            sequenceMatcher.findSequence(-1, CR_LF, CR_LF);

            if (!sequenceMatcher.isMatchedAndNotEOF()) {
                throw new IOException("Request header cannot be read");
            }

            String headersString = headersHandler.asString();
            Multimap<String, String> headers = LinkedListMultimap.create();
            String[] split = headersString.split("\r\n");
            for (String headerString : split) {
                parseParams(headerString, "; ", headers);
            }

            return headers;
        }

        return null;
    }
}
