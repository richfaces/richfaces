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

import javax.faces.validator.DoubleRangeValidator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.richfaces.appplication.FacesMessages;

/**
 * @author Pavel Yaschenko
 * 
 */
@RunWith(ValidatorTestRunner.class)
public class DoubleRangeValidatorTest extends BaseTest {

	private Enum<?>[] messages = { FacesMessages.DOUBLE_RANGE_VALIDATOR_MAXIMUM, FacesMessages.DOUBLE_RANGE_VALIDATOR_MINIMUM, FacesMessages.DOUBLE_RANGE_VALIDATOR_NOT_IN_RANGE, FacesMessages.DOUBLE_RANGE_VALIDATOR_TYPE };
	
	private void setup() {
        setClientFunction("RichFaces.csv.getValidator('double-range')");
        setObjectId(DoubleRangeValidator.VALIDATOR_ID);
        setErrorMessageEnums(messages);
	}
	
    public DoubleRangeValidatorTest() {
        super("META-INF/resources/org.richfaces/double-range-validator.js");
    }
    
    @Test
    @TestDataHolder(
        successes = {
            @TestData(submittedValue = "1.e10"),
            @TestData(submittedValue = "1.e-10"),
            @TestData(submittedValue = ".e10"),
            @TestData(submittedValue = "0.1e10"),
            @TestData(submittedValue = "0.1e-10"),
            @TestData(submittedValue = "-0.1e-10")
        },
        failures = {
        }
    )
    
    public void conversionTest() throws Exception {
        setup();
    }

    @Test
    @TestDataHolder(
        successes = {
            @TestData(submittedValue = "0.9"),
            @TestData(submittedValue = "1.1"),
            @TestData(submittedValue = "1.2")
        },
        failures = {
            @TestData(submittedValue = "1.3"),
            @TestData(submittedValue = "0.8"),
            @TestData(submittedValue = "aaa")
        }
    )
    
    public void minMaxTest() throws Exception {
        setup();
        setAttribute("maximum", 1.2);
        setAttribute("minimum", 0.9);
    }
    
    @Test
    @TestDataHolder(
        successes = {
            @TestData(submittedValue = "1.2"),
            @TestData(submittedValue = "1.3")
        },
        failures = {
            @TestData(submittedValue = "1.1")
        }
    )
    
    public void minTest() throws Exception {
        setup();
        setAttribute("minimum", 1.2);
    }
    
    @Test
    @TestDataHolder(
        successes = {
            @TestData(submittedValue = "1.1"),
            @TestData(submittedValue = "0.9")
        },
        failures = {
            @TestData(submittedValue = "1.2")
        }
    )
    
    public void maxTest() throws Exception {
        setup();
        setAttribute("maximum", 1.1);
    }
}
