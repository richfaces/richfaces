
/**
 *
 */
package org.ajax4jsf.tests;

import javax.el.ExpressionFactory;

import org.apache.shale.test.el.MockExpressionFactory;
import org.apache.shale.test.mock.MockApplication12;

/**
 * @author Administrator
 *
 */
public class MockApplication extends MockApplication12 {
    private ExpressionFactory expressionFactory;

    public MockApplication() {
        expressionFactory = new EnumSupportExpressionFactoryWrapper(new MockExpressionFactory());
    }

    @Override
    public ExpressionFactory getExpressionFactory() {
        return expressionFactory;
    }
}
