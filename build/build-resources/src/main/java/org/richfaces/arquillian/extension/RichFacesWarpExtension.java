package org.richfaces.arquillian.extension;

import org.jboss.arquillian.container.test.spi.RemoteLoadableExtension;
import org.richfaces.arquillian.warp.WarpRequestSynchronizer;

public class RichFacesWarpExtension implements RemoteLoadableExtension {

    @Override
    public void register(ExtensionBuilder builder) {
        builder.observer(WarpRequestSynchronizer.class);
    }
}
