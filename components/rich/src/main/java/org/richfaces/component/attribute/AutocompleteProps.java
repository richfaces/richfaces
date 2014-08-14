package org.richfaces.component.attribute;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.Signature;
import org.richfaces.component.AutocompleteMode;

import javax.el.MethodExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
public interface AutocompleteProps {
    /**
     *  <p>Determine how the suggestion list is requested:</p>
     *  <dl>
     *      <dt>client</dt>
     *      <dd>pre-loads data to the client and uses the input to filter the possible suggestions</dd>
     *      <dt>ajax</dt>
     *      <dd>fetches suggestions with every input change using Ajax requests</dd>
     *      <dt>lazyClient</dt>
     *      <dd>
     * pre-loads data to the client and uses the input to filter the possible suggestions. The filtering does not start
     * until the input length matches a minimum value. Set the minimum value with the minChars attribute.
     *      </dd>
     *      <dt>cachedAjax</dt>
     *      <dd>
     * pre-loads data via Ajax requests when the input length matches a minimum value. Set the minimum value with the
     * minChars attribute. All suggestions are handled on the client until the input prefix is changed, at which point
     * a new request is made based on the new input prefix
     *      </dd>
     *  </dl>
     *  <p>Default: cachedAjax</p>
     */
    @Attribute
    AutocompleteMode getMode();

    /**
     * Minimal number of chars in input to activate suggestion popup
     */
    @Attribute
    int getMinChars();

    /**
     * A request-scope attribute via which the data object for the current row will be used when iterating
     */
    @Attribute(literal = true)
    String getVar();

    /**
     * A collection of suggestions that will be resented to the user
     */
    @Attribute()
    Object getAutocompleteList();

    /**
     * A method which returns a list of suggestions according to a supplied prefix
     */
    @Attribute(signature = @Signature(returnType = Object.class, parameters = { FacesContext.class, UIComponent.class,
            String.class }))
    MethodExpression getAutocompleteMethod();
    void setAutocompleteMethod(MethodExpression expression);

    /**
     * Workaround for RF-11469
     */
    @Attribute(hidden = true, signature = @Signature(returnType = Object.class, parameters = { String.class }))
    MethodExpression getAutocompleteMethodWithOneParameter();
    void setAutocompleteMethodWithOneParameter(MethodExpression expression);
}
