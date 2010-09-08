/**
 *
 */
package org.richfaces.resource;

import javax.faces.context.FacesContext;

/**
 * @author Nick Belaevski
 * @since 4.0
 */
public interface ResourceCodec {
    
    public String encodeResourceRequestPath(FacesContext context, String libraryName, String resourceName, 
        Object resourceData, String resourceVersion);
    
    public String encodeJSFMapping(FacesContext context, String resourcePath);
    
    public ResourceRequestData decodeResource(FacesContext context, String requestPath);
}
