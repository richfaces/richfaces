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

/**
 *
 */
package org.richfaces.example;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.faces.FacesException;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 * @author leo
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
@ManagedBean
@ApplicationScoped
public class Pages {
    public static final String DEFAULT_TITLE_PATTERN = "<ui\\:param\\s+name=\"title\"\\s+value=\"([^\"]*)\"";
    private static final Pattern XHTML_PATTERN = Pattern.compile(".*\\.xhtml");
    private static final Pattern PARENT_FOLDER_PATTERN = Pattern.compile("(/.*/.*/).*\\.xhtml");
    private static final Pattern FOLDER_PATTERN = Pattern.compile(".*/$");
    private static final String EXAMPLE_PATH = "/examples";
    private Pattern titlePattern = compilePattern(DEFAULT_TITLE_PATTERN);
    private Map<String, List<PageDescriptionBean>> pageFolderMap;
    private List<PageDescriptionBean> indexPages;

    public Pattern compilePattern(String titlePattern) {
        return Pattern.compile(titlePattern, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
    }

    @PostConstruct
    public void init() {
        pageFolderMap = new HashMap<String, List<PageDescriptionBean>>();
        Set<String> resourcePaths = getExternalContext().getResourcePaths(EXAMPLE_PATH);
        indexPages = new ArrayList<PageDescriptionBean>(resourcePaths.size());
        for (Iterator<String> iterator = resourcePaths.iterator(); iterator.hasNext();) {
            String folderPath = iterator.next();
            File folder = new File(folderPath);
            if (FOLDER_PATTERN.matcher(folderPath).matches()) {
                pageFolderMap.put(folderPath, getPagesByPattern(XHTML_PATTERN, folderPath));
                String title = folderPath;
                try {
                    title = folderPath.split("/")[2];
                    title = firstChartToUppercase(title);
                } finally {
                    indexPages.add(new PageDescriptionBean(folderPath + "index.jsf", title));
                }
            }
        }
        indexPages.addAll(getPagesByPattern(XHTML_PATTERN, EXAMPLE_PATH));
    }

    private String firstChartToUppercase(String string) {
        char[] chars = string.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);
        return new String(chars);
    }

    private ExternalContext getExternalContext() {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        ExternalContext externalContext = null;
        if (null != facesContext) {
            externalContext = facesContext.getExternalContext();
        }
        return externalContext;
    }

    public List<PageDescriptionBean> getXhtmlPages() {
        String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();
        Matcher m = PARENT_FOLDER_PATTERN.matcher(viewId);
        String path;
        if (m.find()) {
            path = m.group(1);
        } else {
            return indexPages;
        }
        return pageFolderMap.get(path);
    }

    /**
     *
     */
    private List<PageDescriptionBean> getPagesByPattern(Pattern pattern, String path) {
        List<PageDescriptionBean> pageList = new ArrayList<PageDescriptionBean>();
        Set<String> resourcePaths = getExternalContext().getResourcePaths(path);
        for (Iterator<String> iterator = resourcePaths.iterator(); iterator.hasNext();) {
            String page = iterator.next();
            if (pattern.matcher(page).matches() && !page.endsWith("/index.xhtml")) {
                InputStream pageInputStream = getExternalContext().getResourceAsStream(page);
                String title = page;
                if (null != pageInputStream) {
                    byte[] head = new byte[2048];
                    try {
                        int readed = pageInputStream.read(head);
                        String headString = new String(head, 0, readed);
                        if (title.endsWith("input/")) {
                            System.out.println(headString);
                        }
                        Matcher titleMatcher = titlePattern.matcher(headString);
                        if (titleMatcher.find() && titleMatcher.group(1).length() > 0) {
                            title = titleMatcher.group(1);
                        }
                    } catch (IOException e) {
                        throw new FacesException("can't read directory content", e);
                    } finally {
                        try {
                            pageInputStream.close();
                        } catch (IOException e) {
                            // ignore it.
                        }
                    }
                }
                pageList.add(new PageDescriptionBean(page, title));
            }
        }
        Collections.sort(pageList);
        return pageList;
    }

    /**
     * @param titlePattern the titlePattern to set
     */
    public void setTitlePattern(String titlePattern) {
        this.titlePattern = compilePattern(titlePattern);
    }

    /**
     * @return the titlePattern
     */
    public String getTitlePattern() {
        return titlePattern.toString();
    }
}
