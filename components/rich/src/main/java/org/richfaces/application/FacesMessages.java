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
package org.richfaces.application;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIInput;
import javax.faces.component.UISelectMany;
import javax.faces.component.UISelectOne;
import javax.faces.convert.BigDecimalConverter;
import javax.faces.convert.BigIntegerConverter;
import javax.faces.convert.BooleanConverter;
import javax.faces.convert.ByteConverter;
import javax.faces.convert.CharacterConverter;
import javax.faces.convert.DateTimeConverter;
import javax.faces.convert.DoubleConverter;
import javax.faces.convert.EnumConverter;
import javax.faces.convert.FloatConverter;
import javax.faces.convert.IntegerConverter;
import javax.faces.convert.LongConverter;
import javax.faces.convert.NumberConverter;
import javax.faces.convert.ShortConverter;
import javax.faces.validator.BeanValidator;
import javax.faces.validator.DoubleRangeValidator;
import javax.faces.validator.LengthValidator;
import javax.faces.validator.LongRangeValidator;
import javax.faces.validator.RegexValidator;

import org.richfaces.l10n.MessageBundle;

/**
 * @author Nick Belaevski
 *
 */
@MessageBundle(baseName = FacesMessage.FACES_MESSAGES)
public enum FacesMessages {

    UIINPUT_CONVERSION(UIInput.CONVERSION_MESSAGE_ID),
    UIINPUT_REQUIRED(UIInput.REQUIRED_MESSAGE_ID),
    UIINPUT_UPDATE(UIInput.UPDATE_MESSAGE_ID),
    UISELECTONE_INVALID(UISelectOne.INVALID_MESSAGE_ID),
    UISELECTMANY_INVALID(UISelectMany.INVALID_MESSAGE_ID),
    BIG_DECIMAL_CONVERTER_DECIMAL(BigDecimalConverter.DECIMAL_ID),
    BIG_DECIMAL_CONVERTER_DECIMAL_DETAIL(BigDecimalConverter.DECIMAL_ID, true),
    BIG_INTEGER_CONVERTER_BIGINTEGER(BigIntegerConverter.BIGINTEGER_ID),
    BIG_INTEGER_CONVERTER_BIGINTEGER_DETAIL(BigIntegerConverter.BIGINTEGER_ID, true),
    BOOLEAN_CONVERTER_BOOLEAN(BooleanConverter.BOOLEAN_ID),
    BOOLEAN_CONVERTER_BOOLEAN_DETAIL(BooleanConverter.BOOLEAN_ID, true),
    BYTE_CONVERTER_BYTE(ByteConverter.BYTE_ID),
    BYTE_CONVERTER_BYTE_DETAIL(ByteConverter.BYTE_ID, true),
    CHARACTER_CONVERTER_CHARACTER(CharacterConverter.CHARACTER_ID),
    CHARACTER_CONVERTER_CHARACTER_DETAIL(CharacterConverter.CHARACTER_ID, true),
    DATE_TIME_CONVERTER_DATE(DateTimeConverter.DATE_ID),
    DATE_TIME_CONVERTER_DATE_DETAIL(DateTimeConverter.DATE_ID, true),
    DATE_TIME_CONVERTER_TIME(DateTimeConverter.TIME_ID),
    DATE_TIME_CONVERTER_TIME_DETAIL(DateTimeConverter.TIME_ID, true),
    DATE_TIME_CONVERTER_DATETIME(DateTimeConverter.DATETIME_ID),
    DATE_TIME_CONVERTER_DATETIME_DETAIL(DateTimeConverter.DATETIME_ID, true),
    DATE_TIME_CONVERTER_PATTERN_TYPE("javax.faces.converter.DateTimeConverter.PATTERN_TYPE"),
    DOUBLE_CONVERTER_DOUBLE(DoubleConverter.DOUBLE_ID),
    DOUBLE_CONVERTER_DOUBLE_DETAIL(DoubleConverter.DOUBLE_ID, true),
    ENUM_CONVERTER_ENUM(EnumConverter.ENUM_ID),
    ENUM_CONVERTER_ENUM_DETAIL(EnumConverter.ENUM_ID, true),
    ENUM_CONVERTER_ENUM_NO_CLASS(EnumConverter.ENUM_NO_CLASS_ID),
    ENUM_CONVERTER_ENUM_NO_CLASS_DETAIL(EnumConverter.ENUM_NO_CLASS_ID, true),
    FLOAT_CONVERTER_FLOAT(FloatConverter.FLOAT_ID),
    FLOAT_CONVERTER_FLOAT_DETAIL(FloatConverter.FLOAT_ID, true),
    INTEGER_CONVERTER_INTEGER(IntegerConverter.INTEGER_ID),
    INTEGER_CONVERTER_INTEGER_DETAIL(IntegerConverter.INTEGER_ID, true),
    LONG_CONVERTER_LONG(LongConverter.LONG_ID),
    LONG_CONVERTER_LONG_DETAIL(LongConverter.LONG_ID, true),
    NUMBER_CONVERTER_CURRENCY(NumberConverter.CURRENCY_ID),
    NUMBER_CONVERTER_CURRENCY_DETAIL(NumberConverter.CURRENCY_ID, true),
    NUMBER_CONVERTER_PERCENT(NumberConverter.PERCENT_ID),
    NUMBER_CONVERTER_PERCENT_DETAIL(NumberConverter.PERCENT_ID, true),
    NUMBER_CONVERTER_NUMBER(NumberConverter.NUMBER_ID),
    NUMBER_CONVERTER_NUMBER_DETAIL(NumberConverter.NUMBER_ID, true),
    NUMBER_CONVERTER_PATTERN(NumberConverter.PATTERN_ID),
    NUMBER_CONVERTER_PATTERN_DETAIL(NumberConverter.PATTERN_ID, true),
    SHORT_CONVERTER_SHORT(ShortConverter.SHORT_ID),
    SHORT_CONVERTER_SHORT_DETAIL(ShortConverter.SHORT_ID, true),
    CONVERTER_STRING("javax.faces.converter.STRING"),
    DOUBLE_RANGE_VALIDATOR_MAXIMUM(DoubleRangeValidator.MAXIMUM_MESSAGE_ID),
    DOUBLE_RANGE_VALIDATOR_MINIMUM(DoubleRangeValidator.MINIMUM_MESSAGE_ID),
    DOUBLE_RANGE_VALIDATOR_NOT_IN_RANGE(DoubleRangeValidator.NOT_IN_RANGE_MESSAGE_ID),
    DOUBLE_RANGE_VALIDATOR_TYPE(DoubleRangeValidator.TYPE_MESSAGE_ID),
    LENGTH_VALIDATOR_MAXIMUM(LengthValidator.MAXIMUM_MESSAGE_ID),
    LENGTH_VALIDATOR_MINIMUM(LengthValidator.MINIMUM_MESSAGE_ID),
    LONG_RANGE_VALIDATOR_MAXIMUM(LongRangeValidator.MAXIMUM_MESSAGE_ID),
    LONG_RANGE_VALIDATOR_MINIMUM(LongRangeValidator.MINIMUM_MESSAGE_ID),
    LONG_RANGE_VALIDATOR_NOT_IN_RANGE(LongRangeValidator.NOT_IN_RANGE_MESSAGE_ID),
    LONG_RANGE_VALIDATOR_TYPE(LongRangeValidator.TYPE_MESSAGE_ID),
    VALIDATOR_NOT_IN_RANGE("javax.faces.validator.NOT_IN_RANGE"),
    REGEX_VALIDATOR_PATTERN_NOT_SET(RegexValidator.PATTERN_NOT_SET_MESSAGE_ID),
    REGEX_VALIDATOR_PATTERN_NOT_SET_DETAIL(RegexValidator.PATTERN_NOT_SET_MESSAGE_ID, true),
    REGEX_VALIDATOR_NOT_MATCHED(RegexValidator.NOT_MATCHED_MESSAGE_ID),
    REGEX_VALIDATOR_NOT_MATCHED_DETAIL(RegexValidator.NOT_MATCHED_MESSAGE_ID, true),
    REGEX_VALIDATOR_MATCH_EXCEPTION(RegexValidator.MATCH_EXCEPTION_MESSAGE_ID),
    REGEX_VALIDATOR_MATCH_EXCEPTION_DETAIL(RegexValidator.MATCH_EXCEPTION_MESSAGE_ID, true),
    BEAN_VALIDATOR_MESSAGE(BeanValidator.MESSAGE_ID);
    private static final String DETAIL = "_detail";
    private String key;

    private FacesMessages(String key) {
        this(key, false);
    }

    private FacesMessages(String key, boolean isDetail) {
        this.key = isDetail ? key + DETAIL : key;
    }

    @Override
    public String toString() {
        return key;
    }
}
