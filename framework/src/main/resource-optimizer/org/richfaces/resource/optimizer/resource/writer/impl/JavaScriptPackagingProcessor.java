/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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
package org.richfaces.resource.optimizer.resource.writer.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;

import org.richfaces.resource.optimizer.resource.writer.ResourceProcessor;

import com.google.common.io.ByteStreams;
import com.google.common.io.Closeables;
import com.google.common.io.InputSupplier;
import com.google.common.io.OutputSupplier;

/**
 * @author Nick Belaevski
 * 
 */
public class JavaScriptPackagingProcessor implements ResourceProcessor {
    private Charset charset;

    public JavaScriptPackagingProcessor(Charset charset) {
        this.charset = charset;
    }

    @Override
    public boolean isSupportedFile(String name) {
        return name.endsWith(".js");
    }
    
    @Override
    public void process(String resourceName, InputSupplier<? extends InputStream> in,
            OutputSupplier<? extends OutputStream> out, boolean closeAtFinish) throws IOException {
        process(resourceName, in.getInput(), out.getOutput(), closeAtFinish);
    }

    @Override
    public void process(String resourceName, InputStream in, OutputStream out, boolean closeAtFinish) throws IOException {
        
        Reader reader = null;
        Writer writer = null;

        try {
            reader = new InputStreamReader(in, charset);
            writer = new OutputStreamWriter(out, charset);

            ByteStreams.copy(in, out);
            if (!closeAtFinish) {
                // add semicolon to satisfy end of context of each script when packing files
                writer.write(";");
                writer.flush();
            }
        } finally {
            Closeables.closeQuietly(reader);
            if (closeAtFinish) {
                Closeables.closeQuietly(writer);
            } else {
                writer.flush();
            }
        }
    }
}
