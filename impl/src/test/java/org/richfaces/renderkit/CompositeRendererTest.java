package org.richfaces.renderkit;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Map;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.jboss.test.faces.AbstractFacesTest;

public class CompositeRendererTest extends AbstractFacesTest {
    private CompositeRenderer compositeRenderer;

    public void setUp() throws Exception {
        super.setUp();
        setupFacesRequest();
        this.compositeRenderer = new CompositeRenderer() {
            protected Class<? extends UIComponent> getComponentClass() {
                return UIComponent.class;
            }
        };
    }

    public void tearDown() throws Exception {
        super.tearDown();
        this.compositeRenderer = null;
    }

    public final void testDoDecodeFacesContextUIComponent() {
        MockDecodeContributor[] contributors = new MockDecodeContributor[5];

        for (int i = 0; i < contributors.length; i++) {
            contributors[i] = new MockDecodeContributor();
            compositeRenderer.addContributor(contributors[i]);
        }

        UIInput component = new UIInput();

        compositeRenderer.doDecode(facesContext, component);

        for (int i = 0; i < contributors.length; i++) {
            assertSame(compositeRenderer, contributors[i].getRenderer());
            assertSame(facesContext, contributors[i].getContext());
            assertSame(component, contributors[i].getComponent());
        }
    }

    public final void testMergeScriptOptionsScriptOptionsFacesContextUIComponent() {
        UIInput input = new UIInput();
        ScriptOptions inputOptions = new ScriptOptions(input);

        inputOptions.addOption("input", "1");

        ScriptOptions formOptions = new ScriptOptions(input);

        formOptions.addOption("form", "2");

        ScriptOptions options = new ScriptOptions(input);

        options.addOption("generic", "3");

        MockDecodeContributor inputContributor;
        MockDecodeContributor formContributor;
        MockDecodeContributor contributor;
        MockDecodeContributor nullContributor;

        inputContributor = new MockDecodeContributor(UIComponent.class, inputOptions);
        formContributor = new MockDecodeContributor(UIComponent.class, formOptions);
        contributor = new MockDecodeContributor(UIComponent.class, options);
        nullContributor = new MockDecodeContributor(UIComponent.class, (ScriptOptions) null);
        compositeRenderer.addContributor(contributor);
        compositeRenderer.addContributor(formContributor);
        compositeRenderer.addContributor(inputContributor);
        compositeRenderer.addContributor(nullContributor);

        ScriptOptions scriptOptions = new ScriptOptions(input);

        compositeRenderer.mergeScriptOptions(scriptOptions, facesContext, input);

        Map map = scriptOptions.getMap();

        assertEquals(3, map.size());
        assertEquals("1", map.get("input"));
        assertEquals("2", map.get("form"));
        assertEquals("3", map.get("generic"));
    }

    public final void testMergeScriptOptionsScriptOptionsFacesContextUIComponentClass() {
        UIInput input = new UIInput();
        ScriptOptions inputOptions = new ScriptOptions(input);

        inputOptions.addOption("input", "1");

        ScriptOptions formOptions = new ScriptOptions(input);

        formOptions.addOption("form", "2");

        ScriptOptions options = new ScriptOptions(input);

        options.addOption("generic", "3");

        MockDecodeContributor inputContributor;
        MockDecodeContributor formContributor;
        MockDecodeContributor contributor;
        MockDecodeContributor nullContributor;

        inputContributor = new MockDecodeContributor(UIInput.class, inputOptions);
        formContributor = new MockDecodeContributor(NamingContainer.class, formOptions);
        contributor = new MockDecodeContributor(UIComponent.class, options);
        nullContributor = new MockDecodeContributor(UIForm.class, (ScriptOptions) null);
        compositeRenderer.addContributor(contributor);
        compositeRenderer.addContributor(formContributor);
        compositeRenderer.addContributor(inputContributor);
        compositeRenderer.addContributor(nullContributor);

        ScriptOptions scriptOptions = new ScriptOptions(input);

        compositeRenderer.mergeScriptOptions(scriptOptions, facesContext, input, UIForm.class);

        Map map = scriptOptions.getMap();

        assertEquals(2, map.size());
        assertEquals("2", map.get("form"));
        assertEquals("3", map.get("generic"));
    }

    public final void testGetScriptContributionsStringFacesContextUIComponent() {
        MockDecodeContributor inputContributor;
        MockDecodeContributor formContributor;
        MockDecodeContributor contributor;
        MockDecodeContributor nullContributor;

        inputContributor = new MockDecodeContributor(UIComponent.class, ".1;");
        formContributor = new MockDecodeContributor(UIComponent.class, ".2;");
        contributor = new MockDecodeContributor(UIComponent.class, ".3;");
        nullContributor = new MockDecodeContributor(UIForm.class, (String) null);
        compositeRenderer.addContributor(contributor);
        compositeRenderer.addContributor(formContributor);
        compositeRenderer.addContributor(inputContributor);
        compositeRenderer.addContributor(nullContributor);

        UIInput input = new UIInput();

        input.getAttributes().put("test", ".testValue");

        String contributions = compositeRenderer.getScriptContributions("theVar", facesContext, input);

        assertEquals("theVar.testValue.3;theVar.testValue.2;theVar.testValue.1;", contributions);
    }

    public final void testGetScriptContributionsStringFacesContextUIComponentClass() {
        MockDecodeContributor inputContributor;
        MockDecodeContributor formContributor;
        MockDecodeContributor contributor;
        MockDecodeContributor nullContributor;

        inputContributor = new MockDecodeContributor(UIInput.class, ".input;");
        formContributor = new MockDecodeContributor(NamingContainer.class, ".namingContainer;");
        contributor = new MockDecodeContributor(UIComponent.class, ".generic;");
        nullContributor = new MockDecodeContributor(UIForm.class, (String) null);
        compositeRenderer.addContributor(contributor);
        compositeRenderer.addContributor(formContributor);
        compositeRenderer.addContributor(inputContributor);
        compositeRenderer.addContributor(nullContributor);

        UIInput input = new UIInput();

        input.getAttributes().put("test", ".testValue");

        String contributions = compositeRenderer.getScriptContributions("theVar", facesContext, input, UIForm.class);

        assertEquals("theVar.testValue.generic;theVar.testValue.namingContainer;", contributions);
    }

    public final void testAddContributor() {
        MockDecodeContributor[] contributors = new MockDecodeContributor[5];

        for (int i = 0; i < contributors.length; i++) {
            contributors[i] = new MockDecodeContributor();
            compositeRenderer.addContributor(contributors[i]);
        }

        assertTrue(Arrays.deepEquals(contributors, compositeRenderer.getContributors()));
    }

    public final void testContributorDecodeCallback() {
        MockDecodeContributor inputContributor;
        MockDecodeContributor formContributor;

        formContributor = new MockDecodeContributor(NamingContainer.class);
        inputContributor = new MockDecodeContributor(UIInput.class);
        compositeRenderer.addContributor(inputContributor);
        compositeRenderer.addContributor(formContributor);

        UIComponent component = new UIInput();

        compositeRenderer.doDecode(facesContext, component);
        assertSame(compositeRenderer, inputContributor.getRenderer());
        assertSame(facesContext, inputContributor.getContext());
        assertSame(component, inputContributor.getComponent());
        assertNull(formContributor.getRenderer());
        assertNull(formContributor.getContext());
        assertNull(formContributor.getComponent());
        inputContributor.reset();
        formContributor.reset();
        assertNull(inputContributor.getRenderer());
        assertNull(inputContributor.getContext());
        assertNull(inputContributor.getComponent());
        assertNull(formContributor.getRenderer());
        assertNull(formContributor.getContext());
        assertNull(formContributor.getComponent());
        component = new UIForm();
        compositeRenderer.doDecode(facesContext, component);
        assertSame(compositeRenderer, formContributor.getRenderer());
        assertSame(facesContext, formContributor.getContext());
        assertSame(component, formContributor.getComponent());
        assertNull(inputContributor.getRenderer());
        assertNull(inputContributor.getContext());
        assertNull(inputContributor.getComponent());
    }

    public final void testAddParameterEncoder() {
        MockAttributeParameterEncoder[] encoders = new MockAttributeParameterEncoder[5];

        for (int i = 0; i < encoders.length; i++) {
            encoders[i] = new MockAttributeParameterEncoder("aaa");
            compositeRenderer.addParameterEncoder(encoders[i]);
        }

        assertTrue(Arrays.deepEquals(encoders, compositeRenderer.getParameterEncoders()));
    }

    public final void testEncodeAttributeParameters() throws IOException {
        MockAttributeParameterEncoder encoder1 = new MockAttributeParameterEncoder("Attribute");
        MockAttributeParameterEncoder encoder2 = new MockAttributeParameterEncoder("MoreAttribute");

        compositeRenderer.addParameterEncoder(encoder1);
        compositeRenderer.addParameterEncoder(encoder2);

        UIInput input = new UIInput();

        input.getAttributes().put("Attribute", "testValue1");
        input.getAttributes().put("MoreAttribute", "testValue2");

        // ResponseWriter responseWriter = facesContext.getResponseWriter();
        StringWriter stringWriter = new StringWriter();
        ResponseWriter responseWriter = facesContext.getRenderKit().createResponseWriter(stringWriter, "text/html",
                                            "UTF8");

        facesContext.setResponseWriter(responseWriter);
        responseWriter.startDocument();
        responseWriter.startElement("span", input);
        compositeRenderer.encodeAttributeParameters(facesContext, input);
        responseWriter.endElement("span");
        responseWriter.endDocument();
        responseWriter.flush();

        String result = stringWriter.getBuffer().toString();

        assertTrue(result.contains("testAttribute=\"testValue1\""));
        assertTrue(result.contains("testMoreAttribute=\"testValue2\""));
    }
}


class MockAttributeParameterEncoder implements AttributeParametersEncoder {
    private String attributeName;

    public MockAttributeParameterEncoder(String attributeName) {
        super();
        this.attributeName = attributeName;
    }

    public void doEncode(FacesContext context, UIComponent component) throws IOException {
        context.getResponseWriter().writeAttribute("test" + attributeName,
                component.getAttributes().get(attributeName), null);
    }
}


class MockDecodeContributor implements RendererContributor {
    private UIComponent component;
    private Class componentClass;
    private FacesContext context;
    private ScriptOptions options;
    private CompositeRenderer renderer;
    private String scriptContribution;
    private String[] scriptDependencies;
    private String[] styleDependencies;

    public MockDecodeContributor() {
        this(UIComponent.class);
    }

    public MockDecodeContributor(Class componentClass) {
        super();
        this.componentClass = componentClass;
    }

    public MockDecodeContributor(Class componentClass, ScriptOptions options) {
        super();
        this.componentClass = componentClass;
        this.options = options;
    }

    public MockDecodeContributor(Class componentClass, String scriptContribution) {
        super();
        this.componentClass = componentClass;
        this.scriptContribution = scriptContribution;
    }

    public MockDecodeContributor(Class componentClass, String[] scriptDependencies, String[] styleDependencies) {
        super();
        this.componentClass = componentClass;
        this.scriptDependencies = scriptDependencies;
        this.styleDependencies = styleDependencies;
    }

    public void decode(FacesContext context, UIComponent component, CompositeRenderer compositeRenderer) {
        this.component = component;
        this.context = context;
        this.renderer = compositeRenderer;
    }

    public Class getAcceptableClass() {
        return componentClass;
    }

    public String getScriptContribution(FacesContext context, UIComponent component) {
        return scriptContribution != null ? component.getAttributes().get("test") + scriptContribution : null;
    }

    public String[] getScriptDependencies() {
        return scriptDependencies;
    }

    public String[] getStyleDependencies() {
        return styleDependencies;
    }

    public ScriptOptions buildOptions(FacesContext context, UIComponent component) {
        return options;
    }

    public UIComponent getComponent() {
        return component;
    }

    public FacesContext getContext() {
        return context;
    }

    public CompositeRenderer getRenderer() {
        return renderer;
    }

    public void reset() {
        this.component = null;
        this.context = null;
        this.renderer = null;
    }
}
