package org.richfaces.convert;

import static org.richfaces.model.TreeDataModel.SEPARATOR_CHAR;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.richfaces.model.SequenceRowKey;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.ObjectArrays;

/**
 * @author Nick Belaevski
 * @since 3.3.1
 */
public class SequenceRowKeyConverter<T> implements Converter {
    static final Splitter SEPARATOR_SPLITTER = Splitter.on(SEPARATOR_CHAR);
    private Class<T> clazz;
    private Converter delegateConverter;

    public SequenceRowKeyConverter(Class<T> clazz, Converter delegateConverter) {
        super();
        this.clazz = clazz;
        this.delegateConverter = delegateConverter;
    }

    public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
        if (Strings.isNullOrEmpty(value)) {
            return null;
        }

        Iterable<String> split = SEPARATOR_SPLITTER.split(value);
        List<T> keysList = Lists.<T>newArrayList();

        for (String s : split) {
            T convertedKey = clazz.cast(delegateConverter.getAsObject(context, component, s));
            keysList.add(convertedKey);
        }

        return new SequenceRowKey(keysList.toArray(ObjectArrays.newArray(clazz, keysList.size())));
    }

    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }

        SequenceRowKey sequenceRowKey = (SequenceRowKey) value;

        StringBuilder result = new StringBuilder();

        for (Object simpleKey : sequenceRowKey.getSimpleKeys()) {
            String convertedKey = delegateConverter.getAsString(context, component, simpleKey);

            if (result.length() > 0) {
                result.append(SEPARATOR_CHAR);
            }

            result.append(convertedKey);
        }

        return result.toString();
    }
}