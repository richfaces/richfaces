package org.richfaces.javascript.client;

import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;

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

    @Rule
    public final Qunit qunit;

    protected final RunParameters criteria;
    protected MockFacesEnvironment facesEnvironment;
    protected UIComponent component;

    public MockTestBase(RunParameters criteria) {
        this.criteria = criteria;
        this.qunit = createQunitPage().build();
    }

    @Before
    public void setUp() {
        this.facesEnvironment = MockFacesEnvironment.createEnvironment().withApplication().resetToNice();        
        component = facesEnvironment.createMock(UIComponent.class);
        recordMocks();
        facesEnvironment.replay();
    }

    protected void recordMocks() {
        // template method to record mock objects 
        
    }

    @After
    public void tearDown() {
        this.facesEnvironment.verify();
        this.facesEnvironment.release();
        this.facesEnvironment = null;
    }

    protected Message getErrorMessage() {
        return new Message(2,"error","script error");
    }

    protected Object getJavaScriptOptions() {
        return criteria.getOptions();
    }

    protected org.jboss.test.qunit.Qunit.Builder createQunitPage() {
        return Qunit.builder().loadJsfResource("jquery.js").loadJsfResource("richfaces.js")
                    .loadJsfResource("richfaces-event.js").loadJsfResource("csv.js", "org.richfaces");
    }

    protected abstract String getJavaScriptFunctionName();
    
    protected static List<RunParameters[]> options(RunParameters ...criterias){
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
        RunParameters testCriteria = pass(value);
        Map<String, Object> options = testCriteria.getOptions();
        options.put(option1, value1);
        options.put(option2, value2);
        return testCriteria;
    }

    private static RunParameters[] optionsArray(RunParameters testCriteria) {
        return new RunParameters[] { testCriteria };
    }

}