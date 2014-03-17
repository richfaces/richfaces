package org.richfaces.component;

/**
 * @author amarkhel
 */
public enum Positioning {
    auto("AA"),
    topLeft("LT"),
    topRight("RT"),
    bottomLeft("LB"),
    bottomRight("RB"),
    autoLeft("LA"),
    autoRight("RA"),
    topAuto("AT"),
    bottomAuto("AB");
    public static final Positioning DEFAULT = auto;
    String value;

    Positioning(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
