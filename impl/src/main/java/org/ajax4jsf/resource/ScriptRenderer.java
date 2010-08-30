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

package org.ajax4jsf.resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.ajax4jsf.Messages;
import org.ajax4jsf.javascript.JSMin;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;

class CountingOutputStream extends OutputStream {
    private int written = 0;
    private OutputStream outputStream;

    CountingOutputStream(OutputStream outputStream) {
        super();
        this.outputStream = outputStream;
    }

    public void close() throws IOException {
        outputStream.close();
    }

    public void flush() throws IOException {
        outputStream.flush();
    }

    public void write(byte[] b, int off, int len) throws IOException {
        outputStream.write(b, off, len);
        written += len;
    }

    public void write(byte[] b) throws IOException {
        outputStream.write(b);
        written += b.length;
    }

    public void write(int b) throws IOException {
        outputStream.write(b);
        written++;
    }

    int getWritten() {
        return written;
    }
}

/**
 * @author asmirnov@exadel.com (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.1 $ $Date: 2007/01/09 18:57:04 $
 */
public class ScriptRenderer extends CompressedScriptRenderer {
    private static final String COMPRESS_SCRIPTS_PARAMETER = "org.ajax4jsf.COMPRESS_SCRIPT";
    private static final Logger LOG = RichfacesLogger.RESOURCE.getLogger();

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.resource.BaseResourceRenderer#send(org.ajax4jsf.resource.InternetResource,
     * org.ajax4jsf.resource.ResourceContext)
     */
    @Override
    public int send(InternetResource base, ResourceContext context) throws IOException {
        InputStream in = base.getResourceAsStream(context);

        if (null == in) {
            String message = Messages.getMessage(Messages.NO_INPUT_STREAM_ERROR, base.getKey());

            throw new IOException(message);
        }

        OutputStream out = context.getOutputStream();

        // Compress JavaScript output by JSMin ( true by default )
        if (!"false".equalsIgnoreCase(context.getInitParameter(COMPRESS_SCRIPTS_PARAMETER))) {
            CountingOutputStream countingStream = new CountingOutputStream(out);
            JSMin jsmin = new JSMin(in, countingStream);

            try {
                jsmin.jsmin();
            } catch (Exception e) {
                LOG.error("Error send script to client for resource " + base.getKey(), e);
            } finally {
                in.close();
                countingStream.flush();
                countingStream.close();
            }

            int written = countingStream.getWritten();

            if (LOG.isDebugEnabled()) {
                LOG.debug("Send " + written + " bytes to client for JavaScript resource " + base.getKey());
            }

            return written;
        } else {
            return sendStream(in, out);
        }
    }
}
