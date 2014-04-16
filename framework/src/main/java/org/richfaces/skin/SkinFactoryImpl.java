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
package org.richfaces.skin;

import static org.richfaces.application.configuration.ConfigurationServiceHelper.getConfigurationValue;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.faces.context.FacesContext;

import org.ajax4jsf.Messages;
import org.richfaces.configuration.CoreConfiguration;

/**
 * Implementation of {@link SkinFactory} with building skins from properties files.
 *
 * @author shura
 */
public class SkinFactoryImpl extends AbstractSkinFactory {
    private static final String SKIN_KEY = SkinFactoryImpl.class.getName() + ":skin";
    private static final String BASE_SKIN_KEY = SkinFactoryImpl.class.getName() + ":baseSkin";

    // private static final String DEFAULT_CONFIGURATION_RESOURCE = "META-INF/skins/DEFAULT.configuration.properties";
    /**
     * Name of default skin . "DEFAULT" in this realisation.
     */
    private static final String DEFAULT_SKIN_NAME = "DEFAULT";
    private static final String[] THEME_PATHS = { "META-INF/themes/%s.theme.properties", "%s.theme.properties" };
    // private Properties defaultSkinProperties = null;
    private Map<String, Theme> themes = new HashMap<String, Theme>();

    public Skin getDefaultSkin(FacesContext context) {
        return getSkin(context, DEFAULT_SKIN_NAME);
    }

    public Skin getSkin(FacesContext context) {
        Skin skin = (Skin) context.getAttributes().get(SKIN_KEY);
        if (skin == null) {
            Skin mainSkin = getSkinOrName(context, false);
            Skin baseSkin = getSkinOrName(context, true);

            if (mainSkin != null || baseSkin != null) {
                skin = new CompositeSkinImpl(mainSkin, baseSkin);
            } else {
                // CompositeSkinImpl caches hash code
                skin = new CompositeSkinImpl(getDefaultSkin(context));
            }

            context.getAttributes().put(SKIN_KEY, skin);
        }

        return skin;
    }

    public Skin getBaseSkin(FacesContext context) {
        Skin skin = (Skin) context.getAttributes().get(BASE_SKIN_KEY);
        if (skin == null) {
            Skin baseSkin = getSkinOrName(context, true);

            if (baseSkin != null) {
                skin = new CompositeSkinImpl(baseSkin);
            } else {
                // CompositeSkinImpl caches hash code
                skin = new CompositeSkinImpl(getDefaultSkin(context));
            }

            context.getAttributes().put(BASE_SKIN_KEY, skin);
        }

        return skin;
    }

    static void clearSkinCaches(FacesContext context) {
        context.getAttributes().remove(BASE_SKIN_KEY);
        context.getAttributes().remove(SKIN_KEY);
    }

    // protected Properties getDefaultSkinProperties() {
    // if (defaultSkinProperties == null) {
    // defaultSkinProperties = loadProperties(DEFAULT_SKIN_NAME,DEFAULT_SKIN_PATHS);
    // }
    // return defaultSkinProperties;
    // }

    /**
     * Calculate name for current skin. For EL init parameter store value binding for speed calculations.
     *
     * @param context
     * @param useBase
     * @return name of currens skin from init parameter ( "DEFAULT" if no parameter ) or {@link Skin } as result of evaluation EL
     *         expression.
     */
    protected Skin getSkinOrName(FacesContext context, boolean useBase) {
        Object skinObject = getConfigurationValue(context, useBase ? CoreConfiguration.Items.baseSkin
            : CoreConfiguration.Items.skin);

        Skin result = null;

        if (skinObject instanceof Skin) {
            result = (Skin) skinObject;
        } else if (skinObject != null) {
            result = getSkin(context, (String) skinObject);
        }

        return result;
    }

    @Override
    public Theme getTheme(FacesContext facesContext, String name) {
        Theme theme = themes.get(name);

        if (null == theme) {
            Properties properties;

            try {
                properties = loadProperties(name, THEME_PATHS);
            } catch (SkinNotFoundException e) {
                throw new ThemeNotFoundException(Messages.getMessage(Messages.THEME_NOT_FOUND_ERROR, name), e.getCause());
            }

            processProperties(facesContext, properties);
            theme = new ThemeImpl(properties);
            themes.put(name, theme);
        }

        return theme;
    }
}
