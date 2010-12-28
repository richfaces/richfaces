package org.richfaces.component;

/**
 * @author amarkhel
 */
public enum Positioning {
    topRight("RT"),
    topLeft("LT"),
    bottomRight("BR"),
    bottomLeft("BL"),

    auto("AA"),
    topAuto("AT"),
    bottomAuto("AB"),
    autoRight("RA"),
    autoLeft("LA");

    public static final Positioning DEFAULT = bottomRight;

    String value;

    Positioning(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
