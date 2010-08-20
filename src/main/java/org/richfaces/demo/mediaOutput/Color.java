package org.richfaces.demo.mediaOutput;

/**
 * @author Ilya Shaikovsky Class created to hold rgb properties of the color. Used in order to avoid AWT dependencies as
 *         GAE not allows them
 */
public class Color {

    private int red;
    private int green;
    private int blue;

    public Color(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public int getRed() {
        return red;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public int getGreen() {
        return green;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public int getBlue() {
        return blue;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }
    @Override
    public String toString() {
        return String.valueOf(getRed()) + String.valueOf(getGreen()) + String.valueOf(getBlue());
    }
}
