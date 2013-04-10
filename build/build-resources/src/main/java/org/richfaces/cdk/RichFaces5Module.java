package org.richfaces.cdk;

import com.google.inject.AbstractModule;

public class RichFaces5Module extends AbstractModule implements CdkModule {

    @Override
    protected void configure() {
        bind(ModelValidator.class).to(RichFaces5Validator.class);
        bind(NamingConventions.class).to(RichFaces5Conventions.class);
    }

}
