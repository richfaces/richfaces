package org.richfaces.photoalbum.util;

import java.lang.reflect.Type;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jboss.solder.reflection.annotated.AnnotatedTypeBuilder;
import org.jboss.solder.servlet.http.ImplicitHttpServletObjectsProducer;

// workaround for SOLDER-332
public class Solder332Workaround implements Extension {

    public void vetoHttpServletObjectsProducer(@Observes ProcessAnnotatedType<ImplicitHttpServletObjectsProducer> event) {
        AnnotatedTypeBuilder<ImplicitHttpServletObjectsProducer> builder = new AnnotatedTypeBuilder<ImplicitHttpServletObjectsProducer>();
        builder.readFromType(event.getAnnotatedType());

        // remove producer methods for HttpServletRequest, HttpSession and ServletContext
        for (AnnotatedMethod<? super ImplicitHttpServletObjectsProducer> method : event.getAnnotatedType().getMethods()) {
            Type type = method.getBaseType();
            if (HttpServletRequest.class.equals(type) || HttpSession.class.equals(type) || ServletContext.class.equals(type)) {
                builder.removeFromMethod(method, Produces.class);
            }
        }
        event.setAnnotatedType(builder.create());
    }
}
