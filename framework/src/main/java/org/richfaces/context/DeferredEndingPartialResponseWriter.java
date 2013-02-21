package org.richfaces.context;

import java.io.IOException;

import javax.faces.context.PartialResponseWriter;

/**
 * <p>
 * This {@link PartialResponseWriter} wrapper doesn't end the document when {@link #endDocument()} is called to allow write own
 * partial-response extensions at the end of the document.
 * </p>
 *
 * <p>
 * The method {@link #finallyEndDocument()} needs to be called to actually end the document.
 * </p>
 *
 * @author Lukas Fryc
 */
public class DeferredEndingPartialResponseWriter extends PartialResponseWriterWrapper {

    public DeferredEndingPartialResponseWriter(PartialResponseWriter wrapped) {
        super(wrapped);
    }

    /**
     * The invocation will be blocked, you need to call {@link #finallyEndDocument()} to actually end the document.
     */
    @Override
    public void endDocument() throws IOException {
    }

    /**
     * Will finally end the document since {@link #endDocument()} was blocked to allow to write partial-response extensions.
     *
     * @throws IOException if an input/output error occurs
     */
    void finallyEndDocument() throws IOException {
        super.endDocument();
    }

}
