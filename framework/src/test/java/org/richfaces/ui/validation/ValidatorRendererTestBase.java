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

package org.richfaces.ui.validation;

import com.google.common.collect.ImmutableMap;

import org.jboss.test.faces.mock.Mock;
import org.richfaces.resource.ResourceKey;
import org.richfaces.ui.behavior.BehaviorTestBase;
import org.richfaces.ui.validation.validator.ClientValidatorBehavior;
import org.richfaces.ui.validation.validator.ClientValidatorRenderer;
import org.richfaces.validator.Message;

import javax.faces.application.FacesMessage;

import java.util.Collections;
import java.util.Map;

public class ValidatorRendererTestBase extends BehaviorTestBase {
    protected static final String CLIENT_VALIDATORS_JS = "clientValidators.js";
    protected static final String ORG_RICHFACES = "org.richfaces";
    protected static final String REGEX_VALIDATOR = "regexValidator";
    protected static final FacesMessage FACES_VALIDATOR_MESSAGE = new FacesMessage("Validator Message");
    protected static final Message VALIDATOR_MESSAGE = new Message(FACES_VALIDATOR_MESSAGE);
    protected static final Map<String, ? extends Object> VALIDATOR_PARAMS = ImmutableMap.of("foo", "value", "bar", 10);
    protected static final Iterable<ResourceKey> CLIENT_VALIDATOR_LIBRARY = Collections.singleton(ResourceKey.create(
        CLIENT_VALIDATORS_JS, ORG_RICHFACES));
    protected ClientValidatorRenderer renderer = new ClientValidatorRenderer();
    @Mock
    protected ClientValidatorBehavior mockBehavior;

    public ValidatorRendererTestBase() {
        super();
    }
}