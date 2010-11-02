/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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
package org.richfaces.convert;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.internal.runners.model.ReflectiveCallable;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.Suite;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

/**
 * @author Pavel Yaschenko
 * 
 */
public abstract class BaseTestRunner extends Suite {

    private final class ParameterizedFrameworkMethod extends FrameworkMethod {

        private TestData testData;

        private boolean failure;

        public ParameterizedFrameworkMethod(Method method, TestData testData, boolean failure) {
            super(method);
            this.testData = testData;
            this.failure = failure;
        }

        public String getFormattedParameters() {
            if (testData == null) {
                return "no test data";
            }

            StringBuilder sb = new StringBuilder();

            if (failure) {
                sb.append("<failure>");
            } else {
                sb.append("<success>");
            }

            sb.append(" @TestData {");

            sb.append("submittedValue=" + testData.submittedValue());

            if (!Strings.isNullOrEmpty(testData.failureMessage())) {
                sb.append(", failureMessage=" + testData.failureMessage());
            }

            sb.append("}");

            return sb.toString();
        }

        @Override
        public Object invokeExplosively(final Object target, final Object... params) throws Throwable {
            return new ReflectiveCallable() {
                @Override
                protected Object runReflectiveCall() throws Throwable {
                    BaseTest baseTest = (BaseTest) target;

                    baseTest.setSubmittedValue(testData.submittedValue());

                    if (!Strings.isNullOrEmpty(testData.failureMessage())) {
                        baseTest.setFailureMessage(testData.failureMessage());
                    }

                    Object returnValue = getMethod().invoke(target, params);
                    checkResult(baseTest, failure);

                    return returnValue;
                }
            }.run();
        }
    }

    protected abstract void checkResult(BaseTest baseTest, boolean failure) throws Throwable;

    private class ParameterizedTestRunner extends BlockJUnit4ClassRunner {

        private String testMethodName;

        private List<FrameworkMethod> children = Lists.newArrayList();

        public ParameterizedTestRunner(Class<?> klass, FrameworkMethod testMethod) throws InitializationError {

            super(klass);

            this.testMethodName = testMethod.getName();

            TestDataHolder testDataHolder = testMethod.getAnnotation(TestDataHolder.class);
            if (testDataHolder != null) {
                for (TestData testData : testDataHolder.successes()) {
                    children.add(new ParameterizedFrameworkMethod(testMethod.getMethod(), testData, false));
                }

                for (TestData testData : testDataHolder.failures()) {
                    children.add(new ParameterizedFrameworkMethod(testMethod.getMethod(), testData, true));
                }
            } else {
                children.add(testMethod);
            }
        }

        @Override
        public Object createTest() throws Exception {
            return getTestClass().getOnlyConstructor().newInstance();
        }

        @Override
        protected String getName() {
            return testMethodName;
        }

        @Override
        protected String testName(final FrameworkMethod method) {
            if (method instanceof ParameterizedFrameworkMethod) {
                String formattedParamsString = ((ParameterizedFrameworkMethod) method).getFormattedParameters();

                return String.format("%s %s", testMethodName, formattedParamsString);
            }

            return testMethodName;
        }

        @Override
        protected void validateConstructor(List<Throwable> errors) {
            validateOnlyOneConstructor(errors);
        }

        @Override
        protected Statement classBlock(RunNotifier notifier) {
            return childrenInvoker(notifier);
        }

        @Override
        protected List<FrameworkMethod> getChildren() {
            return children;
        }

    }

    private final ArrayList<Runner> runners = new ArrayList<Runner>();

    /**
     * Only called reflectively. Do not use programmatically.
     */
    public BaseTestRunner(Class<?> klass) throws Throwable {
        super(klass, Collections.<Runner> emptyList());

        TestClass testClass = getTestClass();
        List<FrameworkMethod> testMethods = testClass.getAnnotatedMethods(Test.class);
        for (FrameworkMethod testMethod : testMethods) {
            runners.add(new ParameterizedTestRunner(klass, testMethod));
        }
    }

    @Override
    protected List<Runner> getChildren() {
        return runners;
    }
}
