package org.richfaces.renderkit.html;

import java.util.Collections;
import java.util.Map;

import javax.faces.application.FacesMessage;

import org.jboss.test.faces.mock.Mock;
import org.richfaces.component.behavior.BehaviorTestBase;
import org.richfaces.component.behavior.ClientValidatorBehavior;
import org.richfaces.javascript.Message;
import org.richfaces.resource.ResourceKey;

import com.google.common.collect.ImmutableMap;

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