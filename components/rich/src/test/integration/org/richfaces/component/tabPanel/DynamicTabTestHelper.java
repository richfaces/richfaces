package org.richfaces.component.tabPanel;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;

import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.richfaces.fragment.tabPanel.RichFacesTabPanel;

public class DynamicTabTestHelper {

    public void check_tab_switch(RichFacesTabPanel tabPanel, WebElement a4jCreateTabButton) {
        List<WebElement> tabs = tabPanel.advanced().getTabHeaders();
        Assert.assertEquals(6, tabs.size());
        tabs.get(1).click();
        Assert.assertEquals("content of tab 0", tabPanel.advanced().getVisibleContent().getText());

        guardAjax(tabs.get(2)).click();
        Assert.assertEquals("content of tab 2", tabPanel.advanced().getVisibleContent().getText());

        guardAjax(tabs.get(4)).click();
        Assert.assertEquals("content of tab 4", tabPanel.advanced().getVisibleContent().getText());

        guardAjax(tabs.get(5)).click();
        Assert.assertEquals("content of tab 5", tabPanel.advanced().getVisibleContent().getText());

        guardAjax(tabs.get(0)).click();
        Assert.assertEquals("content of tab 0", tabPanel.advanced().getVisibleContent().getText());

        guardAjax(a4jCreateTabButton).click();
        Assert.assertEquals(7, tabs.size());

        guardAjax(tabs.get(6)).click();
        Assert.assertEquals("content of tab 6", tabPanel.advanced().getVisibleContent().getText());

        guardAjax(tabs.get(0)).click();
        Assert.assertEquals("content of tab 0",tabPanel.advanced().getVisibleContent().getText());

        WebElement removeLink = tabs.get(6).findElement(By.tagName("a"));
        guardAjax(removeLink).click();
        Assert.assertEquals(6, tabs.size());
    }


    public void check_row_removal(RichFacesTabPanel tabPanel, WebElement a4jCreateTabButton) {
        List<WebElement> tabs = tabPanel.advanced().getTabHeaders();
        Assert.assertEquals(6, tabs.size());

        guardAjax(a4jCreateTabButton).click();
        guardAjax(a4jCreateTabButton).click();
        guardAjax(a4jCreateTabButton).click();

        Assert.assertEquals(9, tabs.size());

        WebElement removeLink = tabs.get(8).findElement(By.tagName("a"));
        guardAjax(removeLink).click();
        Assert.assertEquals(8, tabs.size());

        removeLink = tabs.get(7).findElement(By.tagName("a"));
        guardAjax(removeLink).click();
        Assert.assertEquals(7, tabs.size());

        removeLink = tabs.get(6).findElement(By.tagName("a"));
        guardAjax(removeLink).click();
        Assert.assertEquals(6, tabs.size());
    }
}
