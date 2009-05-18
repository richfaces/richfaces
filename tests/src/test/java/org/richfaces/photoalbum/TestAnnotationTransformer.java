package org.richfaces.photoalbum;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.testng.internal.annotations.IAnnotationTransformer;
import org.testng.internal.annotations.ITest;
import org.testng.internal.annotations.TestAnnotation;

/**
 * This transformer sets necessary data provider for each test method.
 *
 * @author carcasser
 */
public class TestAnnotationTransformer implements IAnnotationTransformer {

    /**
     * @see IAnnotationTransformer#transform(ITest, Class, Constructor, Method)
     */
    public void transform(ITest annotation, Class testClass, Constructor testConstructor, Method testMethod) {

        if ((testClass == null || RichSeleniumTest.class.isAssignableFrom(testClass))
                && (annotation instanceof TestAnnotation)) {
            ((TestAnnotation) annotation).setDataProvider("templates");
        }
    }

}