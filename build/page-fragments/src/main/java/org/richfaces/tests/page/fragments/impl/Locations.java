/**
 * JBoss, Home of Professional Open Source
 * Copyright 2012-2013, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.page.fragments.impl;

import java.util.Arrays;
import java.util.Iterator;

import org.openqa.selenium.Point;

import com.google.common.base.Objects;

/**
 * Helper class for storing locations of borders.
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class Locations implements Iterable<Point> {

    private final Point topLeft;
    private final Point topRight;
    private final Point bottomLeft;
    private final Point bottomRight;

    public Locations(Point topLeft, Point topRight, Point bottomLeft, Point bottomRight) {
        this.topLeft = topLeft;
        this.topRight = topRight;
        this.bottomLeft = bottomLeft;
        this.bottomRight = bottomRight;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Locations other = (Locations) obj;
        if (!this.topLeft.equals(other.topLeft)) {
            return false;
        }
        if (!this.topRight.equals(other.topRight)) {
            return false;
        }
        if (!this.bottomLeft.equals(other.bottomLeft)) {
            return false;
        }
        if (!this.bottomRight.equals(other.bottomRight)) {
            return false;
        }
        return true;
    }

    public Point getBottomLeft() {
        return bottomLeft;
    }

    public Point getBottomRight() {
        return bottomRight;
    }

    public int getHeight() {
        return bottomRight.y - topRight.y;
    }

    public Point getTopLeft() {
        return topLeft;
    }

    public Point getTopRight() {
        return topRight;
    }

    public int getWidth() {
        return topRight.x - topLeft.x;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(bottomLeft, bottomRight, topLeft, topRight);
    }

    @Override
    public Iterator<Point> iterator() {
        return Arrays.asList(topLeft, topRight, bottomLeft, bottomRight).iterator();
    }

    /**
     * Returns new instance with moved borders.
     */
    public Locations moveAllBy(int x, int y) {
        return new Locations(topLeft.moveBy(x, y), topRight.moveBy(x, y),
                bottomLeft.moveBy(x, y), bottomRight.moveBy(x, y));
    }

    /**
     * Returns new instance with resized borders form bottom right corner.
     * The top left point will stay unchanged, other borders will change.
     */
    public Locations resizeFromBottomRight(int byXPixels, int byYPixels) {
        return new Locations(topLeft, topRight.moveBy(byXPixels, 0),
                bottomLeft.moveBy(0, byYPixels), bottomRight.moveBy(byXPixels, byYPixels));
    }

    @Override
    public String toString() {
        return "Locations{" + "topLeft=" + topLeft + ", topRight=" + topRight + ", bottomLeft=" + bottomLeft + ", bottomRight=" + bottomRight + '}';
    }
}
