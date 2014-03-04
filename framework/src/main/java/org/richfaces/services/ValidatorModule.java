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
package org.richfaces.services;

import java.util.Map;

import org.richfaces.el.ValueExpressionAnalayserImpl;
import org.richfaces.javascript.ClientScriptService;
import org.richfaces.javascript.ClientScriptServiceImpl;
import org.richfaces.javascript.ClientServiceConfigParser;
import org.richfaces.javascript.LibraryFunction;
import org.richfaces.validator.BeanValidatorService;
import org.richfaces.validator.BeanValidatorServiceImpl;
import org.richfaces.validator.ConverterServiceImpl;
import org.richfaces.validator.DummyBeanValidatorService;
import org.richfaces.validator.FacesConverterService;
import org.richfaces.validator.FacesValidatorService;
import org.richfaces.validator.FacesValidatorServiceImpl;
import org.richfaces.validator.InitializationException;
import org.richfaces.validator.RichFacesBeanValidatorFactory;

/**
 * <p class="changed_added_4_0">
 * This class initializes validator-related services
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
public class ValidatorModule implements Module {
    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.application.Module#configure(org.richfaces.application.ServicesFactory)
     */
    @Override
    public void configure(ServicesFactory factory) {
        configureBeanValidators(factory);
        factory.setInstance(FacesConverterService.class, new ConverterServiceImpl());
        factory.setInstance(FacesValidatorService.class, new FacesValidatorServiceImpl());
        ClientScriptServiceImpl clientScriptService = createClientScriptService();
        factory.setInstance(ClientScriptService.class, clientScriptService);
    }

    private ClientScriptServiceImpl createClientScriptService() {
        Map<Class<?>, LibraryFunction> config = ClientServiceConfigParser.parseConfig("META-INF/csv.xml");
        ClientScriptServiceImpl clientScriptService = new ClientScriptServiceImpl(config);
        return clientScriptService;
    }

    void configureBeanValidators(ServicesFactory factory) {
        BeanValidatorService service;
        try {
            RichFacesBeanValidatorFactory validatorFactory = new RichFacesBeanValidatorFactory();
            validatorFactory.init();
            service = new BeanValidatorServiceImpl(new ValueExpressionAnalayserImpl(), validatorFactory);
        } catch (InitializationException e) {
            // JSR-303 is available but not initialised.
            service = new DummyBeanValidatorService();
        } catch (NoClassDefFoundError e) {
            // JSR-303 is not avalable.
            // log.warn("Validator implementations not found at classpath, default NullValidator will be used.");
            service = new DummyBeanValidatorService();
        }
        factory.setInstance(BeanValidatorService.class, service);
    }
}
