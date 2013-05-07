/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
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

package org.richfaces.javascript;

import org.junit.Assert;
import org.junit.Test;
import org.richfaces.ui.behavior.AnonymousFunctionCall;

public class AnonymousFunctionCallTest {
    // -------------------------- OTHER METHODS --------------------------

    @Test
    public void args2values2() {
        Assert.assertEquals("(function(a,b){alert(a+b)})(1,2)", new AnonymousFunctionCall("a", "b").addParameterValue(1, 2)
                .addToBody("alert(a+b)").toScript());
        Assert.assertEquals("(function(a,b){alert(a+b)})(1,2)", new AnonymousFunctionCall().addParameterName("a", "b")
                .addParameterValue(1, 2).addToBody("alert(a+b)").toScript());
        Assert.assertEquals("(function(a,b){alert(a+b)})(1,2)", new AnonymousFunctionCall().addParameterName("a")
                .addParameterName("b").addParameterValue(1, 2).addToBody("alert(a+b)").toScript());
    }

    @Test
    public void args2values3() {
        Assert.assertEquals("(function(a,b){alert(a+b)})(1,2,3)", new AnonymousFunctionCall("a", "b")
                .addParameterValue(1, 2, 3).addToBody("alert(a+b)").toScript());
    }

    @Test
    public void args3values2() {
        Assert.assertEquals("(function(a,b,c){alert(a+b)})(1,2)",
                new AnonymousFunctionCall("a", "b", "c").addParameterValue(1, 2).addToBody("alert(a+b)").toScript());
    }

    @Test
    public void noArgs() {
        Assert.assertEquals("(function(){alert(123)})()", new AnonymousFunctionCall().addToBody("alert(123)").toScript());
    }

    @Test
    public void noArgsNoBody() {
        Assert.assertEquals("(function(){})()", new AnonymousFunctionCall().toScript());
    }
}
