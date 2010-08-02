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

package org.ajax4jsf.util;

@Deprecated
public final class Zipper {
    private Zipper() {
    }

    public static void zip(byte[] buf, int value, int offset) {
        buf[offset] = (byte) (value & 0x0ff);
        buf[offset + 1] = (byte) ((value & 0x0ff00) >> 8);
        buf[offset + 2] = (byte) ((value & 0x0ff0000) >> 16);
    }

    public static int unzip(byte[] buf, int offset) {
        int r0 = buf[offset] & 0x0ff;
        int r1 = (buf[offset + 1] << 8) & 0x0ff00;
        int r2 = (buf[offset + 2] << 16) & 0x0ff0000;
        int ret = r0 | r1 | r2;

        return ret;
    }
}
