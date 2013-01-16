package org.richfaces.photoalbum.util;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Named;

import org.jboss.solder.beanManager.BeanManagerLocator;
import org.richfaces.photoalbum.bean.UserBean;
import org.richfaces.photoalbum.domain.Album;

@Named
@RequestScoped
@FacesConverter("albumConverter")
public class AlbumConverter implements Converter {

    private BeanManager getBeanManager() {
        return new BeanManagerLocator().getBeanManager();
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        BeanManager bm = getBeanManager();
        Bean<UserBean> bean = (Bean<UserBean>) bm.getBeans(UserBean.class).iterator().next();
        CreationalContext<UserBean> ctx = bm.createCreationalContext(bean);
        UserBean userBean = (UserBean) bm.getReference(bean, UserBean.class, ctx); // this could be inlined, but intentionally
                                                                                   // left this way

        for (Album a : userBean.getUser().getAlbums()) {
            if (a.getName().equals(value)) {
                return a;
            }
        }

        return new Album();
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }

        return ((Album) value).getName();
    }

}
