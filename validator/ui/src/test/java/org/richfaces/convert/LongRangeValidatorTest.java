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

import javax.faces.validator.LongRangeValidator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.richfaces.appplication.FacesMessages;

/**
 * @author Pavel Yaschenko
 * 
 */
@RunWith(ValidatorTestRunner.class)
public class LongRangeValidatorTest extends BaseTest {

	private Enum<?>[] messages = { FacesMessages.LONG_RANGE_VALIDATOR_MAXIMUM, FacesMessages.LONG_RANGE_VALIDATOR_MINIMUM, FacesMessages.LONG_RANGE_VALIDATOR_NOT_IN_RANGE, FacesMessages.LONG_RANGE_VALIDATOR_TYPE };
	
	private void setup() {
        setClientFunction("RichFaces.csv.getValidator('long-range')");
        setObjectId(LongRangeValidator.VALIDATOR_ID);
        setErrorMessageEnums(messages);
	}
	
    public LongRangeValidatorTest() {
        super("META-INF/resources/org.richfaces/long-range-validator.js");
    }

    @Test
    @TestDataHolder(
        successes = {
            @TestData(submittedValue = "5"),
            @TestData(submittedValue = "7"),
            @TestData(submittedValue = "10")
        },
        failures = {
            @TestData(submittedValue = "4"),
            @TestData(submittedValue = "11"),
            @TestData(submittedValue = "aaa")
        }
    )
    
    public void minMaxTest() throws Exception {
        setup();
        setAttribute("maximum", 10);
        setAttribute("minimum", 5);
    }
    
    @Test
    @TestDataHolder(
        successes = {
            @TestData(submittedValue = "10"),
            @TestData(submittedValue = "11")
        },
        failures = {
            @TestData(submittedValue = "9")
        }
    )
    
    public void minTest() throws Exception {
        setup();
        setAttribute("minimum", 10);
    }
    
    @Test
    @TestDataHolder(
        successes = {
            @TestData(submittedValue = "10"),
            @TestData(submittedValue = "9")
        },
        failures = {
            @TestData(submittedValue = "11")
        }
    )
    
    public void maxTest() throws Exception {
        setup();
        setAttribute("maximum", 10);
    }
}
