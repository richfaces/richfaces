/**
 * 
 */
package org.richfaces.demo.model.tree;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.xml.bind.JAXB;

import org.richfaces.demo.model.tree.adaptors.Root;

/**
 * @author Nick Belaevski
 *         mailto:nbelaevski@exadel.com
 *         created 25.07.2007
 *
 */
@ManagedBean
@ApplicationScoped
public class TreeModelBean {
	
    private Root root;
    
    private void initializeRoot() {
        root = JAXB.unmarshal(TreeModelBean.class.getResource("tree-model-data.xml"), Root.class);
    }
    
    public Root getRoot() {
        if (root == null) {
            initializeRoot();
        }
        
        return root;
    }

}
