package org.richfaces.component;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;
import org.richfaces.renderkit.html.ClientOnlyScript;
import org.richfaces.renderkit.html.ComponentValidatorScript;
import org.richfaces.validator.LibraryResource;
import org.richfaces.validator.LibraryScriptString;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

/**
 * Test collection of validator scripts.
 * @author asmirnov
 *
 */
public class UIValidatorScriptCollectionTest {

    static final LibraryResource FOO_RESOURCE = new LibraryResource("org.rf", "foo");


    @Test
    public void testAddOrFindScript() {
        UIValidatorScript validatorScriptComponent = new UIValidatorScript();
        ComponentValidatorScript validatorScript = createValidatorScript("foo", "bar");
        ComponentValidatorScript validatorScript2 = validatorScriptComponent.addOrFindScript(validatorScript);
        Collection<ComponentValidatorScript> scripts = validatorScriptComponent.getScripts();
        assertEquals(1, scripts.size());
        assertSame(validatorScript,Iterables.getOnlyElement(scripts));
    }

    @Test
    public void testAddOrFindScript2() {
        UIValidatorScript validatorScriptComponent = new UIValidatorScript();
        ComponentValidatorScript validatorScript = createValidatorScript("foo", "bar");
        validatorScriptComponent.addOrFindScript(validatorScript);
        ComponentValidatorScript validatorScript2 = createValidatorScript("fooz", "baz","bar");
        ComponentValidatorScript validatorScript3 = validatorScriptComponent.addOrFindScript(validatorScript2);
        Collection<ComponentValidatorScript> scripts = validatorScriptComponent.getScripts();
        assertEquals(2, scripts.size());
        assertSame(validatorScript2,validatorScript3);
    }

    @Test
    public void testAddOrFindScript3() {
        UIValidatorScript validatorScriptComponent = new UIValidatorScript();
        ComponentValidatorScript validatorScript = createValidatorScript("foo", "bar");
        validatorScriptComponent.addOrFindScript(validatorScript);
        ComponentValidatorScript validatorScript2 = createValidatorScript("foo", "bar");
        ComponentValidatorScript validatorScript3 = validatorScriptComponent.addOrFindScript(validatorScript2);
        Collection<ComponentValidatorScript> scripts = validatorScriptComponent.getScripts();
        assertEquals(1, scripts.size());
        assertSame(validatorScript,validatorScript3);
    }

    private LibraryScriptString createLibraryScript(final String name) {
        return new Script(name);
    }
    
    private ComponentValidatorScript createValidatorScript(String converter, String ...validators){
        ArrayList<LibraryScriptString> validatorScripts = Lists.newArrayList();
        for (String validatorName : validators) {
            validatorScripts.add(createLibraryScript(validatorName));
        }
        return new ClientOnlyScript(createLibraryScript(converter), validatorScripts);
    }
}
