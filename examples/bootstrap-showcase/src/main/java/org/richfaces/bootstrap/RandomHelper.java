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

package org.richfaces.bootstrap;

import java.util.Random;

public final class RandomHelper {
    private RandomHelper() {

    }

    public static int genRand() {
        return rand(1, 10000);
    }

    public static int rand(int lo, int hi) {
        Random rn2 = new Random();
        int n = hi - lo + 1;
        int i = rn2.nextInt() % n;

        if (i < 0) {
            i = -i;
        }

        return lo + i;
    }

    public static String randomstring(int lo, int hi) {
        int n = rand(lo, hi);
        byte[] b = new byte[n];

        for (int i = 0; i < n; i++) {
            b[i] = (byte) rand('A', 'Z');
        }

        return new String(b);
    }
}
