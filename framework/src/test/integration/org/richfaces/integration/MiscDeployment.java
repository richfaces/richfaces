package org.richfaces.integration;

import org.richfaces.deployment.FrameworkDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

public class MiscDeployment extends FrameworkDeployment {

    public MiscDeployment(Class<?> testClass) {
        super(testClass);
    }

    public FaceletAsset baseFacelet(String name) {
        FaceletAsset p = new FaceletAsset();

        this.archive().add(p, name);

        return p;
    }
}
