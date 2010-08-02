package org.ajax4jsf.resource;

import java.util.Arrays;

import org.ajax4jsf.tests.AbstractAjax4JsfTestCase;

public class ParametersEncodingTestCase extends AbstractAjax4JsfTestCase {
    private byte[] data = {
        1, 2, 3, 4, 5, 6, 7, 8, 9, 0
    };
    private ResourceBuilderImpl builder;

    public ParametersEncodingTestCase(String arg0) {
        super(arg0);
    }

    public void setUp() throws Exception {
        super.setUp();
        builder = new ResourceBuilderImpl();
    }

    public void tearDown() throws Exception {
        builder = null;
        super.tearDown();
    }

    public final void testEncrypt() {
        byte[] bs = builder.encrypt(data);
        byte[] bs2 = builder.decrypt(bs);

        assertTrue(Arrays.equals(data, bs2));
    }

    public final void testDecryptLeak() {
        byte[] bs = {};

        for (int i = 0; i < 10000; i++) {
            bs = builder.encrypt(data);
        }

        byte[] bs2 = {};

        for (int i = 0; i < 10000; i++) {
            bs2 = builder.decrypt(bs);
        }

        assertTrue(Arrays.equals(data, bs2));
    }
}
