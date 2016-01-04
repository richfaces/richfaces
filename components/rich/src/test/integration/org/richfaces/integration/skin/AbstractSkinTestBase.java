/*
 * JBoss, Home of Professional Open Source
 * Copyright 2014, Red Hat, Inc. and individual contributors
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
package org.richfaces.integration.skin;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.WebElement;

import com.google.common.collect.Maps;

public abstract class AbstractSkinTestBase {

    protected URL getBackgroundUrl(WebElement element) {
        String backgroundImage = element.getCssValue("background-image");
        Matcher matcher = Pattern.compile("url\\((.*)\\)").matcher(backgroundImage);
        if (!matcher.matches()) {
            throw new IllegalArgumentException(String.format("the background-image property '%s' does not match the regexp", backgroundImage));
        }
        String urlString = matcher.group(1);
        urlString = urlString.replaceAll("(^\"|\"$)", "");
        try {
            return new URL(urlString);
        } catch (MalformedURLException e) {
            throw new IllegalStateException(String.format("the url '%s' isn't valid URL", urlString), e);
        }
    }

    protected Map<String, String> parseQueryParameters(URL url) {
        HashMap<String, String> map = Maps.newHashMap();
        int indexOfEq;
        for (String paramString : url.getQuery().split("&")) {
            indexOfEq = paramString.indexOf('=');
            map.put(paramString.substring(0, indexOfEq), paramString.substring(indexOfEq + 1));
        }
        return map;
    }
}
