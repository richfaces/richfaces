/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2014, Red Hat, Inc. and individual contributors
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
package org.richfaces.showcase.status;

import static java.text.MessageFormat.format;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.javascript.Dependency;
import org.jboss.arquillian.graphene.javascript.JavaScript;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.richfaces.showcase.AbstractWebDriverTest;

import com.google.common.base.Predicate;

/**
 * @author pmensik
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public abstract class TestUsage extends AbstractWebDriverTest {

    private static final String REGEXP_HIDE = "at <[0-9]+> status image was <not visible>";
    private static final String REGEXP_SHOW = "at <[0-9]+> status image was <visible>";

    @JavaScript
    private StatusChangeObserver statusChangeObserver;

    /**
     * @param actionTriggeringStatusToShow
     * @param statusIndex 0 for first status, 1 for second status (only in ITestReferencedUsage)
     */
    protected void assertProgressPictureAppearsOnAjaxRequest(Action actionTriggeringStatusToShow, int statusIndex) {
        assertTrue(statusIndex == 0 || statusIndex == 1);

        statusChangeObserver.watchForChangeOfStatus(statusIndex);
        Graphene.guardAjax(actionTriggeringStatusToShow).perform();
        Graphene.waitAjax().until(new Predicate<WebDriver>() {
            @Override
            public boolean apply(WebDriver t) {
                return statusChangeObserver.getRecords().size() >= 2;
            }
        });

        List<String> records = statusChangeObserver.getRecords();
        assertEquals("There should be 2 records!", 2, records.size());
        assertTrue(format("First record does not match. Expected: record matches <{0}>. Have: record <{1}>", REGEXP_SHOW, records.get(0)), records.get(0).matches(REGEXP_SHOW));
        assertTrue(format("Second record does not match. Expected: record matches <{0}>. Have: record <{1}>", REGEXP_HIDE, records.get(1)), records.get(1).matches(REGEXP_HIDE));

        Pattern pattern = Pattern.compile("([0-9]+)");
        Matcher m1 = pattern.matcher(records.get(0));
        Matcher m2 = pattern.matcher(records.get(1));
        m1.find();
        m2.find();
        assertEquals("Delay between status show and hide should be in interval <400;1200>", 800, Long.valueOf(m2.group(0)) - Long.valueOf(m1.group(0)), 400);
    }

    protected void assertProgressPictureAppearsOnAjaxRequest(Action actionTriggeringStatusToShow) {
        assertProgressPictureAppearsOnAjaxRequest(actionTriggeringStatusToShow, 0);
    }

    protected Action clickButtonAction(WebElement button) {
        return new Actions(webDriver).click(button).build();
    }

    protected Action sendKeysToInputAction(WebElement input) {
        return new Actions(webDriver).sendKeys(input, "a").build();
    }

    @JavaScript("statusChangeObserver")
    @Dependency(sources = "javascript/statusChangeObserver.js")
    public interface StatusChangeObserver {

        List<String> getRecords();

        void watchForChangeOfStatus(int statusIndex);
    }
}
