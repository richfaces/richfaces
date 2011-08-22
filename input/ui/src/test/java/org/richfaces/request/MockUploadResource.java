/*
 * GENERATED FILE - DO NOT EDIT
 */
package org.richfaces.request;

import static org.easymock.EasyMock.createControl;
import static org.jboss.test.faces.mock.FacesMockController.findMethod;
import static org.jboss.test.faces.mock.FacesMockController.invokeMethod;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

import org.easymock.IMocksControl;
import org.easymock.internal.ThrowableWrapper;
import org.jboss.test.faces.mock.FacesMockController.MockObject;

public class MockUploadResource extends FileUploadResource implements MockObject {
    private final IMocksControl control;
    private final String name;

    /**
     * Default constructor
     */
    public MockUploadResource() {
        super(null, null);
        this.control = createControl();
        this.name = null;
    }

    /**
     * @param control
     */
    public MockUploadResource(IMocksControl control, String name) {
        super(null, null);
        this.control = control;
        this.name = name;
    }

    public IMocksControl getControl() {
        return control;
    }

    public String toString() {
        return getClass().getSimpleName() + (name != null ? name : "");
    }

    public boolean equals(Object obj) {
        return this == obj;
    }

    public int hashCode() {
        if (name != null) {
            final int prime = 31;
            int result = 1;
            result = prime * result + name.hashCode();
            result = prime * result + getClass().getName().hashCode();
            return result;
        } else {
            return System.identityHashCode(this);
        }
    }

    private static final Method createMethod0 = findMethod(FileUploadResource.class, "create");

    public void create() throws IOException {
        invokeMethod(this.control, this, createMethod0);
    }

    private static final Method completeMethod0 = findMethod(FileUploadResource.class, "complete");

    public void complete() {
        invokeMethod(this.control, this, completeMethod0);
    }

    private static final Method handleMethod0 = findMethod(FileUploadResource.class, "handle", byte[].class, Integer.TYPE);

    public void handle(byte[] bytes, int length) throws IOException {
        invokeMethod(this.control, this, handleMethod0, bytes, length);
    }

    private static final Method getInputStreamMethod0 = findMethod(FileUploadResource.class, "getInputStream");

    @Override
    public InputStream getInputStream() throws IOException {
        try {
            return invokeMethod(this.control, this, getInputStreamMethod0);
        } catch (RuntimeException e) {
            if (e.getCause() instanceof ThrowableWrapper
                && ((ThrowableWrapper) e.getCause()).getThrowable() instanceof IOException) {
                throw (IOException) ((ThrowableWrapper) e.getCause()).getThrowable();
            } else {
                throw e;
            }
        }
    }

    private static final Method getSizeMethod0 = findMethod(FileUploadResource.class, "getSize");

    @Override
    public long getSize() {
        return (Long) invokeMethod(this.control, this, getSizeMethod0);
    }

    private static final Method writeMethod0 = findMethod(FileUploadResource.class, "write", String.class);

    @Override
    public void write(String fileName) throws IOException {
        invokeMethod(this.control, this, writeMethod0, fileName);
    }

    private static final Method deleteMethod0 = findMethod(FileUploadResource.class, "delete");

    @Override
    public void delete() throws IOException {
        invokeMethod(this.control, this, deleteMethod0);
    }
}
