/**
 *
 */
package org.richfaces.renderkit.html;

import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import org.richfaces.renderkit.html.images.GradientType;
import org.richfaces.renderkit.html.images.GradientType.BiColor;

/**
 * @author asmirnov
 */
public abstract class Base2WayGradient extends BaseGradient {

    /**
     * @param horizontal
     */
    public Base2WayGradient(boolean horizontal) {
        super(horizontal);

        // TODO Auto-generated constructor stub
    }

    /**
     * @param width
     * @param height
     */
    public Base2WayGradient(int width, int height) {
        super(width, height);

        // TODO Auto-generated constructor stub
    }

    /**
     * @param baseColor
     * @param gradientColor
     */
    public Base2WayGradient(String baseColor, String gradientColor) {
        super(baseColor, gradientColor);

        // TODO Auto-generated constructor stub
    }

    /**
     * @param width
     * @param height
     * @param horizontal
     */
    public Base2WayGradient(int width, int height, boolean horizontal) {
        super(width, height, horizontal);

        // TODO Auto-generated constructor stub
    }

    /**
     * @param width
     * @param height
     * @param gradientHeight
     */
    public Base2WayGradient(int width, int height, int gradientHeight) {
        super(width, height, gradientHeight);

        // TODO Auto-generated constructor stub
    }

    /**
     * @param baseColor
     * @param gradientColor
     * @param horizontal
     */
    public Base2WayGradient(String baseColor, String gradientColor, boolean horizontal) {
        super(baseColor, gradientColor, horizontal);

        // TODO Auto-generated constructor stub
    }

    /**
     * @param width
     * @param height
     * @param gradientHeight
     * @param horizontal
     */
    public Base2WayGradient(int width, int height, int gradientHeight, boolean horizontal) {
        super(width, height, gradientHeight, horizontal);

        // TODO Auto-generated constructor stub
    }

    /**
     * @param width
     * @param height
     * @param baseColor
     * @param gradientColor
     */
    public Base2WayGradient(int width, int height, String baseColor, String gradientColor) {
        super(width, height, baseColor, gradientColor);

        // TODO Auto-generated constructor stub
    }

    /**
     * @param width
     * @param height
     * @param gradientHeight
     * @param baseColor
     * @param gradientColor
     */
    public Base2WayGradient(int width, int height, int gradientHeight, String baseColor, String gradientColor) {
        super(width, height, gradientHeight, baseColor, gradientColor);

        // TODO Auto-generated constructor stub
    }

    /**
     * @param width
     * @param height
     * @param baseColor
     * @param gradientColor
     * @param horizontal
     */
    public Base2WayGradient(int width, int height, String baseColor, String gradientColor, boolean horizontal) {
        super(width, height, baseColor, gradientColor, horizontal);

        // TODO Auto-generated constructor stub
    }

    /**
     * @param width
     * @param height
     * @param gradientHeight
     * @param baseColor
     * @param gradientColor
     * @param horizontal
     */
    public Base2WayGradient(int width, int height, int gradientHeight, String baseColor, String gradientColor,
                            boolean horizontal) {
        super(width, height, gradientHeight, baseColor, gradientColor, horizontal);

        // TODO Auto-generated constructor stub
    }

    protected void drawBackGradient(Graphics2D g2d, Rectangle2D rectangle, BiColor colors, int height) {
        if (colors != null) {
            GradientPaint gragient = new GradientPaint(0, (float) (rectangle.getHeight() - height),
                colors.getBottomColor(), 0, (float) rectangle.getHeight(),
                colors.getTopColor());

            g2d.setPaint(gragient);
            g2d.fill(rectangle);
        }
    }

    @Override
    protected void paintGradient(Graphics2D g2d, Dimension dim) {
        if ((headerBackgroundColor != null) && (headerGradientColor != null)) {
            BiColor biColor = new GradientType.BiColor(headerBackgroundColor, headerGradientColor);
            BiColor firstLayer = gradientType.getFirstLayerColors(biColor);
            BiColor secondLayer = gradientType.getSecondLayerColors(biColor);

            if (isHorizontal()) {

                // x -> y, y -> x
                g2d.transform(new AffineTransform(0, 1, 1, 0, 0, 0));
                dim.setSize(dim.height, dim.width);
            }

            int localGradientHeight = getGradientHeight();

            if (localGradientHeight < 0) {
                localGradientHeight = dim.height / 2;
            }

            Rectangle2D rect = new Rectangle2D.Float(0, 0, dim.width, localGradientHeight);

            drawGradient(g2d, rect, firstLayer, localGradientHeight);
            rect = new Rectangle2D.Float(0, localGradientHeight, dim.width, dim.height);
            drawBackGradient(g2d, rect, firstLayer, localGradientHeight);

            int smallGradientHeight = localGradientHeight / 2;

            rect = new Rectangle2D.Float(0, 0, dim.width, smallGradientHeight);
            drawGradient(g2d, rect, secondLayer, smallGradientHeight);
            rect = new Rectangle2D.Float(0, dim.height - smallGradientHeight, dim.width, dim.height);
            drawBackGradient(g2d, rect, secondLayer, smallGradientHeight);
        }
    }
}
