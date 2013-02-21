package org.richfaces.context;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.PartialResponseWriter;
import javax.faces.context.ResponseWriter;

/**
 * Wraps {@link PartialResponseWriter} and delegates all calls to it.
 *
 * @author Lukas Fryc
 */
public abstract class PartialResponseWriterWrapper extends PartialResponseWriter {

    private PartialResponseWriter wrapped;

    public PartialResponseWriterWrapper(PartialResponseWriter wrapped) {
        super(wrapped);
        this.wrapped = wrapped;
    }

    public PartialResponseWriter getWrapped() {
        return wrapped;
    }

    @Override
    public void startDocument() throws IOException {

        wrapped.startDocument();
    }

    @Override
    public void endDocument() throws IOException {

        wrapped.endDocument();
    }

    @Override
    public void startInsertBefore(String targetId) throws IOException {

        wrapped.startInsertBefore(targetId);
    }

    @Override
    public void startInsertAfter(String targetId) throws IOException {

        wrapped.startInsertAfter(targetId);
    }

    @Override
    public void endInsert() throws IOException {

        wrapped.endInsert();
    }

    @Override
    public void startUpdate(String targetId) throws IOException {

        wrapped.startUpdate(targetId);
    }

    @Override
    public void endUpdate() throws IOException {

        wrapped.endUpdate();
    }

    @Override
    public void updateAttributes(String targetId, Map<String, String> attributes) throws IOException {

        wrapped.updateAttributes(targetId, attributes);
    }

    @Override
    public void delete(String targetId) throws IOException {

        wrapped.delete(targetId);
    }

    @Override
    public void redirect(String url) throws IOException {

        wrapped.redirect(url);
    }

    @Override
    public void startEval() throws IOException {

        wrapped.startEval();
    }

    @Override
    public void endEval() throws IOException {

        wrapped.endEval();
    }

    @Override
    public void startExtension(Map<String, String> attributes) throws IOException {

        wrapped.startExtension(attributes);
    }

    @Override
    public void endExtension() throws IOException {

        wrapped.endExtension();
    }

    @Override
    public void startError(String errorName) throws IOException {

        wrapped.startError(errorName);
    }

    @Override
    public void endError() throws IOException {

        wrapped.endError();
    }

    @Override
    public String getContentType() {

        return wrapped.getContentType();
    }

    @Override
    public String getCharacterEncoding() {

        return wrapped.getCharacterEncoding();
    }

    @Override
    public void flush() throws IOException {

        wrapped.flush();
    }

    @Override
    public void startElement(String name, UIComponent component) throws IOException {

        wrapped.startElement(name, component);
    }

    @Override
    public void startCDATA() throws IOException {

        wrapped.startCDATA();
    }

    @Override
    public void endCDATA() throws IOException {

        wrapped.endCDATA();
    }

    @Override
    public void endElement(String name) throws IOException {

        wrapped.endElement(name);
    }

    @Override
    public void writeAttribute(String name, Object value, String property) throws IOException {

        wrapped.writeAttribute(name, value, property);
    }

    @Override
    public void writeURIAttribute(String name, Object value, String property) throws IOException {

        wrapped.writeURIAttribute(name, value, property);
    }

    @Override
    public void writeComment(Object comment) throws IOException {

        wrapped.writeComment(comment);
    }

    @Override
    public void writeText(Object text, String property) throws IOException {

        wrapped.writeText(text, property);
    }

    @Override
    public void writeText(Object text, UIComponent component, String property) throws IOException {

        wrapped.writeText(text, component, property);
    }

    @Override
    public void writeText(char[] text, int off, int len) throws IOException {

        wrapped.writeText(text, off, len);
    }

    @Override
    public ResponseWriter cloneWithWriter(Writer writer) {

        return wrapped.cloneWithWriter(writer);
    }

    @Override
    public void close() throws IOException {

        wrapped.close();
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {

        wrapped.write(cbuf, off, len);
    }

    @Override
    public void write(int c) throws IOException {

        wrapped.write(c);
    }

    @Override
    public void write(char[] cbuf) throws IOException {

        wrapped.write(cbuf);
    }

    @Override
    public void write(String str) throws IOException {

        wrapped.write(str);
    }

    @Override
    public void write(String str, int off, int len) throws IOException {

        wrapped.write(str, off, len);
    }

    @Override
    public Writer append(CharSequence csq) throws IOException {

        return wrapped.append(csq);
    }

    @Override
    public Writer append(CharSequence csq, int start, int end) throws IOException {

        return wrapped.append(csq, start, end);
    }

    @Override
    public Writer append(char c) throws IOException {

        return wrapped.append(c);
    }
}
