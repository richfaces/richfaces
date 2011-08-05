package org.richfaces;

/**
 * Defines where new items will appear
 *
 * @author lfryc
 *
 */
public enum StackingMethod {
    /**
     * new items appears on the start of the stack (as first)
     */
    first,
    /**
     * new items appears on the end of the stack (as last)
     */
    last;

    public static final StackingMethod DEFAULT = first;
}
