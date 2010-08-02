package org.ajax4jsf.config;

import java.util.HashMap;
import java.util.Map;

import javax.faces.context.ExternalContext;

import org.ajax4jsf.config.FrameworkConfiguration.InitParam;
import org.jboss.test.faces.AbstractFacesTest;

/**
 * @author Anton Belevich
 *
 */
public class FrameworkConfigurationTest extends AbstractFacesTest {
    private FrameworkConfiguration configuration;
    Map<String, String> localInitParams;
    private MockInitializationBean mockBean;
    Map<String, String> refenceInitParams;

    public void testBooleanOption() {
        boolean actual = configuration.isOptionEnabled(InitParam.COMPRESS_SCRIPT);
        boolean expected = Boolean.parseBoolean(localInitParams.get(InitParam.COMPRESS_SCRIPT.name()));

        assertEquals(expected, actual);
    }

    public void testBooleanOptionValueReference() {
        boolean actual = configuration.isOptionEnabled(InitParam.ENCRYPT_RESOURCE_DATA);
        boolean expected = mockBean.getEncryptResourceData();

        assertEquals(expected, actual);
    }

    public void testNumberOption() {
        int actual = configuration.getOptionNumber(InitParam.DEFAULT_EXPIRE);
        int expected = Integer.parseInt(localInitParams.get(InitParam.DEFAULT_EXPIRE.name()));

        assertEquals(expected, actual);
    }

    public void testStringOption() {
        String actual = configuration.getOption(InitParam.SKIN);
        String expected = (String) localInitParams.get(InitParam.SKIN.name());

        assertEquals(expected, actual);
    }

    public void testStringOptionValueReference() {
        String actual = configuration.getOptionValue(InitParam.LoadStyleStrategy);
        String expected = mockBean.getStyleStrategy();

        assertEquals(expected, actual);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mockBean = new MockInitializationBean();
        setupFacesRequest();

        ExternalContext externalContext = facesContext.getExternalContext();

        configuration = FrameworkConfiguration.getInstance(externalContext);
        facesContext.getExternalContext().getRequestMap().put("mockBean", mockBean);
    }

    @Override
    protected void setupJsfInitParameters() {
        super.setupJsfInitParameters();
        localInitParams = new HashMap<String, String>();
        localInitParams.put(InitParam.SKIN.name(), "blueSky");
        localInitParams.put(InitParam.COMPRESS_SCRIPT.name(), "false");
        localInitParams.put(InitParam.DEFAULT_EXPIRE.name(), "8500");
        facesServer.addInitParameter(FrameworkConfiguration.InitParam.SKIN.getQualifiedName(), "blueSky");
        facesServer.addInitParameter(FrameworkConfiguration.InitParam.LoadScriptStrategy.getQualifiedName(), "ALL");
        facesServer.addInitParameter(FrameworkConfiguration.InitParam.COMPRESS_SCRIPT.getQualifiedName(), "false");
        facesServer.addInitParameter(FrameworkConfiguration.InitParam.DEFAULT_EXPIRE.getQualifiedName(), "8500");
        facesServer.addInitParameter(FrameworkConfiguration.InitParam.ENCRYPT_RESOURCE_DATA.getQualifiedName(),
                                     "#{mockBean.encryptResourceData}");
        facesServer.addInitParameter(FrameworkConfiguration.InitParam.LoadStyleStrategy.getQualifiedName(),
                                     "#{mockBean.styleStrategy}");
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        mockBean = null;
    }

    public class MockInitializationBean {
        private String styleStrategy = "ALL";
        private boolean encryptResourceData = true;

        public boolean getEncryptResourceData() {
            return encryptResourceData;
        }

        public void setEncryptResourceData(boolean encryptResourceData) {
            this.encryptResourceData = encryptResourceData;
        }

        public String getStyleStrategy() {
            return styleStrategy;
        }

        public void setStyleStrategy(String styleStrategy) {
            this.styleStrategy = styleStrategy;
        }
    }
}
