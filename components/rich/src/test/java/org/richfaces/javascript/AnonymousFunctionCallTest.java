package org.richfaces.javascript;

import org.junit.Assert;
import org.junit.Test;
import org.richfaces.component.behavior.AnonymousFunctionCall;

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
