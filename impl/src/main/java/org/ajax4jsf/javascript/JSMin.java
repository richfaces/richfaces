/*
 * JSMin.java 2006-02-13
 *
 * Copyright (c) 2006 John Reilly (www.inconspicuous.org)
 *
 * This work is a translation from C to Java of jsmin.c published by
 * Douglas Crockford.  Permission is hereby granted to use the Java
 * version under the same conditions as the jsmin.c on which it is
 * based.
 *
 *
 *
 *
 * jsmin.c 2003-04-21
 *
 * Copyright (c) 2002 Douglas Crockford (www.crockford.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * The Software shall be used for Good, not Evil.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.ajax4jsf.javascript;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;

public class JSMin {
    private static final int EOF = -1;

    private int column;
    private PushbackInputStream in;
    private int line;
    private OutputStream out;
    private int theA;
    private int theB;

    public JSMin(InputStream in, OutputStream out) {
        this.in = new PushbackInputStream(in, 2);
        this.out = out;
        this.line = 0;
        this.column = 0;
    }

    /**
     * isAlphanum -- return true if the character is a letter, digit,
     * underscore, dollar sign, or non-ASCII character.
     */
    static boolean isAlphanum(int c) {
        return c >= 'a' && c <= 'z' || ((c >= '0') && (c <= '9')) || ((c >= 'A') && (c <= 'Z')) || (c == '_')
            || (c == '$') || (c == '\\') || (c > 126);
    }

    /**
     * get -- return the next character from stdin. Watch out for lookahead. If
     * the character is a control character, translate it to a space or
     * linefeed.
     */
    int get() throws IOException {
        int c = in.read();

        if (c == '\n') {
            line++;
            column = 0;
        } else {
            column++;
        }

        if ((c >= ' ') || (c == '\n') || (c == EOF)) {
            return c;
        }

        if (c == '\r') {
            column = 0;

            return '\n';
        }

        return ' ';
    }

    /**
     * Get the next character without getting it.
     */
    int peek() throws IOException {
        int lookaheadChar = in.read();

        in.unread(lookaheadChar);

        return lookaheadChar;
    }

    void back(int c) throws IOException {
        in.unread(c);
    }

    /**
     * next -- get the next character, excluding comments. peek() is used to see
     * if a '/' is followed by a '/' or '*'.
     */
    int next() throws IOException, UnterminatedCommentException {
        int c = get();

        if (c == '/') {
            switch (peek()) {
                case '/':
                    for (; ;) {
                        c = get();

                        if (c <= '\n') {
                            return c;
                        }
                    }
                case '*':
                    get();

                    if (peek() == '@') {

                        // TODO: add spaces skipping
                        back('*');

                        return c;
                    } else {
                        for (; ;) {
                            switch (get()) {
                                case '*':
                                    if (peek() == '/') {
                                        get();

                                        return ' ';
                                    }

                                    break;

                                case EOF:
                                    throw new UnterminatedCommentException(line, column);

                                default:
                                    throw new IllegalStateException();
                            }
                        }
                    }
                default:
                    return c;
            }
        }

        return c;
    }

    /**
     * action -- do something! What you do is determined by the argument: 1
     * Output A. Copy B to A. Get the next B. 2 Copy B to A. Get the next B.
     * (Delete A). 3 Get the next B. (Delete B). action treats a string as a
     * single character. Wow! action recognizes a regular expression if it is
     * preceded by ( or , or =.
     */
    void action(int d) throws IOException, UnterminatedRegExpLiteralException, UnterminatedCommentException,
        UnterminatedStringLiteralException {

        switch (d) {
            case 1:
                out.write(theA);
            case 2:
                theA = theB;

                if ((theA == '\'') || (theA == '"')) {
                    for (; ;) {
                        out.write(theA);
                        theA = get();

                        if (theA == theB) {
                            break;
                        }

                        if (theA <= '\n') {
                            throw new UnterminatedStringLiteralException(line, column);
                        }

                        if (theA == '\\') {
                            out.write(theA);
                            theA = get();
                        }
                    }
                }
            case 3:
                theB = next();

                if ((theB == '/')
                    && ((theA == '(') || (theA == '!') || (theA == '&') || (theA == '|') || (theA == ',')
                    || (theA == '=') || (theA == ':') || (theA == '['))) {
                    out.write(theA);
                    out.write(theB);

                    for (; ;) {
                        theA = get();

                        if (theA == '/') {
                            break;
                        } else if (theA == '\\') {
                            out.write(theA);
                            theA = get();
                        } else if (theA <= '\n') {
                            throw new UnterminatedRegExpLiteralException(line, column);
                        }

                        out.write(theA);
                    }

                    theB = next();
                }
            default:

                // Do nothink.
        }
    }

    /**
     * jsmin -- Copy the input to the output, deleting the characters which are
     * insignificant to JavaScript. Comments will be removed. Tabs will be
     * replaced with spaces. Carriage returns will be replaced with linefeeds.
     * Most spaces and linefeeds will be removed.
     */
    public void jsmin()
        throws IOException, UnterminatedRegExpLiteralException, UnterminatedCommentException,
        UnterminatedStringLiteralException {
        theA = '\n';
        action(3);

        while (theA != EOF) {
            switch (theA) {
                case ' ':
                    if (isAlphanum(theB)) {
                        action(1);
                    } else {
                        action(2);
                    }

                    break;

                case '\n':
                    switch (theB) {
                        case '{':
                        case '[':
                        case '(':
                        case '+':
                        case '-':
                            action(1);

                            break;

                        case ' ':
                            action(3);

                            break;

                        default:
                            if (isAlphanum(theB)) {
                                action(1);
                            } else {
                                action(2);
                            }
                    }

                    break;

                default:
                    switch (theB) {
                        case ' ':
                            if (isAlphanum(theA)) {
                                action(1);

                                break;
                            }

                            action(3);

                            break;

                        case '\n':
                            switch (theA) {
                                case '}':
                                case ']':
                                case ')':
                                case '+':
                                case '-':
                                case '"':
                                case '\'':
                                    action(1);

                                    break;

                                default:
                                    if (isAlphanum(theA)) {
                                        action(1);
                                    } else {
                                        action(3);
                                    }
                            }

                            break;

                        default:
                            action(1);

                            break;
                    }
            }
        }

        out.flush();
    }

    /**
     * Run JSMin
     * <p/>
     * Get the first param on file name and
     * output on console compressed file
     */
    public static void main(String[] arg) {
        try {
            JSMin jsmin = new JSMin(new FileInputStream(arg[0]), System.out);

            jsmin.jsmin();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnterminatedRegExpLiteralException e) {
            e.printStackTrace();
        } catch (UnterminatedCommentException e) {
            e.printStackTrace();
        } catch (UnterminatedStringLiteralException e) {
            e.printStackTrace();
        }
    }

    static class UnterminatedCommentException extends Exception {
        private static final long serialVersionUID = -5847720072617372584L;

        UnterminatedCommentException(int line, int column) {
            super("Unterminated comment at line " + line + " and column " + column);
        }
    }

    static class UnterminatedRegExpLiteralException extends Exception {
        private static final long serialVersionUID = 8791439031230079257L;

        UnterminatedRegExpLiteralException(int line, int column) {
            super("Unterminated regular expression at line " + line + " and column " + column);
        }
    }

    static class UnterminatedStringLiteralException extends Exception {
        private static final long serialVersionUID = 5256369791002619999L;

        UnterminatedStringLiteralException(int line, int column) {
            super("Unterminated string literal at line " + line + " and column " + column);
        }
    }
}
