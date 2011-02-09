package org.richfaces.demo.images;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import org.richfaces.renderkit.html.BaseGradient;

public class PageFooterGradient extends BaseGradient {

    @Override
    protected void paintGradient(Graphics2D g2d, Dimension dim) {
        Dimension halfHeightDim = new Dimension();
        halfHeightDim.setSize(dim.getWidth(), dim.getHeight() / 2); 
        super.paintGradient(g2d, halfHeightDim);
        AffineTransform transform = new AffineTransform(1, 0, 0, -1, 0, getSafeHeight());
        g2d.transform(transform);
        super.paintGradient(g2d, halfHeightDim);
    }
}
