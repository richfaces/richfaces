/**
 * 
 */
package org.richfaces.component;

import java.beans.FeatureDescriptor;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.el.ELContext;
import javax.el.ELResolver;
import javax.faces.context.FacesContext;

import org.richfaces.component.UIGraphValidator.GraphValidatorState;

/**
 * @author asmirnov
 *
 */
public class ClonedObjectResolver extends ELResolver {

	/* (non-Javadoc)
	 * @see javax.el.ELResolver#getCommonPropertyType(javax.el.ELContext, java.lang.Object)
	 */
	@Override
	public Class<?> getCommonPropertyType(ELContext context, Object base) {
		// Do nothing
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.el.ELResolver#getFeatureDescriptors(javax.el.ELContext, java.lang.Object)
	 */
	@Override
	public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context,
			Object base) {
		// do nothing
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.el.ELResolver#getType(javax.el.ELContext, java.lang.Object, java.lang.Object)
	 */
	@Override
	public Class<?> getType(ELContext context, Object base, Object property) {
		Object cloned = resolveCloned(context, base, property);
		if(null != cloned){
			context.setPropertyResolved(true);
			return cloned.getClass();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.el.ELResolver#getValue(javax.el.ELContext, java.lang.Object, java.lang.Object)
	 */
	@Override
	public Object getValue(ELContext context, Object base, Object property) {
		Object cloned = resolveCloned(context, base, property);
		if(null != cloned){
			context.setPropertyResolved(true);
		}
		return cloned;
	}
		

	/* (non-Javadoc)
	 * @see javax.el.ELResolver#isReadOnly(javax.el.ELContext, java.lang.Object, java.lang.Object)
	 */
	@Override
	public boolean isReadOnly(ELContext context, Object base, Object property) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see javax.el.ELResolver#setValue(javax.el.ELContext, java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void setValue(ELContext context, Object base, Object property,
			Object value) {
		// TODO Auto-generated method stub

	}

	public static Object resolveCloned(ELContext context, Object base, Object property){
		if(null != base || null != property){
			FacesContext facesContext = FacesContext.getCurrentInstance();
			Map<String, Object> requestMap = facesContext.getExternalContext().getRequestMap();
			for (String key : requestMap.keySet()) {
				if(null != key && key.startsWith(UIGraphValidator.STATE_ATTRIBUTE_PREFIX)){
					UIGraphValidator.GraphValidatorState state = (GraphValidatorState) requestMap.get(key);
					if(state.isSame(base, property)){
						return state.getCloned();
					}
				}
			}
		}
		return null;
	}
}
