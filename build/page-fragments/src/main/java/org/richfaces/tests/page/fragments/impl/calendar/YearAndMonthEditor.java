/**
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.page.fragments.impl.calendar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.Validate;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.ByJQuery;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.fragment.Root;
import org.jboss.arquillian.graphene.wait.FluentWait;
import org.joda.time.DateTime;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.page.fragments.impl.Utils;
import org.richfaces.tests.page.fragments.impl.utils.WaitingWrapper;
import org.richfaces.tests.page.fragments.impl.utils.WaitingWrapperImpl;

import com.google.common.collect.Lists;

/**
 * Component for calendar's year and month editor.
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class YearAndMonthEditor {

    @Root
    private WebElement root;

    @Drone
    private WebDriver browser;

    @FindByJQuery("div[id*='DateEditorLayoutM']:even")
    private List<WebElement> monthsEven;
    @FindByJQuery("div[id*='DateEditorLayoutM']:odd")
    private List<WebElement> monthsOdd;
    @FindBy(css = "div[id*='DateEditorLayoutM'].rf-cal-edtr-btn-sel")
    private WebElement selectedMonth;

    @FindBy(css = "div[id*='DateEditorLayoutY']")
    private List<WebElement> years;
    @FindByJQuery("div[id*='DateEditorLayoutY']:even")
    private List<WebElement> yearsEven;
    @FindByJQuery("div[id*='DateEditorLayoutY']:odd")
    private List<WebElement> yearsOdd;
    @FindBy(css = "div[id*='DateEditorLayoutY0']")
    private WebElement firstYear;
    @FindBy(css = "div[id*='DateEditorLayoutY'].rf-cal-edtr-btn-sel")
    private WebElement selectedYear;

    @FindByJQuery("[id$='DateEditorLayoutTR'] > td > div:eq(2)")
    private WebElement previousDecadeButtonElement;
    @FindByJQuery("[id$='DateEditorLayoutTR'] > td > div:eq(3)")
    private WebElement nextDecadeButtonElement;
    @FindBy(css = "div[id$=DateEditorButtonOk]")
    private WebElement okButtonElement;
    @FindBy(css = "div[id$=DateEditorButtonCancel]")
    private WebElement cancelButtonElement;

    private static final String SELECTED = "rf-cal-edtr-btn-sel";

    private long _timeoutForYearAndMonthEditorToBeNotVisible = -1;
    private long _timeoutForYearAndMonthEditorToBeVisible = -1;

    public void cancelDate() {
        cancelButtonElement.click();
        waitUntilIsNotVisible().perform();
    }

    public void confirmDate() {
        okButtonElement.click();
        waitUntilIsNotVisible().perform();
    }

    public WebElement getCancelButtonElement() {
        return cancelButtonElement;
    }

    public DateTime getDate() {
        return new DateTime().withMonthOfYear(getSelectedMonthNumber()).withYear(getSelectedYearNumber());
    }

    public List<Integer> getDisplayedYears() {
        List<WebElement> inRightOrder = Lists.newArrayList(yearsEven);
        inRightOrder.addAll(yearsOdd);
        List<Integer> result = new ArrayList<Integer>(10);
        for (WebElement webElement : inRightOrder) {
            result.add(Integer.parseInt(webElement.getText().trim()));
        }
        return result;
    }

    public WebElement getNextDecadeButtonElement() {
        return nextDecadeButtonElement;
    }

    public WebElement getOkButtonElement() {
        return okButtonElement;
    }

    public WebElement getPreviousDecadeButtonElement() {
        return previousDecadeButtonElement;
    }

    public WebElement getRoot() {
        return root;
    }

    public Integer getSelectedMonthNumber() {
        if (Utils.isVisible(selectedMonth)) {
            String id = selectedMonth.getAttribute("id");
            return Integer.parseInt(id.substring(id.lastIndexOf("M") + 1))
                + 1;// indexed from 0
        }
        return null;
    }

    public Integer getSelectedYearNumber() {
        if (Utils.isVisible(selectedYear)) {
            String year = selectedYear.getText();
            return Integer.parseInt(year);
        }
        return null;
    }

    public List<String> getShortMonthsLabels() {
        List<WebElement> inRightOrder = Lists.newArrayList(monthsEven);
        inRightOrder.addAll(monthsOdd);
        List<String> result = new ArrayList<String>(12);
        for (WebElement webElement : inRightOrder) {
            result.add(webElement.getText().trim());
        }
        return result;
    }

    public boolean isVisible() {
        return Utils.isVisible(root);
    }

    public void nextDecade() {
        String firstBefore = firstYear.getText();
        nextDecadeButtonElement.click();
        Graphene.waitGui().withMessage("The decade was not changed.").until().element(firstYear).text().not().equalTo(firstBefore);
    }

    public void previousDecade() {
        String firstBefore = firstYear.getText();
        previousDecadeButtonElement.click();
        Graphene.waitGui().withMessage("The decade was not changed.").until().element(firstYear).text().not().equalTo(firstBefore);
    }

    public YearAndMonthEditor selectDate(DateTime date) {
        return selectDate(date.getMonthOfYear(), date.getYear());
    }

    private YearAndMonthEditor selectDate(int month, int year) {
        Validate.isTrue(month > 0 && month < 13, "Month number has to be in interval <1,12>");//this should not be necessary
        selectMonth(month);
        selectYear(year);
        return this;
    }

    private void selectMonth(int month) {
        WebElement monthElement = root.findElement(By.cssSelector("div[id*='DateEditorLayoutM" + (month - 1) + "']"));
        monthElement.click();
        Graphene.waitGui().withMessage("The month was not selected.").until().element(monthElement).attribute("class").contains(SELECTED);
    }

    private void selectYear(int year) {
        int yearsPickerSize = years.size();
        int yearInTheFirstColumn = Integer.parseInt(firstYear.getText());
        int diff = year - yearInTheFirstColumn;
        // scroll to year
        if (diff > 0 && diff >= yearsPickerSize) {
            while (diff > 0) {
                nextDecade();
                diff -= yearsPickerSize;
            }
        } else {
            while (diff < 0) {
                previousDecade();
                diff += yearsPickerSize;
            }
        }
        // select year
        WebElement yearElement = root.findElement(ByJQuery.selector("div[id*='DateEditorLayoutY']:contains('" + year + "')"));
        yearElement.click();
        Graphene.waitGui().withMessage("The year was not selected.").until().element(yearElement).attribute("class").contains(SELECTED);
    }

    public void setuptimeoutForYearAndMonthEditorToBeNotVisible(long timeoutInMilliseconds) {
        _timeoutForYearAndMonthEditorToBeNotVisible = timeoutInMilliseconds;
    }

    public long getTimeoutForYearAndMonthEditorToBeNotVisible() {
        return _timeoutForYearAndMonthEditorToBeNotVisible == -1 ? Utils.getWaitAjaxDefaultTimeout(browser) : _timeoutForYearAndMonthEditorToBeNotVisible;
    }

    public void setupTimeoutForYearAndMonthEditorToBeVisible(long timeoutInMilliseconds) {
        _timeoutForYearAndMonthEditorToBeVisible = timeoutInMilliseconds;
    }

    public long getTimeoutForYearAndMonthEditorToBeVisible() {
        return _timeoutForYearAndMonthEditorToBeVisible == -1 ? Utils.getWaitAjaxDefaultTimeout(browser) : _timeoutForYearAndMonthEditorToBeVisible;
    }

    public WaitingWrapper waitUntilIsNotVisible() {
        return new WaitingWrapperImpl() {
            @Override
            protected void performWait(FluentWait<WebDriver, Void> wait) {
                wait.until().element(root).is().not().visible();
            }
        }.withMessage("Year and month editor to be not visible.")
         .withTimeout(getTimeoutForYearAndMonthEditorToBeNotVisible(), TimeUnit.MILLISECONDS);
    }

    public WaitingWrapper waitUntilIsVisible() {
        return new WaitingWrapperImpl() {
            @Override
            protected void performWait(FluentWait<WebDriver, Void> wait) {
                wait.until().element(root).is().visible();
            }
        }.withMessage("Year and month editor to be not visible.")
         .withTimeout(getTimeoutForYearAndMonthEditorToBeVisible(), TimeUnit.MILLISECONDS);
    }
}
