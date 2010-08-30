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
package org.richfaces.application;

import javax.faces.application.FacesMessage;

import org.richfaces.l10n.MessageBundle;

/**
 * @author Nick Belaevski
 * 
 */
@MessageBundle(baseName = FacesMessage.FACES_MESSAGES)
public enum FacesMessages {

    UIINPUT_CONVERSION("javax.faces.component.UIInput.CONVERSION"),
    UIINPUT_REQUIRED("javax.faces.component.UIInput.REQUIRED"),
    UIINPUT_UPDATE("javax.faces.component.UIInput.UPDATE"),
    UISELECTONE_INVALID("javax.faces.component.UISelectOne.INVALID"),
    UISELECTMANY_INVALID("javax.faces.component.UISelectMany.INVALID"),
    BIG_DECIMAL_CONVERTER_DECIMAL("javax.faces.converter.BigDecimalConverter.DECIMAL"),
    BIG_DECIMAL_CONVERTER_DECIMAL_DETAIL("javax.faces.converter.BigDecimalConverter.DECIMAL_detail"),
    BIG_INTEGER_CONVERTER_BIGINTEGER("javax.faces.converter.BigIntegerConverter.BIGINTEGER"),
    BIG_INTEGER_CONVERTER_BIGINTEGER_DETAIL("javax.faces.converter.BigIntegerConverter.BIGINTEGER_detail"),
    BOOLEAN_CONVERTER_BOOLEAN("javax.faces.converter.BooleanConverter.BOOLEAN"),
    BOOLEAN_CONVERTER_BOOLEAN_DETAIL("javax.faces.converter.BooleanConverter.BOOLEAN_detail"),
    BYTE_CONVERTER_BYTE("javax.faces.converter.ByteConverter.BYTE"),
    BYTE_CONVERTER_BYTE_DETAIL("javax.faces.converter.ByteConverter.BYTE_detail"),
    CHARACTER_CONVERTER_CHARACTER("javax.faces.converter.CharacterConverter.CHARACTER"),
    CHARACTER_CONVERTER_CHARACTER_DETAIL("javax.faces.converter.CharacterConverter.CHARACTER_detail"),
    DATE_TIME_CONVERTER_DATE("javax.faces.converter.DateTimeConverter.DATE"),
    DATE_TIME_CONVERTER_DATE_DETAIL("javax.faces.converter.DateTimeConverter.DATE_detail"),
    DATE_TIME_CONVERTER_TIME("javax.faces.converter.DateTimeConverter.TIME"),
    DATE_TIME_CONVERTER_TIME_DETAIL("javax.faces.converter.DateTimeConverter.TIME_detail"),
    DATE_TIME_CONVERTER_DATETIME("javax.faces.converter.DateTimeConverter.DATETIME"),
    DATE_TIME_CONVERTER_DATETIME_DETAIL("javax.faces.converter.DateTimeConverter.DATETIME_detail"),
    DATE_TIME_CONVERTER_PATTERN_TYPE("javax.faces.converter.DateTimeConverter.PATTERN_TYPE"),
    DOUBLE_CONVERTER_DOUBLE("javax.faces.converter.DoubleConverter.DOUBLE"),
    DOUBLE_CONVERTER_DOUBLE_DETAIL("javax.faces.converter.DoubleConverter.DOUBLE_detail"),
    ENUM_CONVERTER_ENUM("javax.faces.converter.EnumConverter.ENUM"),
    ENUM_CONVERTER_ENUM_DETAIL("javax.faces.converter.EnumConverter.ENUM_detail"),
    ENUM_CONVERTER_ENUM_NO_CLASS("javax.faces.converter.EnumConverter.ENUM_NO_CLASS"),
    ENUM_CONVERTER_ENUM_NO_CLASS_DETAIL("javax.faces.converter.EnumConverter.ENUM_NO_CLASS_detail"),
    FLOAT_CONVERTER_FLOAT("javax.faces.converter.FloatConverter.FLOAT"),
    FLOAT_CONVERTER_FLOAT_DETAIL("javax.faces.converter.FloatConverter.FLOAT_detail"),
    INTEGER_CONVERTER_INTEGER("javax.faces.converter.IntegerConverter.INTEGER"),
    INTEGER_CONVERTER_INTEGER_DETAIL("javax.faces.converter.IntegerConverter.INTEGER_detail"),
    LONG_CONVERTER_LONG("javax.faces.converter.LongConverter.LONG"),
    LONG_CONVERTER_LONG_DETAIL("javax.faces.converter.LongConverter.LONG_detail"),
    NUMBER_CONVERTER_CURRENCY("javax.faces.converter.NumberConverter.CURRENCY"),
    NUMBER_CONVERTER_CURRENCY_DETAIL("javax.faces.converter.NumberConverter.CURRENCY_detail"),
    NUMBER_CONVERTER_PERCENT("javax.faces.converter.NumberConverter.PERCENT"),
    NUMBER_CONVERTER_PERCENT_DETAIL("javax.faces.converter.NumberConverter.PERCENT_detail"),
    NUMBER_CONVERTER_NUMBER("javax.faces.converter.NumberConverter.NUMBER"),
    NUMBER_CONVERTER_NUMBER_DETAIL("javax.faces.converter.NumberConverter.NUMBER_detail"),
    NUMBER_CONVERTER_PATTERN("javax.faces.converter.NumberConverter.PATTERN"),
    NUMBER_CONVERTER_PATTERN_DETAIL("javax.faces.converter.NumberConverter.PATTERN_detail"),
    SHORT_CONVERTER_SHORT("javax.faces.converter.ShortConverter.SHORT"),
    SHORT_CONVERTER_SHORT_DETAIL("javax.faces.converter.ShortConverter.SHORT_detail"),
    CONVERTER_STRING("javax.faces.converter.STRING"),
    DOUBLE_RANGE_VALIDATOR_MAXIMUM("javax.faces.validator.DoubleRangeValidator.MAXIMUM"),
    DOUBLE_RANGE_VALIDATOR_MINIMUM("javax.faces.validator.DoubleRangeValidator.MINIMUM"),
    DOUBLE_RANGE_VALIDATOR_NOT_IN_RANGE("javax.faces.validator.DoubleRangeValidator.NOT_IN_RANGE"),
    DOUBLE_RANGE_VALIDATOR_TYPE("javax.faces.validator.DoubleRangeValidator.TYPE"),
    LENGTH_VALIDATOR_MAXIMUM("javax.faces.validator.LengthValidator.MAXIMUM"),
    LENGTH_VALIDATOR_MINIMUM("javax.faces.validator.LengthValidator.MINIMUM"),
    LONG_RANGE_VALIDATOR_MAXIMUM("javax.faces.validator.LongRangeValidator.MAXIMUM"),
    LONG_RANGE_VALIDATOR_MINIMUM("javax.faces.validator.LongRangeValidator.MINIMUM"),
    LONG_RANGE_VALIDATOR_NOT_IN_RANGE("javax.faces.validator.LongRangeValidator.NOT_IN_RANGE"),
    LONG_RANGE_VALIDATOR_TYPE("javax.faces.validator.LongRangeValidator.TYPE"),
    VALIDATOR_NOT_IN_RANGE("javax.faces.validator.NOT_IN_RANGE"),
    REGEX_VALIDATOR_PATTERN_NOT_SET("javax.faces.validator.RegexValidator.PATTERN_NOT_SET"),
    REGEX_VALIDATOR_PATTERN_NOT_SET_DETAIL("javax.faces.validator.RegexValidator.PATTERN_NOT_SET_detail"),
    REGEX_VALIDATOR_NOT_MATCHED("javax.faces.validator.RegexValidator.NOT_MATCHED"),
    REGEX_VALIDATOR_NOT_MATCHED_DETAIL("javax.faces.validator.RegexValidator.NOT_MATCHED_detail"),
    REGEX_VALIDATOR_MATCH_EXCEPTION("javax.faces.validator.RegexValidator.MATCH_EXCEPTION"),
    REGEX_VALIDATOR_MATCH_EXCEPTION_DETAIL("javax.faces.validator.RegexValidator.MATCH_EXCEPTION_detail"),
    BEAN_VALIDATOR_MESSAGE("javax.faces.validator.BeanValidator.MESSAGE");

    private String key;

    private FacesMessages(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return key;
    }
}
