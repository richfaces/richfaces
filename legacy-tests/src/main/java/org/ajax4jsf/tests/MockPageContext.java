
/*
* PageContextMock.java     Date created: 14.12.2007
* Last modified by: $Author$
* $Revision$   $Date$
 */
package org.ajax4jsf.tests;

import java.io.IOException;

import java.util.Enumeration;

import javax.el.ELContext;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.el.ExpressionEvaluator;
import javax.servlet.jsp.el.VariableResolver;

/**
 * TODO Class description goes here.
 * @author Andrey Markavtsov
 *
 */
public class MockPageContext extends PageContext {
    private ELContext ELContext;

    /**
     * TODO Description goes here.
     */
    public MockPageContext() {
        super();
        ELContext = new MockELContext();

        // TODO Auto-generated constructor stub
    }

    /*
     *  (non-Javadoc)
     * @see javax.servlet.jsp.PageContext#forward(java.lang.String)
     */
    @Override
    public void forward(String relativeUrlPath) throws ServletException, IOException {

        // TODO Auto-generated method stub
    }

    /*
     *  (non-Javadoc)
     * @see javax.servlet.jsp.PageContext#getException()
     */
    @Override
    public Exception getException() {

        // TODO Auto-generated method stub
        return null;
    }

    /*
     *  (non-Javadoc)
     * @see javax.servlet.jsp.PageContext#getPage()
     */
    @Override
    public Object getPage() {

        // TODO Auto-generated method stub
        return null;
    }

    /*
     *  (non-Javadoc)
     * @see javax.servlet.jsp.PageContext#getRequest()
     */
    @Override
    public ServletRequest getRequest() {

        // TODO Auto-generated method stub
        return null;
    }

    /*
     *  (non-Javadoc)
     * @see javax.servlet.jsp.PageContext#getResponse()
     */
    @Override
    public ServletResponse getResponse() {

        // TODO Auto-generated method stub
        return null;
    }

    /*
     *  (non-Javadoc)
     * @see javax.servlet.jsp.PageContext#getServletConfig()
     */
    @Override
    public ServletConfig getServletConfig() {

        // TODO Auto-generated method stub
        return null;
    }

    /*
     *  (non-Javadoc)
     * @see javax.servlet.jsp.PageContext#getServletContext()
     */
    @Override
    public ServletContext getServletContext() {

        // TODO Auto-generated method stub
        return null;
    }

    /*
     *  (non-Javadoc)
     * @see javax.servlet.jsp.PageContext#getSession()
     */
    @Override
    public HttpSession getSession() {

        // TODO Auto-generated method stub
        return null;
    }

    /*
     *  (non-Javadoc)
     * @see javax.servlet.jsp.PageContext#handlePageException(java.lang.Exception)
     */
    @Override
    public void handlePageException(Exception e) throws ServletException, IOException {

        // TODO Auto-generated method stub
    }

    /*
     *  (non-Javadoc)
     * @see javax.servlet.jsp.PageContext#handlePageException(java.lang.Throwable)
     */
    @Override
    public void handlePageException(Throwable t) throws ServletException, IOException {

        // TODO Auto-generated method stub
    }

    /*
     *  (non-Javadoc)
     * @see javax.servlet.jsp.PageContext#include(java.lang.String)
     */
    @Override
    public void include(String relativeUrlPath) throws ServletException, IOException {

        // TODO Auto-generated method stub
    }

    /*
     *  (non-Javadoc)
     * @see javax.servlet.jsp.PageContext#include(java.lang.String, boolean)
     */
    @Override
    public void include(String relativeUrlPath, boolean flush) throws ServletException, IOException {

        // TODO Auto-generated method stub
    }

    /*
     *  (non-Javadoc)
     * @see javax.servlet.jsp.PageContext#initialize(javax.servlet.Servlet, javax.servlet.ServletRequest, javax.servlet.ServletResponse, java.lang.String, boolean, int, boolean)
     */
    @Override
    public void initialize(Servlet servlet, ServletRequest request, ServletResponse response, String errorPageURL,
                           boolean needsSession, int bufferSize, boolean autoFlush)
            throws IOException, IllegalStateException, IllegalArgumentException {

        // TODO Auto-generated method stub
    }

    /*
     *  (non-Javadoc)
     * @see javax.servlet.jsp.PageContext#release()
     */
    @Override
    public void release() {

        // TODO Auto-generated method stub
    }

    /*
     *  (non-Javadoc)
     * @see javax.servlet.jsp.JspContext#findAttribute(java.lang.String)
     */
    @Override
    public Object findAttribute(String name) {

        // TODO Auto-generated method stub
        return null;
    }

    /*
     *  (non-Javadoc)
     * @see javax.servlet.jsp.JspContext#getAttribute(java.lang.String)
     */
    @Override
    public Object getAttribute(String name) {

        // TODO Auto-generated method stub
        return null;
    }

    /*
     *  (non-Javadoc)
     * @see javax.servlet.jsp.JspContext#getAttribute(java.lang.String, int)
     */
    @Override
    public Object getAttribute(String name, int scope) {

        // TODO Auto-generated method stub
        return null;
    }

    /*
     *  (non-Javadoc)
     * @see javax.servlet.jsp.JspContext#getAttributeNamesInScope(int)
     */
    @Override
    public Enumeration<String> getAttributeNamesInScope(int scope) {

        // TODO Auto-generated method stub
        return null;
    }

    /*
     *  (non-Javadoc)
     * @see javax.servlet.jsp.JspContext#getAttributesScope(java.lang.String)
     */
    @Override
    public int getAttributesScope(String name) {

        // TODO Auto-generated method stub
        return 0;
    }

    /*
     *  (non-Javadoc)
     * @see javax.servlet.jsp.JspContext#getELContext()
     */
    @Override
    public ELContext getELContext() {

        // TODO Auto-generated method stub
        return this.ELContext;
    }

    /*
     *  (non-Javadoc)
     * @see javax.servlet.jsp.JspContext#getExpressionEvaluator()
     */
    @Override
    public ExpressionEvaluator getExpressionEvaluator() {

        // TODO Auto-generated method stub
        return null;
    }

    /*
     *  (non-Javadoc)
     * @see javax.servlet.jsp.JspContext#getOut()
     */
    @Override
    public JspWriter getOut() {

        // TODO Auto-generated method stub
        return new JspWriter(0, false) {
            @Override
            public void clear() throws IOException {

                // TODO Auto-generated method stub
            }
            @Override
            public void clearBuffer() throws IOException {

                // TODO Auto-generated method stub
            }
            @Override
            public void close() throws IOException {

                // TODO Auto-generated method stub
            }
            @Override
            public void flush() throws IOException {

                // TODO Auto-generated method stub
            }
            @Override
            public int getRemaining() {

                // TODO Auto-generated method stub
                return 0;
            }
            @Override
            public void newLine() throws IOException {

                // TODO Auto-generated method stub
            }
            @Override
            public void print(boolean b) throws IOException {

                // TODO Auto-generated method stub
            }
            @Override
            public void print(char c) throws IOException {

                // TODO Auto-generated method stub
            }
            @Override
            public void print(int i) throws IOException {

                // TODO Auto-generated method stub
            }
            @Override
            public void print(long l) throws IOException {

                // TODO Auto-generated method stub
            }
            @Override
            public void print(float f) throws IOException {

                // TODO Auto-generated method stub
            }
            @Override
            public void print(double d) throws IOException {

                // TODO Auto-generated method stub
            }
            @Override
            public void print(char[] s) throws IOException {

                // TODO Auto-generated method stub
            }
            @Override
            public void print(String s) throws IOException {

                // TODO Auto-generated method stub
            }
            @Override
            public void print(Object obj) throws IOException {

                // TODO Auto-generated method stub
            }
            @Override
            public void println() throws IOException {

                // TODO Auto-generated method stub
            }
            @Override
            public void println(boolean x) throws IOException {

                // TODO Auto-generated method stub
            }
            @Override
            public void println(char x) throws IOException {

                // TODO Auto-generated method stub
            }
            @Override
            public void println(int x) throws IOException {

                // TODO Auto-generated method stub
            }
            @Override
            public void println(long x) throws IOException {

                // TODO Auto-generated method stub
            }
            @Override
            public void println(float x) throws IOException {

                // TODO Auto-generated method stub
            }
            @Override
            public void println(double x) throws IOException {

                // TODO Auto-generated method stub
            }
            @Override
            public void println(char[] x) throws IOException {

                // TODO Auto-generated method stub
            }
            @Override
            public void println(String x) throws IOException {

                // TODO Auto-generated method stub
            }
            @Override
            public void println(Object x) throws IOException {

                // TODO Auto-generated method stub
            }
            @Override
            public void write(char[] cbuf, int off, int len) throws IOException {

                // TODO Auto-generated method stub
            }
        };
    }

    /*
     *  (non-Javadoc)
     * @see javax.servlet.jsp.JspContext#getVariableResolver()
     */
    @Override
    public VariableResolver getVariableResolver() {

        // TODO Auto-generated method stub
        return null;
    }

    /*
     *  (non-Javadoc)
     * @see javax.servlet.jsp.JspContext#removeAttribute(java.lang.String)
     */
    @Override
    public void removeAttribute(String name) {

        // TODO Auto-generated method stub
    }

    /*
     *  (non-Javadoc)
     * @see javax.servlet.jsp.JspContext#removeAttribute(java.lang.String, int)
     */
    @Override
    public void removeAttribute(String name, int scope) {

        // TODO Auto-generated method stub
    }

    /*
     *  (non-Javadoc)
     * @see javax.servlet.jsp.JspContext#setAttribute(java.lang.String, java.lang.Object)
     */
    @Override
    public void setAttribute(String name, Object value) {

        // TODO Auto-generated method stub
    }

    /*
     *  (non-Javadoc)
     * @see javax.servlet.jsp.JspContext#setAttribute(java.lang.String, java.lang.Object, int)
     */
    @Override
    public void setAttribute(String name, Object value, int scope) {

        // TODO Auto-generated method stub
    }
}
