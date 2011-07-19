package org.richfaces.component;

import javax.faces.component.UIInput;

import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;

@JsfComponent(type = AbstractEditor.COMPONENT_TYPE, family = AbstractEditor.COMPONENT_FAMILY, generate = "org.richfaces.component.UIEditor", renderer = @JsfRenderer(type = "org.richfaces.EditorRenderer"), tag = @Tag(name = "editor"), attributes = "core-props.xml")
public class AbstractEditor extends UIInput {
    public static final String COMPONENT_TYPE = "org.richfaces.Editor";
    public static final String COMPONENT_FAMILY = "org.richfaces.Editor";
}
