/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 *
 * Contributor(s):
 *
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 *
 * Portions Copyrighted 2009 Exadel, Inc.
 *
 * Exadel. Inc, elects to include this software in this distribution under the
 * GPL Version 2 license.
 */
package org.richfaces.resource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;
import java.io.StreamCorruptedException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import javax.faces.FacesException;
import javax.faces.application.ViewHandler;
import javax.faces.component.StateHolder;
import javax.faces.component.UINamingContainer;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.ajax4jsf.Messages;
import org.ajax4jsf.util.base64.Codec;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;
import org.richfaces.util.FastJoiner;
import org.richfaces.util.LookAheadObjectInputStream;
import org.richfaces.util.PropertiesUtil;

import com.google.common.base.Function;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;

/**
 * @author Nick Belaevski
 * @since 4.0
 */
public final class ResourceUtils {
    private static final Pattern RESOURCE_PARAMS_SPLIT_PATTERN = Pattern.compile("\\s*(\\s|,)\\s*");
    private static final Pattern RESOURCE_PARAMS = Pattern.compile("\\{([^\\}]*)\\}\\s*$");
    private static final Logger RESOURCE_LOGGER = RichfacesLogger.RESOURCE.getLogger();
    /* HTTP Date format required by the HTTP/1.1 RFC */
    private static final String RFC1123_DATE_PATTERN = "EEE, dd MMM yyyy HH:mm:ss zzz";
    // TODO codec have settings
    private static final Codec CODEC = new Codec();
    private static final SimpleDateFormat RFC1123_DATE_FORMATTER;
    private static final String QUESTION_SIGN = "?";
    private static final String EQUALS_SIGN = "=";
    private static final Pattern CHARSET_IN_CONTENT_TYPE_PATTERN = Pattern.compile(";\\s*charset\\s*=\\s*([^\\s;]+)",
        Pattern.CASE_INSENSITIVE);
    private static final long MILLISECOND_IN_SECOND = 1000L;
    private static final String QUOTED_STRING_REGEX = "(?:\\\\[\\x00-\\x7F]|[^\"\\\\])+";
    private static final Pattern ETAG_PATTERN = Pattern.compile("(?:W/)?\"(" + QUOTED_STRING_REGEX + ")\"(?:,\\s*)?");

    public static final class NamingContainerDataHolder {
        public static final char SEPARATOR_CHAR = UINamingContainer.getSeparatorChar(FacesContext.getCurrentInstance());
        public static final FastJoiner SEPARATOR_CHAR_JOINER = FastJoiner.on(SEPARATOR_CHAR);
        public static final Splitter SEPARATOR_CHAR_SPLITTER = Splitter.on(SEPARATOR_CHAR);

        private NamingContainerDataHolder() {
        }
    }

    static {
        SimpleDateFormat format = new SimpleDateFormat(RFC1123_DATE_PATTERN, Locale.US);

        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        RFC1123_DATE_FORMATTER = format;
    }

    private ResourceUtils() {
    }

    public static String getMappingForRequest(FacesContext context) {
        ExternalContext externalContext = context.getExternalContext();
        String servletPath = externalContext.getRequestServletPath();

        if (servletPath == null) {
            return null;
        }

        if (servletPath.length() == 0) {
            return "/";
        }

        String pathInfo = externalContext.getRequestPathInfo();

        if (pathInfo != null) {
            return servletPath;
        }

        int idx = servletPath.lastIndexOf('.');

        if (idx < 0) {
            return servletPath;
        } else {
            return servletPath.substring(idx);
        }
    }

    public static Date parseHttpDate(String s) {
        Date result = null;

        if (s != null) {
            try {
                result = (Date) ((Format) RFC1123_DATE_FORMATTER.clone()).parseObject(s);
            } catch (ParseException e) {
                RESOURCE_LOGGER.error(e.getMessage(), e);
            }
        }

        return result;
    }

    public static String formatHttpDate(Object object) {
        if (object != null) {
            return ((Format) RFC1123_DATE_FORMATTER.clone()).format(object);
        } else {
            return null;
        }
    }

    protected static byte[] encrypt(byte[] src) {
        try {
            Deflater compressor = new Deflater(Deflater.BEST_SPEED);
            byte[] compressed = new byte[src.length + 100];

            compressor.setInput(src);
            compressor.finish();

            int totalOut = compressor.deflate(compressed);
            byte[] zipsrc = new byte[totalOut];

            System.arraycopy(compressed, 0, zipsrc, 0, totalOut);
            compressor.end();

            return CODEC.encode(zipsrc);
        } catch (Exception e) {
            throw new FacesException("Error encode resource data", e);
        }
    }

    protected static byte[] decrypt(byte[] src) {
        try {
            byte[] zipsrc = CODEC.decode(src);
            Inflater decompressor = new Inflater();
            byte[] uncompressed = new byte[zipsrc.length * 5];

            decompressor.setInput(zipsrc);

            int totalOut = decompressor.inflate(uncompressed);
            byte[] out = new byte[totalOut];

            System.arraycopy(uncompressed, 0, out, 0, totalOut);
            decompressor.end();

            return out;
        } catch (Exception e) {
            throw new FacesException("Error decode resource data", e);
        }
    }

    public static byte[] decodeBytesData(String encodedData) {
        byte[] objectArray = null;

        try {
            byte[] dataArray = encodedData.getBytes("ISO-8859-1");

            objectArray = decrypt(dataArray);
        } catch (UnsupportedEncodingException e1) {

            // default encoding always presented.
        }

        return objectArray;
    }

    public static Object decodeObjectData(String encodedData) {
        byte[] objectArray = decodeBytesData(encodedData);

        try {
            ObjectInputStream in = new LookAheadObjectInputStream(new ByteArrayInputStream(objectArray));
            return in.readObject();
        } catch (StreamCorruptedException e) {
            RESOURCE_LOGGER.error(Messages.getMessage(Messages.STREAM_CORRUPTED_ERROR), e);
        } catch (IOException e) {
            RESOURCE_LOGGER.error(Messages.getMessage(Messages.DESERIALIZE_DATA_INPUT_ERROR), e);
        } catch (ClassNotFoundException e) {
            RESOURCE_LOGGER.error(Messages.getMessage(Messages.DATA_CLASS_NOT_FOUND_ERROR), e);
        }

        return null;
    }

    public static String encodeBytesData(byte[] data) {
        if (data != null) {
            try {
                byte[] dataArray = encrypt(data);

                return new String(dataArray, "ISO-8859-1");
            } catch (Exception e) {
                RESOURCE_LOGGER.error(Messages.getMessage(Messages.QUERY_STRING_BUILDING_ERROR), e);
            }
        }

        return null;
    }

    public static String encodeObjectData(Object data) {
        if (data != null) {
            try {
                ByteArrayOutputStream dataStream = new ByteArrayOutputStream(1024);
                ObjectOutputStream objStream = new ObjectOutputStream(dataStream);

                objStream.writeObject(data);
                objStream.flush();
                objStream.close();
                dataStream.close();

                return encodeBytesData(dataStream.toByteArray());
            } catch (Exception e) {
                RESOURCE_LOGGER.error(Messages.getMessage(Messages.QUERY_STRING_BUILDING_ERROR), e);
            }
        }

        return null;
    }

    public static String encodeJSFURL(FacesContext context, String url) {
        String mapping = ResourceUtils.getMappingForRequest(context);
        String resourcePath = url;

        if (mapping.startsWith("/")) {
            if (mapping.length() != 1) {
                resourcePath = mapping + url;
            }
        } else {
            int paramsSeparator = resourcePath.indexOf(QUESTION_SIGN);
            if (paramsSeparator >= 0) {
                StringBuilder resourcePathBuilder = new StringBuilder(resourcePath.length() + mapping.length());

                resourcePathBuilder.append(resourcePath.substring(0, paramsSeparator));
                resourcePathBuilder.append(mapping);
                resourcePathBuilder.append(resourcePath.substring(paramsSeparator));

                resourcePath = resourcePathBuilder.toString();
            } else {
                resourcePath += mapping;
            }
        }

        ViewHandler viewHandler = context.getApplication().getViewHandler();

        return viewHandler.getResourceURL(context, resourcePath);
    }

    public static Map<String, String> parseResourceParameters(String resourceName) {
        Map<String, String> params = new HashMap<String, String>();

        Matcher matcher = RESOURCE_PARAMS.matcher(resourceName);
        if (matcher.find()) {
            String paramsString = matcher.group(1);

            String[] paramsSplit = RESOURCE_PARAMS_SPLIT_PATTERN.split(paramsString);

            try {
                for (String param : paramsSplit) {
                    String[] pair = param.split(EQUALS_SIGN);
                    String key = URLDecoder.decode(pair[0], "UTF-8").trim();
                    String value = URLDecoder.decode(pair[1], "UTF-8").trim();
                    if (key != null && value != null && key.trim().length() > 0 && value.trim().length() > 0) {
                        params.put(key, value);
                    }
                }
            } catch (UnsupportedEncodingException e) {
                RESOURCE_LOGGER.debug("Cannot parse resource parameters");
            }
        }

        return params;
    }

    public static String decodeResourceURL(FacesContext context) {
        ExternalContext externalContext = context.getExternalContext();
        String resourceName = null;
        String facesMapping = ResourceUtils.getMappingForRequest(context);

        if (facesMapping != null) {
            if (facesMapping.startsWith("/")) {

                // prefix mapping
                resourceName = externalContext.getRequestPathInfo();
            } else {
                String requestServletPath = externalContext.getRequestServletPath();

                resourceName = requestServletPath.substring(0, requestServletPath.length() - facesMapping.length());
            }
        }

        return resourceName;
    }

    public static void copyStreamContent(InputStream is, OutputStream os) throws IOException {
        ReadableByteChannel inChannel = Channels.newChannel(is);
        WritableByteChannel outChannel = Channels.newChannel(os);

        // TODO make this configurable
        ByteBuffer buffer = ByteBuffer.allocate(8192);
        int read;

        while ((read = inChannel.read(buffer)) > 0) {
            buffer.rewind();
            buffer.limit(read);

            while (read > 0) {
                read -= outChannel.write(buffer);
            }

            buffer.clear();
        }
    }

    private static boolean isLegalURIQueryChar(char c) {
        // http://java.sun.com/j2se/1.5.0/docs/api/java/net/URI.html

        // alphanum
        if (('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z') || ('0' <= c && c <= '9')) {
            return true;
        }

        // reserved
        // if (c == ';' || c == '/' || c == '?' || c == ':' || c == '@' || c == '&' || c == '=' || c == '+' || c == ',' || c ==
        // '$' ) {
        // return true;
        // }

        // mark
        if (c == '-' || c == '_' || c == '.' || c == '!' || c == '~' || c == '*' || c == '\'' || c == '(' || c == ')') {
            return true;
        }

        return false;
    }

    private static String escapeURIByte(int b) {
        if (0x10 <= b) {
            return "%" + Integer.toHexString(b);
        } else {
            return "%0" + Integer.toHexString(b);
        }
    }

    public static String encodeURIQueryPart(String s) {
        StringBuilder builder = new StringBuilder();

        int start = 0;
        int idx = 0;

        int length = s.length();
        CharsetEncoder encoder = null;
        ByteBuffer byteBuffer = null;
        CharBuffer buffer = null;

        for (; idx < length; idx++) {
            char c = s.charAt(idx);

            if (!isLegalURIQueryChar(c)) {
                builder.append(s.substring(start, idx));

                if (encoder == null) {
                    encoder = Charset.forName("UTF-8").newEncoder();
                }
                if (buffer == null) {
                    buffer = CharBuffer.allocate(1);
                    byteBuffer = ByteBuffer.allocate(6); // max bytes size in UTF-8
                } else {
                    byteBuffer.limit(6);
                }

                buffer.put(0, c);

                buffer.rewind();
                byteBuffer.rewind();
                encoder.encode(buffer, byteBuffer, true);

                byteBuffer.flip();

                int limit = byteBuffer.limit();
                for (int i = 0; i < limit; i++) {
                    int b = (0xFF & byteBuffer.get());
                    builder.append(escapeURIByte(b));
                }

                start = idx + 1;
            }
        }

        builder.append(s.substring(start, idx));

        return builder.toString();
    }

    public static Object saveResourceState(FacesContext context, Object resource) {
        if (resource instanceof StateHolderResource) {
            StateHolderResource stateHolderResource = (StateHolderResource) resource;
            if (stateHolderResource.isTransient()) {
                return null;
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            try {
                stateHolderResource.writeState(context, dos);
            } catch (IOException e) {
                throw new FacesException(e.getMessage(), e);
            } finally {
                try {
                    dos.close();
                } catch (IOException e) {
                    RESOURCE_LOGGER.debug(e.getMessage(), e);
                }
            }

            return baos.toByteArray();
        } else if (resource instanceof StateHolder) {
            StateHolder stateHolder = (StateHolder) resource;
            if (stateHolder.isTransient()) {
                return null;
            }

            return stateHolder.saveState(context);
        }

        return null;
    }

    public static void restoreResourceState(FacesContext context, Object resource, Object state) {
        if (state == null) {
            // transient resource hasn't provided any data
            return;
        }

        if (resource instanceof StateHolderResource) {
            StateHolderResource stateHolderResource = (StateHolderResource) resource;

            ByteArrayInputStream bais = new ByteArrayInputStream((byte[]) state);
            DataInputStream dis = new DataInputStream(bais);
            try {
                stateHolderResource.readState(context, dis);
            } catch (IOException e) {
                throw new FacesException(e.getMessage(), e);
            } finally {
                try {
                    dis.close();
                } catch (IOException e) {
                    RESOURCE_LOGGER.debug(e.getMessage(), e);
                }
            }
        } else if (resource instanceof StateHolder) {
            StateHolder stateHolder = (StateHolder) resource;
            stateHolder.restoreState(context, state);
        }
    }

    public static Charset getCharsetFromContentType(String contentType) {
        String charsetName = null;

        if (contentType != null) {
            Matcher matcher = CHARSET_IN_CONTENT_TYPE_PATTERN.matcher(contentType);
            if (matcher.find()) {
                charsetName = matcher.group(1);
            }
        }

        return Strings.isNullOrEmpty(charsetName) ? Charset.defaultCharset() : Charset.forName(charsetName);
    }

    public static String formatWeakTag(String eTag) {
        String formattedTag = formatTag(eTag);

        if (formattedTag == null) {
            return null;
        }

        return "W/" + formattedTag;
    }

    public static String formatTag(String eTag) {
        if (eTag == null) {
            return null;
        }

        if (!eTag.matches(QUOTED_STRING_REGEX)) {
            throw new IllegalArgumentException("tag must matches to regexp '" + QUOTED_STRING_REGEX + '\'');
        }

        return '\"' + eTag + '\"';
    }

    public static boolean matchTag(String eTag, String eTagHeaderValue) {
        if ((eTag == null) || (eTagHeaderValue == null)) {
            throw new IllegalArgumentException("tag and tagHeaderValue must be not null");
        }

        Matcher eTagMatcher = ETAG_PATTERN.matcher(eTag);

        if (!eTagMatcher.find()) {
            throw new IllegalArgumentException();
        }

        String tag = eTagMatcher.group(1);
        Matcher eTagHeaderMatcher = ETAG_PATTERN.matcher(eTagHeaderValue);

        while (eTagHeaderMatcher.find()) {
            if (tag.equals(eTagHeaderMatcher.group(1))) {
                return true;
            }
        }

        return false;
    }

    public static long millisToSecond(long millisecond) {
        return millisecond / MILLISECOND_IN_SECOND;
    }

    public static long secondToMillis(long second) {
        return second * MILLISECOND_IN_SECOND;
    }

    static <V> Map<ResourceKey, V> readMappings(Function<Entry<String, String>, V> producer, String mappingFileName) {
        Map<ResourceKey, V> result = Maps.newHashMap();

        for (Entry<String, String> entry : PropertiesUtil.loadProperties(mappingFileName).entrySet()) {
            result.put(ResourceKey.create(entry.getKey()), producer.apply(entry));
        }

        result = Collections.unmodifiableMap(result);
        return result;
    }

}
