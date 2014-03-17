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
package org.ajax4jsf.component;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * <br /><br />
 *
 * Created 03.09.2007
 * @author Nick Belaevski
 * @since 3.1
 */
public class MediaOutputComponentBaseTest extends org.ajax4jsf.tests.AbstractAjax4JsfTestCase {
    public MediaOutputComponentBaseTest(String name) {
        super(name);
    }

    protected Date createTestData_lastModified() {
        Calendar calendar = Calendar.getInstance();

        calendar.clear();
        calendar.set(Calendar.YEAR, 2007);
        calendar.set(Calendar.MONTH, Calendar.SEPTEMBER);
        calendar.set(Calendar.DATE, 3);
        calendar.set(Calendar.HOUR, 22);
        calendar.set(Calendar.MINUTE, 12);
        calendar.set(Calendar.SECOND, 40);

        return calendar.getTime();
    }

    protected Date createTestData_expires() {
        Calendar calendar = Calendar.getInstance();

        calendar.clear();
        calendar.set(Calendar.YEAR, 2007);
        calendar.set(Calendar.MONTH, Calendar.SEPTEMBER);
        calendar.set(Calendar.DATE, 8);
        calendar.set(Calendar.HOUR, 11);
        calendar.set(Calendar.MINUTE, 56);
        calendar.set(Calendar.SECOND, 9);

        return calendar.getTime();
    }

    public void testVoid() throws Exception {

        // empty
    }
}
