/**
 * License Agreement.
 *
 * Rich Faces - Natural Ajax for Java Server Faces (JSF)
 *
 * Copyright (C) 2007 Exadel, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */



package org.ajax4jsf.tests;

import javax.faces.context.FacesContext;

import junit.framework.AssertionFailedError;
import junit.framework.TestResult;

import org.ajax4jsf.context.AjaxContext;
import org.ajax4jsf.context.AjaxContextImpl;

import org.apache.shale.test.mock.MockFacesContext;
import org.apache.shale.test.mock.MockHttpServletRequest;
import org.apache.shale.test.mock.MockHttpServletResponse;
import org.apache.shale.test.mock.MockHttpSession;

/**
 * @author asmirnov@exadel.com (latest modification by $Author: ishabalov $)
 * @version $Revision: 1.1.2.3 $ $Date: 2007/02/20 20:58:08 $
 *
 *
 * A multi-threaded JUnit test case, with setup faces environment.
 * To perform test cases that spin off threads to do tests: <p>
 * <UL>
 * <LI>Extend <code>MultiThreadedTestCase</code>
 * <LI>Write your tests cases as normal except for when you want to spin off threads.
 * <LI>When you want to spin off threads:
 * <UL>
 * <LI>Instead of implementing <code>Runnable</code> extend <code>MultiThreadedTestCase.TestCaseRunnable</code>.
 * <LI>Define <code>runTestCase ()</code> to do your test, you may call <code>fail (), assert ()</code> etc. and throw
 * exceptions with impunity.
 * <LI>Handle thread interrupts by finishing.
 * </UL>
 * <LI>Instantiate all the runnables (one for each thread you wish to spawn) and pass an array of them
 * to <code>runTestCaseRunnables ()</code>.
 * </UL>
 * That's it. An example is below:
 * <PRE>
 * public class MTTest extends ThreadedVcpJsfTestCase
 * {
 *   MTTest (String s) { super (s); }
 *   public class CounterThread extends TestCaseRunnable
 *   {
 *     public void runTestCase (FacesContext context) throws Throwable
 *     {
 *       for (int i = 0; i < 1000; i++)
 *       {
 *         System.out.println ("Counter Thread: " + Thread.currentThread () + " : " + i);
 *         // Do some testing...
 *         if (Thread.currentThread ().isInterrupted ()) {
 *           return;
 *         }
 *       }
 *     }
 *   }
 *
 *   public void test1 ()
 *   {
 *     TestCaseRunnable tct [] = new TestCaseRunnable [5];
 *     for (int i = 0; i < 5; i++)
 *     {
 *       tct[i] = new CounterThread ();
 *      }
 *     runTestCaseRunnables (tct);
 *   }
 * }
 * </PRE>
 * <BR><STRONG>Category: Test</STRONG>
 * <BR><STRONG>Not guaranteed to be thread safe.</STRONG>
 */
public class AbstractThreadedAjax4JsfTestCase extends AbstractAjax4JsfTestCase {

    /**
     * The tests TestResult.
     */
    private TestResult testResult = null;

    /**
     * The threads that are executing.
     */
    private Thread threads[] = null;

    /**
     * Simple constructor.
     */
    public AbstractThreadedAjax4JsfTestCase(String s) {
        super(s);
    }

    /**
     * Interrupt the running threads.
     */
    public void interruptThreads() {
        if (threads != null) {
            for (int i = 0; i < threads.length; i++) {
                threads[i].interrupt();
            }
        }
    }

    /**
     * Override run so we can squirrel away the test result.
     */
    public void run(final TestResult result) {
        testResult = result;
        super.run(result);
        testResult = null;
    }

    /**
     * Create instances of classes and run threads with it.
     * @param clazz - class of test thread implementation.
     * @param numThreads - number of threads to run.
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    protected void runTestCaseThreads(Class clazz, int numThreads) {
        TestCaseRunnable[] runnables = new TestCaseRunnable[numThreads];

        for (int i = 0; i < runnables.length; i++) {
            try {
                runnables[i] = (TestCaseRunnable) clazz.newInstance();
            } catch (Exception e) {
                testResult.addError(this, e);

                return;
            }
        }

        runTestCaseRunnables(runnables);
    }

    /**
     * Run the test case threads.
     * @param runnables - array with instances of {@link TestCaseRunnable} with concrete tests
     */
    protected void runTestCaseRunnables(final TestCaseRunnable[] runnables) {
        if (runnables == null) {
            throw new IllegalArgumentException("runnables is null");
        }

        threads = new Thread[runnables.length];

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(runnables[i]);
        }

        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }

        try {
            for (int i = 0; i < threads.length; i++) {
                threads[i].join();
            }
        } catch (InterruptedException ignore) {
            System.out.println("Thread join interrupted.");
        }

        threads = null;
    }

    /**
     * Handle an exception. Since multiple threads won't have their
     * exceptions caught the threads must manually catch them and call
     * <code>handleException ()</code>.
     * @param t Exception to handle.
     */
    private void handleException(final Throwable t) {
        synchronized (testResult) {
            if (t instanceof AssertionFailedError) {
                testResult.addFailure(this, (AssertionFailedError) t);
            } else {
                testResult.addError(this, t);
            }
        }
    }

    /**
     * Create instance of faces context for current thread.
     * @return
     */
    protected FacesContext createFacesContext() {
        MockHttpSession tsession = new MockHttpSession();
        MockHttpServletRequest trequest = new MockHttpServletRequest(tsession);

        trequest.setAttribute(AjaxContext.AJAX_CONTEXT_KEY, new AjaxContextImpl());

        MockHttpServletResponse tresponse = new MockHttpServletResponse();
        MockFacesContext tfacesContext = (MockFacesContext) facesContextFactory.getFacesContext(servletContext,
                                             trequest, tresponse, lifecycle);

        tfacesContext.setApplication(application);
        tfacesContext.setViewRoot(application.getViewHandler().createView(facesContext,
                facesContext.getViewRoot().getViewId()));

        return tfacesContext;
    }

    /**
     * A test case thread. Override runTestCase () and define
     * behaviour of test in there.
     */
    public abstract class TestCaseRunnable implements Runnable {

        /**
         * Override this to define the test
         */
        public abstract void runTestCase(FacesContext context) throws Throwable;

        /**
         * Run the test in an environment where
         * we can handle the exceptions generated by the test method.
         */
        public void run() {
            FacesContext context = null;

            try {
                context = createFacesContext();
                runTestCase(context);
            } catch (Throwable t) /* Any other exception we handle and then we interrupt the other threads. */ {
                handleException(t);
                interruptThreads();
            } finally {
                if (null != context) {
                    context.release();
                }
            }
        }
    }
}
