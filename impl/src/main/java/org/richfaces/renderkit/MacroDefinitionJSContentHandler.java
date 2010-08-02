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

package org.richfaces.renderkit;

import org.richfaces.javacc.RichMacroDefinition;
import org.richfaces.json.JSContentHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Nick Belaevski - mailto:nbelaevski@exadel.com created 21.06.2007
 */
public class MacroDefinitionJSContentHandler extends JSContentHandler {
    private String epilog;
    private String prolog;

    public MacroDefinitionJSContentHandler(Writer writer, String prolog, String epilog) {
        super(writer);
        this.prolog = prolog;
        this.epilog = epilog;
    }

    protected List<?> parseExpressiion(String expressionString) throws SAXException {
        try {
            if (expressionString.length() != 0) {
                List<?> result = new RichMacroDefinition(new StringReader(expressionString)).expression();

                return result;
            } else {
                List<Object> list = new ArrayList<Object>(1);

                list.add("");

                return list;
            }
        } catch (Exception e) {
            throw new SAXException(e.getMessage(), e);
        }
    }

    private void encodeExpressionString(String string) throws IOException, SAXException {

        /*
         * if (string.length() == 0) {
         *   this.outputWriter.write("\'\'");
         * }
         */
        List<?> parsedExpressiion = parseExpressiion(string);
        boolean isExpression = false;

        for (Iterator<?> iterator = parsedExpressiion.iterator(); iterator.hasNext();) {
            Object next = (Object) iterator.next();

            if (next instanceof Expression) {
                isExpression = true;

                break;
            }
        }

        if (isExpression) {
            this.outputWriter.write("function (context) { return ");
        }

        boolean first = true;

        for (Iterator<?> iterator = parsedExpressiion.iterator(); iterator.hasNext();) {
            Object next = (Object) iterator.next();

            if (next == null) {
                continue;
            }

            if (!first) {
                this.outputWriter.write('+');
            }

            if (next instanceof Expression) {
                Expression macroExpression = (Expression) next;

                this.outputWriter.write(prolog);
                this.encode(macroExpression.getExpression().toString());
                this.outputWriter.write(epilog);
            } else {
                this.outputWriter.write('\'');
                this.encode(next.toString());
                this.outputWriter.write('\'');
            }

            first = false;
        }

        if (isExpression) {
            this.outputWriter.write(";}");
        }
    }

    protected void encodeAttributeValue(Attributes attributes, int idx) throws SAXException, IOException {
        String value = attributes.getValue(idx);

        encodeExpressionString(value);
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        if (isProcessingCdata()) {
            super.characters(ch, start, length);
        } else {
            Writer oldWriter = outputWriter;

            outputWriter = new WellFormedWriter(oldWriter);

            try {
                List<?> parsedExpression = parseExpressiion(new String(ch, start, length));

                for (Iterator<?> iterator = parsedExpression.iterator(); iterator.hasNext();) {
                    Object next = iterator.next();

                    if (next instanceof Expression) {
                        Expression expression = (Expression) next;

                        if (this.isBeforeDocumentStart() || (level < 0)) {
                            return;
                        }

                        try {
                            if ((level != 0) && !this.closeElement(false) && !this.isProcessingCdata()) {
                                this.outputWriter.write(',');
                            }

                            if (!this.isProcessingCdata()) {
                                this.outputWriter.write("new ET(");
                            }

                            this.outputWriter.write("function (context) { return ");
                            this.outputWriter.write(prolog);
                            this.encode(expression.getExpression().toString());
                            this.outputWriter.write(epilog);
                            this.outputWriter.write("}");

                            if (!this.isProcessingCdata()) {
                                this.outputWriter.write(")");
                            }
                        } catch (IOException e) {
                            throw new SAXException("Write error", e);
                        }
                    } else {
                        char[] cs = next.toString().toCharArray();

                        super.characters(cs, 0, cs.length);
                    }

                    if (iterator.hasNext()) {
                        try {
                            this.outputWriter.write(',');
                        } catch (IOException e) {

                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            } finally {
                outputWriter = oldWriter;
            }
        }
    }

    protected void encodeText(char[] chars, int start, int length) throws SAXException, IOException {
        if (!isProcessingCdata()) {
            String str = new String(chars, start, length);

            encodeExpressionString(str);
        } else {
            super.encodeText(chars, start, length);
        }
    }

    /**
     * Writer will not output duplicate commas
     *
     * @author Maksim Kaszynski
     */
    static class WellFormedWriter extends FilterWriter {
        private char lastChar;

        public WellFormedWriter(Writer out) {
            super(out);
        }

        public void write(char[] cbuf, int off, int len) throws IOException {

            // Skip comma
            while ((cbuf[off] == lastChar) && (lastChar == ',') && (len > 0)) {
                off++;
                len--;
            }

            while ((len > 1) && (cbuf[off] == cbuf[off + 1]) && (cbuf[off] == ',')) {
                len--;
                off++;
            }

            super.write(cbuf, off, len);
            lastChar = cbuf[off + len - 1];
        }

        public void write(int c) throws IOException {
            if ((c != lastChar) || (lastChar != ',')) {
                super.write(c);
                lastChar = (char) c;
            }
        }
    }
}
