package org.richfaces.cdk;

import java.io.File;
import java.net.URI;

import org.richfaces.cdk.model.ClassName;
import org.richfaces.cdk.model.ComponentLibrary;
import org.richfaces.cdk.model.ComponentModel;
import org.richfaces.cdk.model.FacesId;
import org.richfaces.cdk.model.InvalidNameException;
import org.richfaces.cdk.model.Name;
import org.richfaces.cdk.model.Name.Classifier;
import org.richfaces.cdk.model.RendererModel;
import org.richfaces.cdk.util.Strings;

import com.google.inject.Inject;

/**
 * <p class="changed_added_4_0">
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
public final class RichFaces5Conventions implements NamingConventions {
    private static final String ABSTRACT = "Abstract";
    private static final String BASE = "Base";
    private static final String UI = "UI";
    private static final String[] COMPONENT_SUFFIXES = { BASE };
    private static final String[] COMPONENT_PREFIXES = { UI, ABSTRACT };
    // TODO - inject base name.
    private String baseName;// = "org.richfaces";

    @Inject
    private ComponentLibrary library;

    public RichFaces5Conventions() {
    }

    @Override
    public FacesId inferComponentType(ClassName componentClass) throws InvalidNameException {

        Name name = inferNameByClass(componentClass, null, null);

        return FacesId.parseId("org.richfaces." + name.getSimpleName());
    }

    private Name inferNameByClass(ClassName componentClass, Classifier classifier, String markup) {
        // check parameters.
        if (null == componentClass) {
            throw new IllegalArgumentException();
        }

        Name name = Name.create(componentClass.toString());

        processName(classifier, markup, name);
        return name;
    }

    private Name inferNameByType(FacesId type, Classifier classifier, String markup) {
        // check parameters.
        if (null == type) {
            throw new IllegalArgumentException();
        }

        Name name = Name.create(type.toString());

        processName(classifier, markup, name);
        return name;
    }

    private void processName(Classifier classifier, String markup, Name name) {
        // Use base library prefix.
        String baseName = this.getBaseName();

        if (null != baseName) {
            name.setPrefix(baseName);
        }

        // JsfComponent type does not contain class or markup parts.
        name.setClassifier(classifier);
        name.setMarkup(markup);

        String simpleName = name.getSimpleName();

        simpleName = removeCommonPreffix(simpleName);

        simpleName = removeCommonSuffix(simpleName);

        name.setSimpleName(simpleName);
    }

    @Override
    public ClassName inferUIComponentClass(FacesId componentType) throws InvalidNameException {
        if (null == componentType) {
            throw new IllegalArgumentException();
        }

        // Infer UI class name from component type.
        Name name = inferBasicName(componentType);
        name.setSimpleName(UI + name.getSimpleName());

        return new ClassName(name.toString());
    }

    @Override
    public FacesId inferUIComponentFamily(FacesId componentType) {
        if (null == componentType) {
            throw new IllegalArgumentException();
        }
        return FacesId.parseId(componentType.toString() + "Family");
    }

    @Override
    public ClassName inferTagHandlerClass(FacesId componentType, String markup) {
        if (null == componentType) {
            throw new IllegalArgumentException();
        }

        Name name = inferBasicName(componentType);
        name.setSimpleName(name.getSimpleName() + "Handler");

        return new ClassName(name.toString());
    }

    private Name inferBasicName(FacesId componentType) {
        ComponentModel component = library.getComponentByType(componentType);
        String baseClassName = component.getBaseClass().getName();
        Name name = Name.create(baseClassName);

        name.setClassifier(null);
        name.setMarkup(null);

        String simpleName = name.getSimpleName();
        simpleName = simpleName.replaceFirst("^Abstract", "");

        return name;
    }

    @Override
    public String inferTagName(FacesId componentType) {
        if (null == componentType) {
            throw new IllegalArgumentException();
        }
        Name name = Name.create(componentType.toString());
        return Strings.firstToLowerCase(name.getSimpleName());
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the baseName
     */
    protected String getBaseName() {
        return baseName;
    }

    public FacesId inferRendererTypeByTemplatePath(String templateName) {
        String rendererType = new File(templateName).getName();
        rendererType = rendererType.split("\\.")[0];
        rendererType = rendererType.replaceFirst("^.", new String(new char[] { rendererType.charAt(0) }).toUpperCase());

        return FacesId.parseId("org.richfaces." + rendererType + "Renderer");
    }

    @Override
    public ClassName inferBehaviorClass(FacesId id) {
        if (null == id) {
            throw new IllegalArgumentException();
        }

        // Infer UI class name from component type.
        Name name = Name.create(id.toString());

        name.setClassifier(Classifier.component);
        // All Behavior classes belongs to "component.behavior" package.
        name.setMarkup("behavior");

        return new ClassName(name.toString());
    }

    @Override
    public FacesId inferBehaviorType(ClassName targetClass) {
        Name name = inferNameByClass(targetClass, Name.Classifier.component, "html");

        return new FacesId(name.toString());
    }

    private String removeCommonSuffix(String simpleName) {
        for (int i = 0; i < COMPONENT_SUFFIXES.length; i++) {
            if (simpleName.endsWith(COMPONENT_SUFFIXES[i])) {
                simpleName = simpleName.substring(0, simpleName.length() - COMPONENT_SUFFIXES[i].length());

                break;
            }
        }
        return simpleName;
    }

    private String removeCommonPreffix(String simpleName) {
        for (int i = 0; i < COMPONENT_PREFIXES.length; i++) {
            if (simpleName.startsWith(COMPONENT_PREFIXES[i])) {
                simpleName = simpleName.substring(COMPONENT_PREFIXES[i].length());

                break;
            }
        }
        return simpleName;
    }

    @Override
    public String inferTaglibName(String uri) throws InvalidNameException {
        try {
            URI taglibUri = URI.create(uri);
            String path = taglibUri.getPath();
            if (null != path) {
                int lastIndexOfPathSeparator = path.lastIndexOf('/');
                if (lastIndexOfPathSeparator >= 0) {
                    path = path.substring(lastIndexOfPathSeparator + 1);
                }
                int indexOfDot = path.indexOf('.');
                if (indexOfDot > 0) {
                    path = path.substring(0, indexOfDot);
                }
                return path;
            } else {
                throw new InvalidNameException("Invalid taglib uri, no path defined " + uri);
            }
        } catch (IllegalArgumentException e) {
            throw new InvalidNameException("Invalid taglib uri " + uri + " , " + e.getMessage());
        }
    }

    @Override
    public String inferTaglibUri(ComponentLibrary library) {
        // TODO infer name from library base names.
        return "http://richfaces.org/rich";
    }

    @Override
    public ClassName inferRendererClass(FacesId rendererType) {
        //return ClassName.get(inferNameByType(id, Classifier.renderkit, null).toString());

        if (null == rendererType) {
            throw new IllegalArgumentException();
        }

        // Infer UI class name from component type.
        RendererModel renderer = library.getRendererByType(rendererType);
        String baseClassName = renderer.getBaseClass().getName();
        Name name = Name.create(baseClassName);

        name.setClassifier(null);
        name.setMarkup(null);

        String simpleName = name.getSimpleName();
        simpleName = simpleName.replaceFirst("Base$", "");

        name.setSimpleName(simpleName);

        return new ClassName(name.toString());
    }

    public FacesId inferRendererFamily(FacesId type) {
        return FacesId.parseId(inferNameByType(type, null, null).toString());
    }

    @Override
    public FacesId inferRendererType(ClassName targetClass) {
        return FacesId.parseId(inferNameByClass(targetClass, Classifier.renderkit, null).toString());
    }

    @Override
    public FacesId inferRendererType(FacesId id) {
        return FacesId.parseId(inferNameByType(id, Classifier.renderkit, null).toString());
    }
}
