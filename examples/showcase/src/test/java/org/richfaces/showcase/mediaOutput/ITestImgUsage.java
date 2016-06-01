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
package org.richfaces.showcase.mediaOutput;

import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.page.Page;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.richfaces.showcase.AbstractWebDriverTest;
import org.richfaces.showcase.mediaOutput.page.ImageState;
import org.richfaces.showcase.mediaOutput.page.ImgUsagePage;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class ITestImgUsage extends AbstractWebDriverTest {

    @Page
    private ImgUsagePage page;

    @Test
    public void testStates() throws Exception {
        Select leftColor = new Select(page.getSelectLeftColor());
        Select rightColor = new Select(page.getSelectRightColor());
        Select textColor = new Select(page.getSelectTextColor());
        for (ImageState state : ImageState.values()) {
            leftColor.selectByIndex(state.getLeftColor().getIndex());
            rightColor.selectByIndex(state.getRightColor().getIndex());
            textColor.selectByIndex(state.getTextColor().getIndex());
            Graphene.guardAjax(page.getSubmitButton()).click();
            testImage(
                state.getLeftColor().getValue(),
                state.getRightColor().getValue(),
                state.getTextColor().getValue());
        }
    }

    private void testImage(final long expectedRightBottomCornerColor, final long expectedLeftTopCornerColor, final long expectedTextColor)
        throws Exception {
        Graphene.waitModel()
            .until(new ExpectedCondition<Boolean>() {
                @Override
                public Boolean apply(WebDriver f) {
                    try {
                        URL imageUrl = new URL(page.getImage().getAttribute("src"));
                        BufferedImage image = ImageIO.read(imageUrl);
                        int widthOfImage = image.getWidth();
                        int heightOfImage = image.getHeight();
                        return image.getRGB(0, 0) == expectedLeftTopCornerColor && image.getRGB(widthOfImage - 1, heightOfImage - 1) == expectedRightBottomCornerColor && image.getRGB(95, 45) == expectedTextColor;
                    } catch (Exception ignored) {
                        return false;
                    }
                }
            });
    }
}
