package org.richfaces.javascript.client;

import static org.easymock.EasyMock.expect;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;

import org.jboss.test.faces.mock.MockFacesEnvironment;
import org.jboss.test.qunit.Qunit;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.richfaces.javascript.Message;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

@RunWith(Parameterized.class)
public abstract class MockTestBase {
    protected static final String TEST_COMPONENT_ID = "testComponent";
    @Rule
    public final Qunit qunit;
    protected final RunParameters criteria;
    protected MockFacesEnvironment facesEnvironment;
    protected UIInput input;

    public MockTestBase(RunParameters criteria) {
        this.criteria = criteria;
        this.qunit = createQunitPage().build();
    }

    @Before
    public void setUp() {
        this.facesEnvironment = MockFacesEnvironment.createEnvironment().withApplication().resetToNice();
        input = facesEnvironment.createMock(UIInput.class);
        recordMocks();
        facesEnvironment.replay();
    }

    protected void recordMocks() {
        UIViewRoot viewRoot = facesEnvironment.createMock(UIViewRoot.class);
        expect(viewRoot.getLocale()).andStubReturn(Locale.ENGLISH);
        expect(facesEnvironment.getFacesContext().getViewRoot()).andStubReturn(viewRoot);
        expect(input.getAttributes()).andStubReturn(Collections.EMPTY_MAP);
        expect(input.getClientId(facesEnvironment.getFacesContext())).andStubReturn(TEST_COMPONENT_ID);
    }

    @After
    public void tearDown() {
        this.facesEnvironment.verify();
        this.facesEnvironment.release();
        this.facesEnvironment = null;
    }

    protected Message getErrorMessage() {
        return new Message(2, "error", "script error");
    }

    protected Object getJavaScriptOptions() {
        return criteria.getOptions();
    }

    protected org.jboss.test.qunit.Qunit.Builder createQunitPage() {
        return Qunit.builder().loadJsfResource("jquery.js").loadJsfResource("richfaces.js")
            .loadJsfResource("richfaces-event.js").loadJsfResource("richfaces-csv.rf4.js", "org.richfaces");
    }

    protected abstract String getJavaScriptFunctionName();

    protected Map<String, Object> getOptions() {
        Map<String, Object> options = criteria.getOptions();
        return options;
    }

    protected static List<RunParameters[]> options(RunParameters... criterias) {
        Builder<RunParameters[]> builder = ImmutableList.builder();
        for (RunParameters testCriteria : criterias) {
            builder.add(optionsArray(testCriteria));
        }
        return builder.build();
    }

    protected static RunParameters pass(Object value) {
        return new RunParameters(value);
    }

    protected static RunParameters pass(Object value, String option1, Object value1) {
        RunParameters testCriteria = pass(value);
        Map<String, Object> options = testCriteria.getOptions();
        options.put(option1, value1);
        return testCriteria;
    }

    protected static RunParameters pass(Object value, String option1, Object value1, String option2, Object value2) {
        RunParameters testCriteria = pass(value, option1, value1);
        Map<String, Object> options = testCriteria.getOptions();
        options.put(option2, value2);
        return testCriteria;
    }

    protected static RunParameters pass(Object value, String option1, Object value1, String option2, Object value2,
        String option3, Object value3) {
        RunParameters testCriteria = pass(value, option1, value1, option2, value2);
        Map<String, Object> options = testCriteria.getOptions();
        options.put(option3, value3);
        return testCriteria;
    }

    private static RunParameters[] optionsArray(RunParameters testCriteria) {
        return new RunParameters[] { testCriteria };
    }
}