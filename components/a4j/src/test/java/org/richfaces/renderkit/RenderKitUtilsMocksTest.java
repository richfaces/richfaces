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
package org.richfaces.renderkit;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.same;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.component.behavior.ClientBehaviorHint;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.easymock.EasyMock;
import org.jboss.test.faces.mock.MockFacesEnvironment;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Maps;

/**
 * @author Nick Belaevski
 *
 */
public class RenderKitUtilsMocksTest {
    /**
     *
     */
    private static final String CLIENT_ID = "submittedId";
    private static final String FACET_NAME = "facetName";
    private MockFacesEnvironment facesEnvironment;
    private ResponseWriter responseWriter;
    private FacesContext facesContext;
    private ExternalContext externalContext;
    private Map<String, Object> componentAttributes;
    private Map<String, List<ClientBehavior>> behaviorsMap;
    private Map<String, ComponentAttribute> knownAttributes;

    @Before
    public void setUp() throws Exception {
        facesEnvironment = MockFacesEnvironment.createEnvironment().withExternalContext();

        facesContext = facesEnvironment.getFacesContext();
        externalContext = facesEnvironment.getExternalContext();

        responseWriter = facesEnvironment.createMock(ResponseWriter.class);
        expect(facesContext.getResponseWriter()).andStubReturn(responseWriter);
        expect(responseWriter.getContentType()).andStubReturn("application/xhtml+xml");

        componentAttributes = new HashMap<String, Object>();
        behaviorsMap = new HashMap<String, List<ClientBehavior>>();
        knownAttributes = new TreeMap<String, ComponentAttribute>();
    }

    @After
    public void tearDown() throws Exception {
        this.facesEnvironment.verify();
        this.facesEnvironment.release();

        this.facesEnvironment = null;
        this.responseWriter = null;
        this.facesContext = null;
        this.externalContext = null;
        this.componentAttributes = null;
        this.behaviorsMap = null;
        this.knownAttributes = null;
    }

    private UIComponent createMockComponent() {
        UIComponent component = facesEnvironment.createMock(UIComponent.class);
        expect(component.getAttributes()).andStubReturn(componentAttributes);
        expect(component.getClientId(same(facesContext))).andStubReturn(CLIENT_ID);
        return component;
    }

    private ClientBehaviorHolder createMockClientBehaviorHolder() {
        UIComponent component = facesEnvironment.createMock(AbstractClientBehaviorHolderComponent.class);
        expect(component.getClientId(same(facesContext))).andStubReturn(CLIENT_ID);
        expect(component.getAttributes()).andStubReturn(componentAttributes);
        ClientBehaviorHolder behaviorHolder = (ClientBehaviorHolder) component;
        expect(behaviorHolder.getClientBehaviors()).andStubReturn(behaviorsMap);
        expect(behaviorHolder.getEventNames()).andStubReturn(
                Arrays.asList("click", "action", "mousemove", "keypress", "blur", "contextmenu"));
        return behaviorHolder;
    }

    @Test
    public void testRenderPassThroughAttributes() throws Exception {
        knownAttributes.put("disabled", new ComponentAttribute("disabled"));
        knownAttributes.put("checked", new ComponentAttribute("checked"));
        knownAttributes.put("style", new ComponentAttribute("style"));
        knownAttributes.put("src", new ComponentAttribute("src"));
        knownAttributes.put("lang", new ComponentAttribute("lang"));
        knownAttributes.put("class", new ComponentAttribute("class").setComponentAttributeName("styleClass"));

        componentAttributes.put("disabled", Boolean.TRUE);
        componentAttributes.put("checked", Boolean.FALSE);
        componentAttributes.put("style", "color:red");
        componentAttributes.put("src", "urn:abc");
        componentAttributes.put("facelets.Mark", 123);
        componentAttributes.put("lang", "ru");
        componentAttributes.put("styleClass", "rich-component");

        UIComponent component = createMockComponent();

        responseWriter.writeAttribute(eq("disabled"), eq(Boolean.TRUE), EasyMock.<String>isNull());
        // checked attribute shouldn't be rendered - it's 'false'
        responseWriter.writeAttribute(eq("style"), eq("color:red"), EasyMock.<String>isNull());
        responseWriter.writeURIAttribute(eq("src"), eq("urn:abc"), EasyMock.<String>isNull());
        // facelets.Mark shouldn't be rendered - it's unknown
        responseWriter.writeAttribute(eq("xml:lang"), eq("ru"), EasyMock.<String>isNull());
        responseWriter.writeAttribute(eq("class"), eq("rich-component"), EasyMock.<String>isNull());

        facesEnvironment.replay();

        RenderKitUtils.renderPassThroughAttributes(facesContext, component, knownAttributes);
    }

    private ClientBehavior createClientBehavior(String handlerData, Set<ClientBehaviorHint> hints) {
        ClientBehavior behavior = facesEnvironment.createMock(ClientBehavior.class);
        expect(behavior.getScript(EasyMock.<ClientBehaviorContext>notNull())).andStubReturn(
                MessageFormat.format("prompt({0})", handlerData));

        expect(behavior.getHints()).andStubReturn(hints);
        return behavior;
    }

    @Test
    public void testBehaviors() throws Exception {
        knownAttributes.put("onclick", new ComponentAttribute("onclick").setEventNames(new String[] { "click", "action" }));
        knownAttributes.put("onmousemove", new ComponentAttribute("onmousemove").setEventNames(new String[] { "mousemove" }));
        knownAttributes.put("onkeypress", new ComponentAttribute("onkeypress").setEventNames(new String[] { "keypress" }));
        knownAttributes.put("oncontextmenu",
                new ComponentAttribute("oncontextmenu").setEventNames(new String[] { "contextmenu" }));

        componentAttributes.put("onkeypress", "alert(keypress)");
        componentAttributes.put("onmousemove", "alert(mousemove)");
        componentAttributes.put("onclick", "alert(click)");

        Set<ClientBehaviorHint> emptyHintsSet = EnumSet.noneOf(ClientBehaviorHint.class);
        Set<ClientBehaviorHint> submittingHintsSet = EnumSet.of(ClientBehaviorHint.SUBMITTING);

        ClientBehavior keypressBehavior = createClientBehavior("keypress", emptyHintsSet);
        ClientBehavior actionBehavior1 = createClientBehavior("action1", emptyHintsSet);
        ClientBehavior actionBehavior2 = createClientBehavior("action2", submittingHintsSet);
        ClientBehavior actionBehavior3 = createClientBehavior("action3", emptyHintsSet);
        ClientBehavior contextmenuBehavior = createClientBehavior("contextmenu", emptyHintsSet);

        behaviorsMap.put("keypress", Arrays.asList(keypressBehavior));
        behaviorsMap.put("action", Arrays.asList(actionBehavior1, actionBehavior2, actionBehavior3));
        behaviorsMap.put("contextmenu", Arrays.asList(contextmenuBehavior));

        ClientBehaviorHolder behaviorHolder = createMockClientBehaviorHolder();
        UIComponent component = (UIComponent) behaviorHolder;

        responseWriter.writeAttribute(eq("onkeypress"),
                eq("return jsf.util.chain(this, event, 'alert(keypress)','prompt(keypress)')"), EasyMock.<String>isNull());
        responseWriter.writeAttribute(eq("onclick"),
                eq("return jsf.util.chain(this, event, 'alert(click)','prompt(action1)','prompt(action2)')"),
                EasyMock.<String>isNull());
        responseWriter.writeAttribute(eq("onmousemove"), eq("alert(mousemove)"), EasyMock.<String>isNull());
        responseWriter.writeAttribute(eq("oncontextmenu"), eq("prompt(contextmenu)"), EasyMock.<String>isNull());

        facesEnvironment.replay();

        RenderKitUtils.renderPassThroughAttributes(facesContext, component, knownAttributes);
    }

    private UIComponent setupBehaviorsTestForDisabledComponent() throws IOException {
        knownAttributes.put("style", new ComponentAttribute("style"));
        knownAttributes.put("onclick", new ComponentAttribute("onclick").setEventNames(new String[] { "click", "action" }));
        knownAttributes.put("onmousemove", new ComponentAttribute("onmousemove").setEventNames(new String[] { "mousemove" }));

        componentAttributes.put("onmousemove", "alert(mousemove)");
        componentAttributes.put("onclick", "alert(click)");
        componentAttributes.put("style", "color:green");

        Set<ClientBehaviorHint> emptyHintsSet = EnumSet.noneOf(ClientBehaviorHint.class);

        ClientBehavior actionBehavior1 = createClientBehavior("action1", emptyHintsSet);
        behaviorsMap.put("action", Arrays.asList(actionBehavior1));

        ClientBehaviorHolder behaviorHolder = createMockClientBehaviorHolder();
        UIComponent component = (UIComponent) behaviorHolder;
        return component;
    }

    @Test
    public void testBehaviorsForDisabledComponent() throws Exception {
        componentAttributes.put("disabled", Boolean.TRUE);
        UIComponent component = setupBehaviorsTestForDisabledComponent();

        responseWriter.writeAttribute(eq("style"), eq("color:green"), EasyMock.<String>isNull());

        facesEnvironment.replay();

        RenderKitUtils.renderPassThroughAttributes(facesContext, component, knownAttributes);
    }

    @Test
    public void testBehaviorsForNonDisabledComponent() throws Exception {
        componentAttributes.put("disabled", Boolean.FALSE);
        UIComponent component = setupBehaviorsTestForDisabledComponent();

        responseWriter.writeAttribute(eq("onclick"),
                eq("return jsf.util.chain(this, event, 'alert(click)','prompt(action1)')"), EasyMock.<String>isNull());
        responseWriter.writeAttribute(eq("onmousemove"), eq("alert(mousemove)"), EasyMock.<String>isNull());
        responseWriter.writeAttribute(eq("style"), eq("color:green"), EasyMock.<String>isNull());

        facesEnvironment.replay();

        RenderKitUtils.renderPassThroughAttributes(facesContext, component, knownAttributes);
    }

    public void checkDisabled(Object attributeValue, boolean expectedValue) throws Exception {
        componentAttributes.put("disabled", attributeValue);

        UIComponent component = createMockComponent();
        facesEnvironment.replay();
        assertTrue(expectedValue == RenderKitUtils.isDisabled(component));
    }

    @Test
    public void testIsDisabled() throws Exception {
        checkDisabled(Boolean.TRUE, true);
    }

    @Test
    public void testIsDisabledString() throws Exception {
        checkDisabled("true", true);
    }

    @Test
    public void testIsNonDisabled() throws Exception {
        checkDisabled(Boolean.FALSE, false);
    }

    @Test
    public void testIsNonDisabledString() throws Exception {
        checkDisabled("false", false);
    }

    @Test
    public void testIsNonDisabledNull() throws Exception {
        checkDisabled(null, false);
    }

    private UIComponent setupTestDecodeBehaviors(String behaviorSourceId, String behaviorEventName) throws Exception {
        ClientBehaviorHolder behaviorHolder = createMockClientBehaviorHolder();
        UIComponent component = (UIComponent) behaviorHolder;

        Map<String, String> requestParameterMap = new HashMap<String, String>();
        requestParameterMap.put(RenderKitUtils.BEHAVIOR_SOURCE_ID, behaviorSourceId);
        requestParameterMap.put(RenderKitUtils.BEHAVIOR_EVENT_NAME, behaviorEventName);
        expect(externalContext.getRequestParameterMap()).andStubReturn(requestParameterMap);

        ClientBehavior actionBehavior = createClientBehavior("action1", EnumSet.of(ClientBehaviorHint.SUBMITTING));
        ClientBehavior actionBehavior1 = createClientBehavior("action2", EnumSet.of(ClientBehaviorHint.SUBMITTING));
        behaviorsMap.put("action", Arrays.asList(actionBehavior, actionBehavior1));

        ClientBehavior blurBehavior = createClientBehavior("blur1", EnumSet.of(ClientBehaviorHint.SUBMITTING));
        behaviorsMap.put("blur", Arrays.asList(blurBehavior));

        return component;
    }

    @Test
    public void testDecodeActionBehaviors() throws Exception {
        UIComponent component = setupTestDecodeBehaviors(CLIENT_ID, "action");

        List<ClientBehavior> behaviors = behaviorsMap.get("action");
        for (ClientBehavior clientBehavior : behaviors) {
            clientBehavior.decode(same(facesContext), same(component));
            expectLastCall();
        }

        facesEnvironment.replay();

        assertEquals("action", RenderKitUtils.decodeBehaviors(facesContext, component));
    }

    @Test
    public void testDecodeBlurBehaviors() throws Exception {
        UIComponent component = setupTestDecodeBehaviors(CLIENT_ID, "blur");

        List<ClientBehavior> behaviors = behaviorsMap.get("blur");
        for (ClientBehavior clientBehavior : behaviors) {
            clientBehavior.decode(same(facesContext), same(component));
            expectLastCall();
        }

        facesEnvironment.replay();

        assertEquals("blur", RenderKitUtils.decodeBehaviors(facesContext, component));
    }

    @Test
    public void testDecodeNonMatchingClientId() throws Exception {
        UIComponent component = setupTestDecodeBehaviors("wrongId", "action");

        // nothing should be called - clientId is not matched

        facesEnvironment.replay();

        assertNull(RenderKitUtils.decodeBehaviors(facesContext, component));
    }

    @Test
    public void testDecodeNoSubmittedBehavior() throws Exception {
        UIComponent component = setupTestDecodeBehaviors(CLIENT_ID, null);

        // nothing should be called - no behavior event information was submitted

        facesEnvironment.replay();

        assertNull(RenderKitUtils.decodeBehaviors(facesContext, component));
    }

    @Test
    public void testDecodeContextMenuBehaviors() throws Exception {
        UIComponent component = setupTestDecodeBehaviors(CLIENT_ID, "contextmenu");

        // nothing should be called - no context menu behaviors were created

        facesEnvironment.replay();

        assertNull(RenderKitUtils.decodeBehaviors(facesContext, component));
    }

    @Test
    public void when_component_has_defined_facet_and_it_is_rendered_then_hasFacet_should_return_true() {
        // given

        UIComponent component = createMockComponent();
        UIComponent facetComponent = createMockComponent();

        expect(component.getFacet(FACET_NAME)).andReturn(facetComponent).times(2);
        expect(facetComponent.isRendered()).andReturn(true);

        facesEnvironment.replay();

        // when
        boolean hasFacet = RenderKitUtils.hasFacet(component, FACET_NAME);

        // then
        assertTrue("hasFacet should return true", hasFacet);
    }

    @Test
    public void when_component_has_defined_facet_but_it_is_not_rendered_then_hasFacet_should_return_false() {
        // given
        UIComponent component = createMockComponent();
        UIComponent facetComponent = createMockComponent();

        expect(component.getFacet(FACET_NAME)).andReturn(facetComponent).times(2);
        expect(facetComponent.isRendered()).andReturn(false);

        facesEnvironment.replay();

        // when
        boolean hasFacet = RenderKitUtils.hasFacet(component, FACET_NAME);

        // then
        assertFalse("hasFacet should return false", hasFacet);
    }

    @Test
    public void when_component_has_not_defined_facet_then_hasFacet_should_return_false() {
        // given
        UIComponent component = createMockComponent();

        expect(component.getFacet(FACET_NAME)).andReturn(null);

        facesEnvironment.replay();

        // when
        boolean hasFacet = RenderKitUtils.hasFacet(component, FACET_NAME);

        // then
        assertFalse("hasFacet should return false", hasFacet);
    }

    @Test
    public void when_attribute_value_expression_is_null_then_evaluateAttribute_should_return_attribute_value() {
        // given
        Map<String, Object> attributes = Maps.newTreeMap();
        Object value = new Object();
        attributes.put("value", value);

        UIComponent component = createMockComponent();

        expect(component.getAttributes()).andReturn(attributes);
        expect(component.getValueExpression("value")).andReturn(null);

        facesEnvironment.replay();

        // when
        Object evaluated = RenderKitUtils.evaluateAttribute("value", component, facesContext);

        // then
        assertEquals(value, evaluated);
    }

    @Test
    public void when_attribute_value_expression_evaluates_to_null_then_evaluateAttribute_should_return_attribute_value() {
        // given
        Map<String, Object> attributes = Maps.newTreeMap();
        Object value = new Object();
        attributes.put("value", value);
        ELContext elContext = facesEnvironment.getElContext();

        ValueExpression valueExpression = facesEnvironment.createMock(ValueExpression.class);


        UIComponent component = createMockComponent();

        expect(facesContext.getELContext()).andReturn(elContext);
        expect(component.getValueExpression("value")).andReturn(valueExpression);
        expect(valueExpression.getValue(elContext)).andReturn(null);
        expect(component.getAttributes()).andReturn(attributes);

        facesEnvironment.replay();

        // when
        Object evaluated = RenderKitUtils.evaluateAttribute("value", component, facesContext);

        // then
        assertEquals(value, evaluated);
    }

    @Test
    public void when_attribute_value_expression_evaluates_to_some_value_then_evaluateAttribute_the_value_is_returned() {
        // given
        Map<String, Object> attributes = Maps.newTreeMap();
        Object attributeValue = new Object();
        Object valueExpressionValue = new Object();
        attributes.put("value", attributeValue);
        ELContext elContext = facesEnvironment.getElContext();

        ValueExpression valueExpression = facesEnvironment.createMock(ValueExpression.class);

        UIComponent component = createMockComponent();

        expect(facesContext.getELContext()).andReturn(elContext);
        expect(component.getValueExpression("value")).andReturn(valueExpression);
        expect(valueExpression.getValue(elContext)).andReturn(valueExpressionValue);

        facesEnvironment.replay();

        // when
        Object evaluated = RenderKitUtils.evaluateAttribute("value", component, facesContext);

        // then
        assertEquals(valueExpressionValue, evaluated);
    }

    public abstract static class AbstractClientBehaviorHolderComponent extends UIComponent implements ClientBehaviorHolder {
    }
}