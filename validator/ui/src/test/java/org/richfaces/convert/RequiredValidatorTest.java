/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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
package org.richfaces.convert;

import javax.faces.validator.RequiredValidator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.richfaces.appplication.FacesMessages;

/**
 * @author Pavel Yaschenko
 * 
 */
@RunWith(ValidatorTestRunner.class)
public class RequiredValidatorTest extends BaseTest {

	private Enum<?>[] messages = { FacesMessages.UIINPUT_REQUIRED };
	
	private void setup() {
        setClientFunction("RichFaces.csv.getValidator('required')");
        setObjectId(RequiredValidator.VALIDATOR_ID);
        setErrorMessageEnums(messages);
	}
	
    public RequiredValidatorTest() {
        super("META-INF/resources/org.richfaces/required-validator.js");
    }

    @Test
    @TestDataHolder(
        successes = {
            @TestData(submittedValue = "foo")
        },
        failures = {
            @TestData(submittedValue = "")
        }
    )
    
    public void test() throws Exception {
        setup();
    }
    
    @Test
    @TestDataHolder(
        successes = {
        },
        failures = {
            @TestData(submittedValue = "")
        }
    )
    
    public void customMessageTest() throws Exception {
        setup();
        setErrorMessage("Input message: value is required");
    }
}
