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

package org.ajax4jsf.util;

import java.awt.*;
import java.nio.ByteBuffer;

/**
 * <br /><br />
 * <p/>
 * Created 21.08.2007
 *
 * @author Nick Belaevski
 * @since 3.1
 */
@Deprecated
public class Zipper2 {
    int offset = 0;
    private byte[] buffer;

    public Zipper2(byte[] buffer) {
        super();
        this.buffer = buffer;
    }

    public Zipper2(byte[] buffer, int offest) {
        super();
        this.buffer = buffer;
        this.offset = offest;
    }

    public Zipper2 addByte(byte b) {
        buffer[offset++] = b;

        return this;
    }

    public Zipper2 addShort(short s) {
        ByteBuffer.wrap(buffer, offset, 2).asShortBuffer().put(s);
        offset += 2;

        return this;
    }

    public Zipper2 addColor(Color color) {
        addColor(color.getRGB());

        return this;
    }

    public Zipper2 addColor(int i) {
        buffer[offset] = (byte) ((i >> 0) & 0xFF);
        buffer[offset + 1] = (byte) ((i >> 8) & 0xFF);
        buffer[offset + 2] = (byte) ((i >> 16) & 0xFF);
        offset += 3;

        return this;
    }

    public Zipper2 addInt(int i) {
        ByteBuffer.wrap(buffer, offset, 4).asIntBuffer().put(i);
        offset += 4;

        return this;
    }

    public Zipper2 addBytes(byte[] bs) {
        if (bs != null) {
            System.arraycopy(bs, 0, buffer, offset, bs.length);
        }

        return this;
    }

    public byte nextByte() {
        return buffer[offset++];
    }

    public short nextShort() {
        short s = ByteBuffer.wrap(buffer, offset, 2).asShortBuffer().get();

        offset += 2;

        return s;
    }

    public Color nextColor() {
        return new Color(nextIntColor());
    }

    public int nextIntColor() {
        int r0 = buffer[offset] & 0x0ff;
        int r1 = (buffer[offset + 1] << 8) & 0x0ff00;
        int r2 = (buffer[offset + 2] << 16) & 0x0ff0000;
        int ret = r0 | r1 | r2;

        offset += 3;

        return ret;
    }

    public int nextInt() {
        int i = ByteBuffer.wrap(buffer, offset, 4).asIntBuffer().get();

        offset += 4;

        return i;
    }

    public byte[] nextBytes() {
        byte[] bs = new byte[buffer.length - offset];

        System.arraycopy(buffer, offset, bs, 0, buffer.length - offset);
        offset = buffer.length;

        return bs;
    }

    public boolean hasMore() {
        return offset < buffer.length;
    }
}
