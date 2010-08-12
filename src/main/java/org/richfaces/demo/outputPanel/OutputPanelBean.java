package org.richfaces.demo.outputPanel;

/**
 * @author Ilya Shaikovsky This bean used for outputPanel demo. It's not annotation defined because we need two
 *         instances under different names. So used faces-config.
 */
public class OutputPanelBean {
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
