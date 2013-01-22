/**
 *
 */
package org.richfaces.demo.model.tree;

import javax.faces.FacesException;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.Unmarshaller.Listener;

import org.richfaces.demo.model.tree.adaptors.Entry;
import org.richfaces.demo.model.tree.adaptors.Root;

/**
 * @author Nick Belaevski mailto:nbelaevski@exadel.com created 25.07.2007
 *
 */
@ManagedBean
@ApplicationScoped
public class TreeModelDataBean {
    private Root root;

    private void initializeRoot() {

        try {
            JAXBContext context = JAXBContext.newInstance(Root.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            unmarshaller.setListener(new Listener() {
                @Override
                public void afterUnmarshal(Object target, Object parent) {
                    super.afterUnmarshal(target, parent);

                    if (parent instanceof Entry) {
                        ((Entry) target).setParent((Entry) parent);
                    }
                }
            });

            root = (Root) unmarshaller.unmarshal(TreeModelDataBean.class.getResource("tree-model-data.xml"));
        } catch (JAXBException e) {
            throw new FacesException(e.getMessage(), e);
        }
    }

    public Root getRoot() {
        if (root == null) {
            initializeRoot();
        }

        return root;
    }
}
