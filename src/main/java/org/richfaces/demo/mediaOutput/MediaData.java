package org.richfaces.demo.mediaOutput;

import org.richfaces.resource.SerializableResource;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class MediaData implements SerializableResource {
    private static final Color[] COLORS = { new Color(255, 0, 0), new Color(0, 0, 255), new Color(0, 255, 0),
            new Color(255, 255, 0), new Color(0, 255, 255) };
    private static final long serialVersionUID = 1L;
    private int colorIndex1 = 0;
    private int colorIndex2 = 1;
    private int colorIndex3 = 2;

    public Color[] getNewColors() {
        return new Color[] { COLORS[colorIndex1], COLORS[colorIndex2], COLORS[colorIndex3], };
    }

    public int getColorIndex1() {
        return colorIndex1;
    }

    public void setColorIndex1(int colorIndex1) {
        this.colorIndex1 = colorIndex1;
    }

    public int getColorIndex2() {
        return colorIndex2;
    }

    public void setColorIndex2(int colorIndex2) {
        this.colorIndex2 = colorIndex2;
    }

    public int getColorIndex3() {
        return colorIndex3;
    }

    public void setColorIndex3(int colorIndex3) {
        this.colorIndex3 = colorIndex3;
    }
}